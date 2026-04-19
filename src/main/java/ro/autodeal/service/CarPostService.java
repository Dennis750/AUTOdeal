package ro.autodeal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
import ro.autodeal.repository.specification.CarPostSpecification;
import org.springframework.context.ApplicationEventPublisher;
import ro.autodeal.event.CarPostCreatedEvent;
import ro.autodeal.event.CarPostDeletedEvent;
import ro.autodeal.event.CarPostUpdatedEvent;
import java.util.List;

@Service
public class CarPostService {

    private final CarPostRepository carPostRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CarPostService(CarPostRepository carPostRepository,
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

    public Page<CarPost> getFilteredPosts(Long brandId,
                                          Integer minPrice,
                                          Integer maxPrice,
                                          Integer year,
                                          FuelType fuelType,
                                          String sortBy,
                                          int page,
                                          int size) {

        Specification<CarPost> spec = Specification.where(CarPostSpecification.isVisible());

        if (brandId != null) {
            spec = spec.and(CarPostSpecification.hasBrandId(brandId));
        }

        if (minPrice != null) {
            spec = spec.and(CarPostSpecification.hasMinPrice(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(CarPostSpecification.hasMaxPrice(maxPrice));
        }

        if (year != null) {
            spec = spec.and(CarPostSpecification.hasYear(year));
        }

        if (fuelType != null) {
            spec = spec.and(CarPostSpecification.hasFuelType(fuelType));
        }

        Sort sort = getSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return carPostRepository.findAll(spec, pageRequest);
    }

    public List<CarPost> getFilteredPostsForExport(Long brandId,
                                                   Integer minPrice,
                                                   Integer maxPrice,
                                                   Integer year,
                                                   FuelType fuelType,
                                                   String sortBy) {

        Specification<CarPost> spec = Specification.where(CarPostSpecification.isVisible());

        if (brandId != null) {
            spec = spec.and(CarPostSpecification.hasBrandId(brandId));
        }

        if (minPrice != null) {
            spec = spec.and(CarPostSpecification.hasMinPrice(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(CarPostSpecification.hasMaxPrice(maxPrice));
        }

        if (year != null) {
            spec = spec.and(CarPostSpecification.hasYear(year));
        }

        if (fuelType != null) {
            spec = spec.and(CarPostSpecification.hasFuelType(fuelType));
        }

        Sort sort = getSort(sortBy);

        return carPostRepository.findAll(spec, sort);
    }

    public Page<CarPost> getMyPosts(String username,
                                    String sortBy,
                                    int page,
                                    int size) {

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Specification<CarPost> spec = Specification.where(CarPostSpecification.isVisible())
                .and((root, query, cb) -> cb.equal(root.get("seller").get("id"), currentUser.getId()));

        Sort sort = getSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return carPostRepository.findAll(spec, pageRequest);
    }

    public CarPost getById(Long id) {
        return carPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car post not found"));
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

    public boolean canEdit(CarPost post, String username) {
        if (username == null || "anonymousUser".equals(username)) {
            return false;
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.ADMIN || post.getSeller().getId().equals(user.getId());
    }

    public boolean canDelete(CarPost post, String username) {
        if (username == null || "anonymousUser".equals(username)) {
            return false;
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.ADMIN || post.getSeller().getId().equals(user.getId());
    }

    private User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
    }

    private Sort getSort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by("id").ascending();
        }

        return switch (sortBy) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            case "yearAsc" -> Sort.by("year").ascending();
            case "yearDesc" -> Sort.by("year").descending();
            case "mileageAsc" -> Sort.by("mileage").ascending();
            case "mileageDesc" -> Sort.by("mileage").descending();
            default -> Sort.by("id").ascending();
        };
    }
}