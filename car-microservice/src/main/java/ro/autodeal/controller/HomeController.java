package ro.autodeal.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.service.BrandService;
import ro.autodeal.service.CarPostQueryService;

@Controller
public class HomeController {

    private final CarPostQueryService carPostQueryService;
    private final BrandService brandService;

    public HomeController(CarPostQueryService carPostQueryService,
                          BrandService brandService) {
        this.carPostQueryService = carPostQueryService;
        this.brandService = brandService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/cars")
    public String cars(@RequestParam(required = false) Long brandId,
                       @RequestParam(required = false) Integer minPrice,
                       @RequestParam(required = false) Integer maxPrice,
                       @RequestParam(required = false) Integer year,
                       @RequestParam(required = false) FuelType fuelType,
                       @RequestParam(required = false) String sortBy,
                       @RequestParam(defaultValue = "0") int page,
                       Authentication authentication,
                       Model model) {

        int pageSize = 9;

        Page<CarPost> pageResult = carPostQueryService.getFilteredPosts(
                brandId, null, minPrice, maxPrice, year, fuelType, sortBy, page, pageSize
        );

        model.addAttribute("carPosts", pageResult.getContent());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("fuelTypes", FuelType.values());

        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedFuelType", fuelType);
        model.addAttribute("selectedSortBy", sortBy);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());
        model.addAttribute("hasNext", pageResult.hasNext());

        String currentUsername = authentication != null ? authentication.getName() : null;
        model.addAttribute("currentUsername", currentUsername);

        model.addAttribute("carPostService", carPostQueryService);

        model.addAttribute("pageTitle", "Available Cars");
        model.addAttribute("isMyListingsPage", false);

        return "cars";
    }

    @GetMapping("/cars/my-posts")
    public String myPosts(@RequestParam(required = false) String sortBy,
                          @RequestParam(defaultValue = "0") int page,
                          Authentication authentication,
                          Model model) {

        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        int pageSize = 9;
        String currentUsername = authentication.getName();

        Page<CarPost> pageResult = carPostQueryService.getMyPosts(
                currentUsername, sortBy, page, pageSize
        );

        model.addAttribute("carPosts", pageResult.getContent());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("fuelTypes", FuelType.values());

        model.addAttribute("selectedBrandId", null);
        model.addAttribute("selectedMinPrice", null);
        model.addAttribute("selectedMaxPrice", null);
        model.addAttribute("selectedYear", null);
        model.addAttribute("selectedFuelType", null);
        model.addAttribute("selectedSortBy", sortBy);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());
        model.addAttribute("hasNext", pageResult.hasNext());

        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("carPostService", carPostQueryService);
        model.addAttribute("pageTitle", "My Listings");
        model.addAttribute("isMyListingsPage", true);

        return "cars";
    }
}