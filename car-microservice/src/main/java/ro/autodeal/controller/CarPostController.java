package ro.autodeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.autodeal.dto.ModelOptionDto;
import ro.autodeal.model.CarModel;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.service.BrandService;
import ro.autodeal.service.CarModelService;
import ro.autodeal.service.CarPostQueryService;
import ro.autodeal.service.CarPostService;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarPostController {

    private final CarPostService carPostService;
    private final CarPostQueryService carPostQueryService;
    private final BrandService brandService;
    private final CarModelService carModelService;

    public CarPostController(CarPostService carPostService,
                             CarPostQueryService carPostQueryService,
                             BrandService brandService,
                             CarModelService carModelService) {
        this.carPostService = carPostService;
        this.carPostQueryService = carPostQueryService;
        this.brandService = brandService;
        this.carModelService = carModelService;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("formTitle", "Create Car Post");
        model.addAttribute("isEdit", false);
        return "car-form";
    }

    @PostMapping("/create")
    public String createCarPost(@RequestParam Long brandId,
                                @RequestParam Long modelId,
                                @RequestParam Integer year,
                                @RequestParam Integer price,
                                @RequestParam Integer mileage,
                                @RequestParam FuelType fuelType,
                                @RequestParam Integer horsepower,
                                @RequestParam String city,
                                @RequestParam(required = false) String imageUrl) {

        carPostService.createCarPost(brandId, modelId, year, price, mileage, fuelType, horsepower, city, imageUrl);
        return "redirect:/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CarPost carPost = carPostQueryService.getById(id);
        List<CarModel> models = carModelService.getModelsByBrandId(carPost.getBrand().getId());

        model.addAttribute("carPost", carPost);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("models", models);
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("formTitle", "Edit Car Post");
        model.addAttribute("isEdit", true);
        model.addAttribute("selectedBrandId", carPost.getBrand().getId());
        model.addAttribute("selectedModelId", carPost.getModel().getId());

        return "car-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCarPost(@PathVariable Long id,
                                @RequestParam Long brandId,
                                @RequestParam Long modelId,
                                @RequestParam Integer year,
                                @RequestParam Integer price,
                                @RequestParam Integer mileage,
                                @RequestParam FuelType fuelType,
                                @RequestParam Integer horsepower,
                                @RequestParam String city,
                                @RequestParam(required = false) String imageUrl) {

        carPostService.updateCarPost(id, brandId, modelId, year, price, mileage, fuelType, horsepower, city, imageUrl);
        return "redirect:/cars";
    }

    @PostMapping("/delete/{id}")
    public String deleteCarPost(@PathVariable Long id) {
        carPostService.deleteById(id);
        return "redirect:/cars";
    }

    @GetMapping("/models")
    @ResponseBody
    public List<ModelOptionDto> getModelsByBrand(@RequestParam Long brandId) {
        return carModelService.getModelsByBrandId(brandId)
                .stream()
                .map(model -> new ModelOptionDto(model.getId(), model.getName()))
                .toList();
    }
}