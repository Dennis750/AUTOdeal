package ro.autodeal.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.autodeal.event.CarPostCreatedEvent;
import ro.autodeal.event.CarPostDeletedEvent;
import ro.autodeal.event.CarPostUpdatedEvent;
import ro.autodeal.model.Brand;
import ro.autodeal.model.CarModel;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.repository.BrandRepository;
import ro.autodeal.repository.CarModelRepository;
import ro.autodeal.repository.CarPostRepository;
import ro.autodeal.repository.UserRepository;

@Service
public class CarPostCommandService {

    private static final String DEFAULT_NOTIFICATION_EMAIL = "denniscortel@gmail.com";

    private final CarPostRepository carPostRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserClientService userClientService;

    public CarPostCommandService(CarPostRepository carPostRepository,
                                 BrandRepository brandRepository,
                                 CarModelRepository carModelRepository,
                                 UserRepository userRepository,
                                 ApplicationEventPublisher eventPublisher,
                                 UserClientService userClientService) {
        this.carPostRepository = carPostRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.userClientService = userClientService;
    }

    public void createCarPost(Long brandId,
                              Long modelId,
                              Integer year,
                              Integer price,
                              Integer mileage,
                              FuelType fuelType,
                              Integer horsepower,
                              String city,
                              String imageUrl) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        CarModel model = carModelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        User seller = getCurrentLoggedInUser();

        String roleFromUserService = userClientService.getUserRole(seller.getUsername());

        if (!"SELLER".equals(roleFromUserService) && !"ADMIN".equals(roleFromUserService)) {
            throw new RuntimeException("Only sellers or admins can create posts");
        }

        if (seller.getEmail() == null || seller.getEmail().isBlank()) {
            seller.setEmail(DEFAULT_NOTIFICATION_EMAIL);
            seller = userRepository.save(seller);
        }

        CarPost carPost = new CarPost();
        carPost.setBrand(brand);
        carPost.setModel(model);
        carPost.setYear(year);
        carPost.setPrice(price);
        carPost.setMileage(mileage);
        carPost.setFuelType(fuelType);
        carPost.setHorsepower(horsepower);
        carPost.setCity(city);
        carPost.setVisible(true);
        carPost.setImageUrl(imageUrl != null && !imageUrl.isBlank() ? imageUrl.trim() : null);
        carPost.setSeller(seller);

        carPostRepository.save(carPost);

        String message = String.format("""
AUTOdeal Notification

Hello %s,

Your car listing has been successfully created.

Car: %s %s
Year: %d
Price: €%,d

Best regards,
AUTOdeal Team
""",
                seller.getUsername(),
                brand.getName(),
                model.getName(),
                year,
                price
        );

