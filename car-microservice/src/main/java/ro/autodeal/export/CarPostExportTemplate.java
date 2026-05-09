package ro.autodeal.export;

import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.service.CarPostQueryService;

import java.util.List;

public abstract class CarPostExportTemplate {

    private final CarPostQueryService carPostQueryService;

    public CarPostExportTemplate(CarPostQueryService carPostQueryService) {
        this.carPostQueryService = carPostQueryService;
    }

    public final String export(Long brandId,
                               Integer minPrice,
                               Integer maxPrice,
                               Integer year,
                               FuelType fuelType,
                               String sortBy) {

        List<CarPost> posts = fetchData(brandId, minPrice, maxPrice, year, fuelType, sortBy);
        String transformedData = transform(posts);
        return writeOutput(transformedData);
    }

    protected List<CarPost> fetchData(Long brandId,
                                      Integer minPrice,
                                      Integer maxPrice,
                                      Integer year,
                                      FuelType fuelType,
                                      String sortBy) {

        return carPostQueryService.getFilteredPostsForExport(
                brandId,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy
        );
    }

    protected abstract String transform(List<CarPost> carPosts);

    protected String writeOutput(String content) {
        return content;
    }
}