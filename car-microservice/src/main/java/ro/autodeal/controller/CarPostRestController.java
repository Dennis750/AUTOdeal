package ro.autodeal.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ro.autodeal.dto.CarPostDto;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.service.CarPostQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarPostRestController {

    private final CarPostQueryService carPostQueryService;

    public CarPostRestController(CarPostQueryService carPostQueryService) {
        this.carPostQueryService = carPostQueryService;
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
}