package ro.autodeal.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;

public class CarPostSpecification {

    public static Specification<CarPost> isVisible() {
        return (root, query, cb) -> cb.isTrue(root.get("visible"));
    }

    public static Specification<CarPost> hasBrandId(Long brandId) {
        return (root, query, cb) -> cb.equal(root.get("brand").get("id"), brandId);
    }

    public static Specification<CarPost> hasMinPrice(Integer minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<CarPost> hasMaxPrice(Integer maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<CarPost> hasYear(Integer year) {
        return (root, query, cb) -> cb.equal(root.get("year"), year);
    }

    public static Specification<CarPost> hasFuelType(FuelType fuelType) {
        return (root, query, cb) -> cb.equal(root.get("fuelType"), fuelType);
    }
}