        eventPublisher.publishEvent(
                new CarPostCreatedEvent(
                        seller.getUsername(),
                        seller.getEmail(),
                        message
                )
        );
    }

    public CarPost createCarPostFromApi(String sellerUsername,
                                        Long brandId,
                                        Long modelId,
                                        Integer year,
                                        Integer price,
                                        Integer mileage,
                                        FuelType fuelType,
                                        Integer horsepower,
                                        String city,
                                        String imageUrl) {

        if (sellerUsername == null || sellerUsername.isBlank()) {
            throw new RuntimeException("You must be logged in to create a car post");
        }

        String roleFromUserService = userClientService.getUserRole(sellerUsername);

        if (!"SELLER".equals(roleFromUserService) && !"ADMIN".equals(roleFromUserService)) {
            throw new RuntimeException("Only sellers or admins can create posts");
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        CarModel model = carModelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        User seller = userRepository.findByUsername(sellerUsername)
                .orElseGet(() -> {
                    User newSeller = new User();
                    newSeller.setUsername(sellerUsername);
                    newSeller.setPassword("external-user");
                    newSeller.setName(sellerUsername);
                    newSeller.setPhoneNumber("N/A");
                    newSeller.setEmail(DEFAULT_NOTIFICATION_EMAIL);
                    newSeller.setRole("ADMIN".equals(roleFromUserService) ? Role.ADMIN : Role.SELLER);
                    return userRepository.save(newSeller);
                });

        if (seller.getEmail() == null || seller.getEmail().isBlank()) {
            seller.setEmail(DEFAULT_NOTIFICATION_EMAIL);
            seller = userRepository.save(seller);
        }

        CarPost carPost = new CarPost();
        carPost.setBrand(brand);
        carPost.setModel(model);
        carPost.setYear(year);
        carPost.setPrice(price);
        carPost.setMileage(mileage);
        carPost.setFuelType(fuelType);
        carPost.setHorsepower(horsepower);
        carPost.setCity(city);
        carPost.setVisible(true);
        carPost.setImageUrl(imageUrl != null && !imageUrl.isBlank() ? imageUrl.trim() : null);
        carPost.setSeller(seller);

        CarPost savedPost = carPostRepository.save(carPost);

        String message = String.format("""
AUTOdeal Notification

Hello %s,

Your car listing has been successfully created.

Car: %s %s
Year: %d
Price: €%,d

Best regards,
AUTOdeal Team
""",
                seller.getUsername(),
                brand.getName(),
                model.getName(),
                year,
                price
        );

        eventPublisher.publishEvent(
                new CarPostCreatedEvent(
                        seller.getUsername(),
                        seller.getEmail(),
                        message
                )
        );

        return savedPost;
    }

    public void updateCarPost(Long id,
                              Long brandId,
                              Long modelId,
                              Integer year,
                              Integer price,
                              Integer mileage,
                              FuelType fuelType,
                              Integer horsepower,
                              String city,
                              String imageUrl) {

        CarPost existingPost = carPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car post not found"));

        User currentUser = getCurrentLoggedInUser();

        String roleFromUserService = userClientService.getUserRole(currentUser.getUsername());

        boolean isAdmin = "ADMIN".equals(roleFromUserService);
        boolean isOwner = existingPost.getSeller().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You can edit only your own posts");
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        CarModel model = carModelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        existingPost.setBrand(brand);
        existingPost.setModel(model);
        existingPost.setYear(year);
        existingPost.setPrice(price);
        existingPost.setMileage(mileage);
        existingPost.setFuelType(fuelType);
        existingPost.setHorsepower(horsepower);
        existingPost.setCity(city);
        existingPost.setImageUrl(imageUrl != null && !imageUrl.isBlank() ? imageUrl.trim() : null);

        carPostRepository.save(existingPost);

        if (currentUser.getEmail() == null || currentUser.getEmail().isBlank()) {
            currentUser.setEmail(DEFAULT_NOTIFICATION_EMAIL);
            currentUser = userRepository.save(currentUser);
        }

        String message = String.format("""
AUTOdeal Notification

Hello %s,

Your car listing has been successfully updated.

Car: %s %s
Year: %d
Price: €%,d

Best regards,
AUTOdeal Team
""",
                currentUser.getUsername(),
                brand.getName(),
                model.getName(),
                year,
                price
        );

        eventPublisher.publishEvent(
                new CarPostUpdatedEvent(
                        currentUser.getUsername(),
                        currentUser.getEmail(),
                        message
                )
        );
    }

    public void deleteById(Long id) {
        CarPost existingPost = carPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car post not found"));

        User currentUser = getCurrentLoggedInUser();

        String roleFromUserService = userClientService.getUserRole(currentUser.getUsername());

        boolean isAdmin = "ADMIN".equals(roleFromUserService);
        boolean isOwner = existingPost.getSeller().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You can delete only your own posts");
        }

        if (currentUser.getEmail() == null || currentUser.getEmail().isBlank()) {
            currentUser.setEmail(DEFAULT_NOTIFICATION_EMAIL);
            currentUser = userRepository.save(currentUser);
        }

        String message = String.format("""
AUTOdeal Notification

Hello %s,

Your car listing has been successfully deleted.

Car: %s %s

Best regards,
AUTOdeal Team
""",
                currentUser.getUsername(),
                existingPost.getBrand().getName(),
                existingPost.getModel().getName()
        );

        carPostRepository.deleteById(id);

        eventPublisher.publishEvent(
                new CarPostDeletedEvent(
                        currentUser.getUsername(),
                        currentUser.getEmail(),
                        message
                )
        );
    }

    public void deleteByIdFromApi(Long id, String username) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("You must be logged in to delete a car post");
        }

        CarPost existingPost = carPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car post not found"));

        String roleFromUserService = userClientService.getUserRole(username);

        boolean isAdmin = "ADMIN".equals(roleFromUserService);
        boolean isOwner = existingPost.getSeller() != null
                && existingPost.getSeller().getUsername() != null
                && existingPost.getSeller().getUsername().equals(username);

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You can delete only your own posts");
        }

        User seller = existingPost.getSeller();

        if (seller != null && (seller.getEmail() == null || seller.getEmail().isBlank())) {
            seller.setEmail(DEFAULT_NOTIFICATION_EMAIL);
            seller = userRepository.save(seller);
        }

        String notificationUsername = seller != null ? seller.getUsername() : username;
        String notificationEmail = seller != null ? seller.getEmail() : DEFAULT_NOTIFICATION_EMAIL;

        String message = String.format("""
AUTOdeal Notification

Hello %s,

Your car listing has been successfully deleted.

Car: %s %s

Best regards,
AUTOdeal Team
""",
                notificationUsername,
                existingPost.getBrand().getName(),
                existingPost.getModel().getName()
        );

        carPostRepository.deleteById(id);

        eventPublisher.publishEvent(
                new CarPostDeletedEvent(
                        notificationUsername,
                        notificationEmail,
                        message
                )
        );
    }

    public CarPost updateCarPostFromApi(Long id,
                                        String username,
                                        Long brandId,
                                        Long modelId,
                                        Integer year,
                                        Integer price,
                                        Integer mileage,
                                        FuelType fuelType,
                                        Integer horsepower,
                                        String city,
                                        String imageUrl) {

        if (username == null || username.isBlank()) {
            throw new RuntimeException("You must be logged in to update a car post");
        }

        CarPost existingPost = carPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car post not found"));

        String roleFromUserService = userClientService.getUserRole(username);

        boolean isAdmin = "ADMIN".equals(roleFromUserService);
        boolean isOwner = existingPost.getSeller() != null
                && existingPost.getSeller().getUsername() != null
                && existingPost.getSeller().getUsername().equals(username);

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You can edit only your own posts");
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        CarModel model = carModelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        existingPost.setBrand(brand);
        existingPost.setModel(model);
        existingPost.setYear(year);
        existingPost.setPrice(price);
        existingPost.setMileage(mileage);
        existingPost.setFuelType(fuelType);
        existingPost.setHorsepower(horsepower);
        existingPost.setCity(city);
        existingPost.setImageUrl(imageUrl != null && !imageUrl.isBlank() ? imageUrl.trim() : null);

        CarPost savedPost = carPostRepository.save(existingPost);

        User seller = savedPost.getSeller();

        if (seller != null && (seller.getEmail() == null || seller.getEmail().isBlank())) {
            seller.setEmail(DEFAULT_NOTIFICATION_EMAIL);
            seller = userRepository.save(seller);
        }

        String notificationUsername = seller != null ? seller.getUsername() : username;
        String notificationEmail = seller != null ? seller.getEmail() : DEFAULT_NOTIFICATION_EMAIL;

        String message = String.format("""
AUTOdeal Notification

Hello %s,

Your car listing has been successfully updated.

Car: %s %s
Year: %d
Price: €%,d

Best regards,
AUTOdeal Team
""",
                notificationUsername,
                brand.getName(),
                model.getName(),
                year,
                price
        );

        eventPublisher.publishEvent(
                new CarPostUpdatedEvent(
                        notificationUsername,
                        notificationEmail,
                        message
                )
        );

        return savedPost;
    }

    private User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
    }
}