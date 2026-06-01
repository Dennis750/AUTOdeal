package ro.autodeal.dto;

import ro.autodeal.model.CarPost;

public class CarPostDto {

    private Long id;
    private Long brandId;
    private Long modelId;
    private String brand;
    private String model;
    private Integer year;
    private Integer price;
    private Integer mileage;
    private String fuelType;
    private Integer horsepower;
    private String city;
    private String imageUrl;
    private String sellerUsername;

    public CarPostDto() {
    }

    public static CarPostDto fromCarPost(CarPost carPost) {
        CarPostDto dto = new CarPostDto();

        dto.setId(carPost.getId());

        if (carPost.getBrand() != null) {
            dto.setBrandId(carPost.getBrand().getId());
            dto.setBrand(carPost.getBrand().getName());
        }

        if (carPost.getModel() != null) {
            dto.setModelId(carPost.getModel().getId());
            dto.setModel(carPost.getModel().getName());
        }

        dto.setYear(carPost.getYear());
        dto.setPrice(carPost.getPrice());
        dto.setMileage(carPost.getMileage());

        if (carPost.getFuelType() != null) {
            dto.setFuelType(carPost.getFuelType().name());
        }

        dto.setHorsepower(carPost.getHorsepower());
        dto.setCity(carPost.getCity());
        dto.setImageUrl(carPost.getImageUrl());

        if (carPost.getSeller() != null) {
            dto.setSellerUsername(carPost.getSeller().getUsername());
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getModelId() {
        return modelId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getMileage() {
        return mileage;
    }

    public String getFuelType() {
        return fuelType;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public String getCity() {
        return city;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
}