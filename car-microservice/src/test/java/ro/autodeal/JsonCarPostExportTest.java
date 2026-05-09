package ro.autodeal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ro.autodeal.export.JsonCarPostExport;
import ro.autodeal.model.*;
import ro.autodeal.service.CarPostQueryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class JsonCarPostExportTest {

    private CarPostQueryService carPostQueryService;
    private JsonCarPostExport jsonCarPostExport;

    @BeforeEach
    void setUp() {
        carPostQueryService = Mockito.mock(CarPostQueryService.class);
        jsonCarPostExport = new JsonCarPostExport(carPostQueryService);
    }

    @Test
    void exportShouldReturnJsonContentForCarPosts() {
        Brand brand = new Brand();
        brand.setName("BMW");

        CarModel model = new CarModel();
        model.setName("M3");
        model.setBrand(brand);

        User seller = new User();
        seller.setUsername("seller1");

        CarPost post = new CarPost();
        post.setId(1L);
        post.setBrand(brand);
        post.setModel(model);
        post.setYear(2018);
        post.setPrice(35000);
        post.setMileage(90000);
        post.setFuelType(FuelType.PETROL);
        post.setHorsepower(425);
        post.setCity("Cluj-Napoca");
        post.setSeller(seller);

        when(carPostQueryService.getFilteredPostsForExport(
                null,
                null,
                null,
                null,
                null,
                null
        )).thenReturn(List.of(post));

        String result = jsonCarPostExport.export(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue(result.contains("\"brand\": \"BMW\""));
        assertTrue(result.contains("\"model\": \"M3\""));
        assertTrue(result.contains("\"price\": 35000"));
        assertTrue(result.contains("\"seller\": \"seller1\""));
    }
}