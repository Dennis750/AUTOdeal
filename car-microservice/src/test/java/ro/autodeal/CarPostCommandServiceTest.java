package ro.autodeal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ro.autodeal.model.*;
import ro.autodeal.repository.BrandRepository;
import ro.autodeal.repository.CarModelRepository;
import ro.autodeal.repository.CarPostRepository;
import ro.autodeal.repository.UserRepository;
import ro.autodeal.service.CarPostCommandService;
import ro.autodeal.service.UserClientService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CarPostCommandServiceTest {

    private CarPostRepository carPostRepository;
    private BrandRepository brandRepository;
    private CarModelRepository carModelRepository;
    private UserRepository userRepository;
    private ApplicationEventPublisher eventPublisher;
    private UserClientService userClientService;
    private CarPostCommandService carPostCommandService;

    @BeforeEach
    void setUp() {
        carPostRepository = Mockito.mock(CarPostRepository.class);
        brandRepository = Mockito.mock(BrandRepository.class);
        carModelRepository = Mockito.mock(CarModelRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        userClientService = Mockito.mock(UserClientService.class);

        carPostCommandService = new CarPostCommandService(
                carPostRepository,
                brandRepository,
                carModelRepository,
                userRepository,
                eventPublisher,
                userClientService
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("seller1", null)
        );
    }

    @Test
    void createCarPostShouldSaveCarWhenUserIsSeller() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("BMW");

        CarModel model = new CarModel();
        model.setId(1L);
        model.setName("M3");
        model.setBrand(brand);

        User seller = new User();
        seller.setId(1L);
        seller.setUsername("seller1");
        seller.setEmail("seller@example.com");
        seller.setRole(Role.SELLER);

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(userRepository.findByUsername("seller1")).thenReturn(Optional.of(seller));
        when(userClientService.getUserRole("seller1")).thenReturn("SELLER");

        carPostCommandService.createCarPost(
                1L,
                1L,
                2018,
                35000,
                90000,
                FuelType.PETROL,
                425,
                "Cluj-Napoca",
                null
        );

        verify(carPostRepository, times(1)).save(any(CarPost.class));
        verify(eventPublisher, times(1)).publishEvent(any(Object.class));
    }

    @Test
    void createCarPostShouldThrowExceptionWhenUserIsVisitor() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("BMW");

        CarModel model = new CarModel();
        model.setId(1L);
        model.setName("M3");
        model.setBrand(brand);

        User visitor = new User();
        visitor.setId(2L);
        visitor.setUsername("visitor1");
        visitor.setEmail("visitor@example.com");
        visitor.setRole(Role.VISITOR);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("visitor1", null)
        );

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(userRepository.findByUsername("visitor1")).thenReturn(Optional.of(visitor));
        when(userClientService.getUserRole("visitor1")).thenReturn("VISITOR");

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                carPostCommandService.createCarPost(
                        1L,
                        1L,
                        2018,
                        35000,
                        90000,
                        FuelType.PETROL,
                        425,
                        "Cluj-Napoca",
                        null
                )
        );

        verify(carPostRepository, never()).save(any(CarPost.class));
    }
}