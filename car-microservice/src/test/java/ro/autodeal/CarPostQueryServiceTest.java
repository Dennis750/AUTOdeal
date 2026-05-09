package ro.autodeal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.repository.CarPostRepository;
import ro.autodeal.repository.UserRepository;
import ro.autodeal.service.CarPostQueryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CarPostQueryServiceTest {

    private CarPostRepository carPostRepository;
    private UserRepository userRepository;
    private CarPostQueryService carPostQueryService;

    @BeforeEach
    void setUp() {
        carPostRepository = Mockito.mock(CarPostRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        carPostQueryService = new CarPostQueryService(carPostRepository, userRepository);
    }

    @Test
    void getFilteredPostsShouldReturnPageOfCars() {
        CarPost carPost = new CarPost();
        carPost.setId(1L);
        carPost.setYear(2020);
        carPost.setPrice(27000);
        carPost.setMileage(60000);
        carPost.setFuelType(FuelType.DIESEL);

        Page<CarPost> page = new PageImpl<>(List.of(carPost));

        when(carPostRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<CarPost> result = carPostQueryService.getFilteredPosts(
                null,
                null,
                null,
                null,
                null,
                "priceAsc",
                0,
                9
        );

        assertEquals(1, result.getContent().size());
        assertEquals(27000, result.getContent().get(0).getPrice());
    }

    @Test
    void getByIdShouldReturnCarPostWhenExists() {
        CarPost carPost = new CarPost();
        carPost.setId(1L);
        carPost.setPrice(35000);

        when(carPostRepository.findById(1L)).thenReturn(java.util.Optional.of(carPost));

        CarPost result = carPostQueryService.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals(35000, result.getPrice());
    }
}