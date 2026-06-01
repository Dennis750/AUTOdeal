package ro.autodeal.controller;

import org.springframework.web.bind.annotation.*;
import ro.autodeal.dto.BrandOptionDto;
import ro.autodeal.dto.ModelOptionDto;
import ro.autodeal.service.BrandService;
import ro.autodeal.service.CarModelService;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "http://localhost:63342")
public class BrandRestController {

    private final BrandService brandService;
    private final CarModelService carModelService;

    public BrandRestController(BrandService brandService,
                               CarModelService carModelService) {
        this.brandService = brandService;
        this.carModelService = carModelService;
    }

    @GetMapping
    public List<BrandOptionDto> getAllBrands() {
        return brandService.getAllBrands()
                .stream()
                .map(brand -> new BrandOptionDto(brand.getId(), brand.getName()))
                .toList();
    }

    @GetMapping("/{brandId}/models")
    public List<ModelOptionDto> getModelsByBrand(@PathVariable Long brandId) {
        return carModelService.getModelsByBrandId(brandId)
                .stream()
                .map(model -> new ModelOptionDto(model.getId(), model.getName()))
                .toList();
    }
}