package ro.autodeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.autodeal.model.CarPost;

import java.util.List;

public interface CarPostRepository extends JpaRepository<CarPost, Long>, JpaSpecificationExecutor<CarPost> {
    List<CarPost> findByVisibleTrue();
}