package ro.autodeal.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.autodeal.export.CsvExportStrategy;
import ro.autodeal.export.JsonExportStrategy;
import ro.autodeal.export.XmlExportStrategy;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.service.CarPostQueryService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/cars/export")
public class CarPostExportController {

    private final CarPostQueryService carPostQueryService;
    private final JsonExportStrategy jsonExportStrategy;
    private final CsvExportStrategy csvExportStrategy;
    private final XmlExportStrategy xmlExportStrategy;

    public CarPostExportController(CarPostQueryService carPostQueryService,
                                   JsonExportStrategy jsonExportStrategy,
                                   CsvExportStrategy csvExportStrategy,
                                   XmlExportStrategy xmlExportStrategy) {
        this.carPostQueryService = carPostQueryService;
        this.jsonExportStrategy = jsonExportStrategy;
        this.csvExportStrategy = csvExportStrategy;
        this.xmlExportStrategy = xmlExportStrategy;
    }

    @GetMapping("/json")
    public ResponseEntity<byte[]> exportJson(@RequestParam(required = false) Long brandId,
                                             @RequestParam(required = false) Integer minPrice,
                                             @RequestParam(required = false) Integer maxPrice,
                                             @RequestParam(required = false) Integer year,
                                             @RequestParam(required = false) FuelType fuelType,
                                             @RequestParam(required = false) String sortBy) {

        List<CarPost> posts = carPostQueryService.getFilteredPostsForExport(
                brandId, minPrice, maxPrice, year, fuelType, sortBy
        );

        String content = jsonExportStrategy.export(posts);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carposts.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(content.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(@RequestParam(required = false) Long brandId,
                                            @RequestParam(required = false) Integer minPrice,
                                            @RequestParam(required = false) Integer maxPrice,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) FuelType fuelType,
                                            @RequestParam(required = false) String sortBy) {

        List<CarPost> posts = carPostQueryService.getFilteredPostsForExport(
                brandId, minPrice, maxPrice, year, fuelType, sortBy
        );

        String content = csvExportStrategy.export(posts);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carposts.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(content.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/xml")
    public ResponseEntity<byte[]> exportXml(@RequestParam(required = false) Long brandId,
                                            @RequestParam(required = false) Integer minPrice,
                                            @RequestParam(required = false) Integer maxPrice,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) FuelType fuelType,
                                            @RequestParam(required = false) String sortBy) {

        List<CarPost> posts = carPostQueryService.getFilteredPostsForExport(
                brandId, minPrice, maxPrice, year, fuelType, sortBy
        );

        String content = xmlExportStrategy.export(posts);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carposts.xml")
                .contentType(MediaType.APPLICATION_XML)
                .body(content.getBytes(StandardCharsets.UTF_8));
    }
}