package ro.autodeal.command;

import ro.autodeal.model.FuelType;
import ro.autodeal.service.CarPostCommandService;

public class UpdateCarPostCommand implements CarPostCommand {

    private final CarPostCommandService carPostCommandService;
    private final Long id;
    private final Long brandId;
    private final Long modelId;
    private final Integer year;
    private final Integer price;
    private final Integer mileage;
    private final FuelType fuelType;
    private final Integer horsepower;
    private final String city;
    private final String imageUrl;

    public UpdateCarPostCommand(CarPostCommandService carPostCommandService,
                                Long id,
                                Long brandId,
                                Long modelId,
                                Integer year,
                                Integer price,
                                Integer mileage,
                                FuelType fuelType,
                                Integer horsepower,
                                String city,
                                String imageUrl) {
        this.carPostCommandService = carPostCommandService;
        this.id = id;
        this.brandId = brandId;
        this.modelId = modelId;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.horsepower = horsepower;
        this.city = city;
        this.imageUrl = imageUrl;
    }

    @Override
    public void execute() {
        carPostCommandService.updateCarPost(
                id,
                brandId,
                modelId,
                year,
                price,
                mileage,
                fuelType,
                horsepower,
                city,
                imageUrl
        );
    }
}