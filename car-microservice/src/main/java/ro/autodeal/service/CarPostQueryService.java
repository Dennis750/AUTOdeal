package ro.autodeal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.repository.CarPostRepository;
import ro.autodeal.repository.UserRepository;
import ro.autodeal.repository.specification.CarPostSpecification;

import java.util.List;

@Service
public class CarPostQueryService {

    private final CarPostRepository carPostRepository;
    private final UserRepository userRepository;

    public CarPostQueryService(CarPostRepository carPostRepository,
                               UserRepository userRepository) {
        this.carPostRepository = carPostRepository;
        this.userRepository = userRepository;
    }

    public Page<CarPost> getFilteredPosts(Long brandId,
                                          Long modelId,
                                          Integer minPrice,
                                          Integer maxPrice,
                                          Integer year,
                                          FuelType fuelType,
                                          String sortBy,
                                          int page,
                                          int size) {

        Specification<CarPost> spec = buildFilterSpecification(
                brandId,
                modelId,
                minPrice,
                maxPrice,
                year,
                fuelType
        );

        Sort sort = getSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return carPostRepository.findAll(spec, pageRequest);
    }

    public Page<CarPost> getFilteredPosts(Long brandId,
                                          Integer minPrice,
                                          Integer maxPrice,
                                          Integer year,
                                          FuelType fuelType,
                                          String sortBy,
                                          int page,
                                          int size) {

        return getFilteredPosts(
                brandId,
                null,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy,
                page,
                size
        );
    }

    public List<CarPost> getFilteredPostsForExport(Long brandId,
                                                   Long modelId,
                                                   Integer minPrice,
                                                   Integer maxPrice,
                                                   Integer year,
                                                   FuelType fuelType,
                                                   String sortBy) {

        Specification<CarPost> spec = buildFilterSpecification(
                brandId,
                modelId,
                minPrice,
                maxPrice,
                year,
                fuelType
        );

        Sort sort = getSort(sortBy);

        return carPostRepository.findAll(spec, sort);
    }

    public List<CarPost> getFilteredPostsForExport(Long brandId,
                                                   Integer minPrice,
                                                   Integer maxPrice,
                                                   Integer year,
                                                   FuelType fuelType,
                                                   String sortBy) {

        return getFilteredPostsForExport(
                brandId,
                null,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy
        );
    }

    private Specification<CarPost> buildFilterSpecification(Long brandId,
                                                            Long modelId,
                                                            Integer minPrice,
                                                            Integer maxPrice,
                                                            Integer year,
                                                            FuelType fuelType) {

        Specification<CarPost> spec = Specification.where(CarPostSpecification.isVisible());

        if (brandId != null) {
            spec = spec.and(CarPostSpecification.hasBrandId(brandId));
        }

        if (modelId != null) {
            spec = spec.and(CarPostSpecification.hasModelId(modelId));
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

        return spec;
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
        return canEdit(post, username);
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