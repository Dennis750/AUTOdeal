package ro.autodeal.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ro.autodeal.dto.CarPostDto;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.service.CarPostCommandService;
import ro.autodeal.service.CarPostQueryService;
import ro.autodeal.service.UserClientService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:63342")
public class CarPostRestController {

    private final CarPostQueryService carPostQueryService;
    private final CarPostCommandService carPostCommandService;
    private final UserClientService userClientService;

    public CarPostRestController(CarPostQueryService carPostQueryService,
                                 CarPostCommandService carPostCommandService,
                                 UserClientService userClientService) {
        this.carPostQueryService = carPostQueryService;
        this.carPostCommandService = carPostCommandService;
        this.userClientService = userClientService;
    }

    @GetMapping
    public List<CarPostDto> getCars(@RequestParam(required = false) Long brandId,
                                    @RequestParam(required = false) Integer minPrice,
                                    @RequestParam(required = false) Integer maxPrice,
                                    @RequestParam(required = false) Integer year,
                                    @RequestParam(required = false) FuelType fuelType,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "9") int size) {

        Page<CarPost> pageResult = carPostQueryService.getFilteredPosts(
                brandId,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy,
                page,
                size
        );

        return pageResult.getContent()
                .stream()
                .map(CarPostDto::fromCarPost)
                .toList();
    }

    @GetMapping("/{id}")
    public CarPostDto getCarById(@PathVariable Long id) {
        CarPost carPost = carPostQueryService.getById(id);
        return CarPostDto.fromCarPost(carPost);
    }

    @PostMapping
    public CarPostDto createCar(@RequestBody CreateCarRequest request) {
        CarPost savedPost = carPostCommandService.createCarPostFromApi(
                request.getSellerUsername(),
                request.getBrandId(),
                request.getModelId(),
                request.getYear(),
                request.getPrice(),
                request.getMileage(),
                request.getFuelType(),
                request.getHorsepower(),
                request.getCity(),
                request.getImageUrl()
        );

        return CarPostDto.fromCarPost(savedPost);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id,
                          @RequestParam String sellerUsername) {
        carPostCommandService.deleteByIdFromApi(id, sellerUsername);
    }

    @PutMapping("/{id}")
    public CarPostDto updateCar(@PathVariable Long id,
                                @RequestBody CreateCarRequest request) {
        CarPost updatedPost = carPostCommandService.updateCarPostFromApi(
                id,
                request.getSellerUsername(),
                request.getBrandId(),
                request.getModelId(),
                request.getYear(),
                request.getPrice(),
                request.getMileage(),
                request.getFuelType(),
                request.getHorsepower(),
                request.getCity(),
                request.getImageUrl()
        );

        return CarPostDto.fromCarPost(updatedPost);
    }

    @GetMapping("/check-user-role/{username}")
    public String checkUserRoleThroughUserMicroservice(@PathVariable String username) {
        return userClientService.getUserRole(username);
    }

    public static class CreateCarRequest {
        private String sellerUsername;
        private Long brandId;
        private Long modelId;
        private Integer year;
        private Integer price;
        private Integer mileage;
        private FuelType fuelType;
        private Integer horsepower;
        private String city;
        private String imageUrl;

        public String getSellerUsername() {
            return sellerUsername;
        }

        public void setSellerUsername(String sellerUsername) {
            this.sellerUsername = sellerUsername;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public Long getModelId() {
            return modelId;
        }

        public void setModelId(Long modelId) {
            this.modelId = modelId;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getMileage() {
            return mileage;
        }

        public void setMileage(Integer mileage) {
            this.mileage = mileage;
        }

        public FuelType getFuelType() {
            return fuelType;
        }

        public void setFuelType(FuelType fuelType) {
            this.fuelType = fuelType;
        }

        public Integer getHorsepower() {
            return horsepower;
        }

        public void setHorsepower(Integer horsepower) {
            this.horsepower = horsepower;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}