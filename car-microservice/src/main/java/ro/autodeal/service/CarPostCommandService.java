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

    private final CarPostRepository carPostRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CarPostCommandService(CarPostRepository carPostRepository,
                                 BrandRepository brandRepository,
                                 CarModelRepository carModelRepository,
                                 UserRepository userRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.carPostRepository = carPostRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
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

        if (seller.getRole() != Role.SELLER && seller.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only sellers or admins can create posts");
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

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
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

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = existingPost.getSeller().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You can delete only your own posts");
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

    private User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
    }
}