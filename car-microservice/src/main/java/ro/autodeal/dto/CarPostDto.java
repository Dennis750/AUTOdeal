package ro.autodeal.dto;

import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;

public class CarPostDto {

    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private Integer price;
    private Integer mileage;
    private FuelType fuelType;
    private Integer horsepower;
    private String city;
    private String imageUrl;
    private String sellerUsername;

    public CarPostDto() {
    }

    public CarPostDto(Long id, String brand, String model, Integer year, Integer price,
                      Integer mileage, FuelType fuelType, Integer horsepower,
                      String city, String imageUrl, String sellerUsername) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.horsepower = horsepower;
        this.city = city;
        this.imageUrl = imageUrl;
        this.sellerUsername = sellerUsername;
    }

    public static CarPostDto fromCarPost(CarPost carPost) {
        return new CarPostDto(
                carPost.getId(),
                carPost.getBrand().getName(),
                carPost.getModel().getName(),
                carPost.getYear(),
                carPost.getPrice(),
                carPost.getMileage(),
                carPost.getFuelType(),
                carPost.getHorsepower(),
                carPost.getCity(),
                carPost.getImageUrl(),
                carPost.getSeller().getUsername()
        );
    }

    public Long getId() {
        return id;
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

    public FuelType getFuelType() {
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

    public void setFuelType(FuelType fuelType) {
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