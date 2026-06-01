package ro.autodeal.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.autodeal.export.CsvCarPostExport;
import ro.autodeal.export.JsonCarPostExport;
import ro.autodeal.export.XmlCarPostExport;
import ro.autodeal.model.FuelType;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/cars/export")
@CrossOrigin(origins = "http://localhost:63342")
public class CarPostExportController {

    private final JsonCarPostExport jsonCarPostExport;
    private final CsvCarPostExport csvCarPostExport;
    private final XmlCarPostExport xmlCarPostExport;

    public CarPostExportController(JsonCarPostExport jsonCarPostExport,
                                   CsvCarPostExport csvCarPostExport,
                                   XmlCarPostExport xmlCarPostExport) {
        this.jsonCarPostExport = jsonCarPostExport;
        this.csvCarPostExport = csvCarPostExport;
        this.xmlCarPostExport = xmlCarPostExport;
    }

    @GetMapping("/json")
    public ResponseEntity<byte[]> exportJson(@RequestParam(required = false) Long brandId,
                                             @RequestParam(required = false) Long modelId,
                                             @RequestParam(required = false) Integer minPrice,
                                             @RequestParam(required = false) Integer maxPrice,
                                             @RequestParam(required = false) Integer year,
                                             @RequestParam(required = false) FuelType fuelType,
                                             @RequestParam(required = false) String sortBy) {

        String content = jsonCarPostExport.export(
                brandId,
                modelId,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carposts.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(content.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(@RequestParam(required = false) Long brandId,
                                            @RequestParam(required = false) Long modelId,
                                            @RequestParam(required = false) Integer minPrice,
                                            @RequestParam(required = false) Integer maxPrice,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) FuelType fuelType,
                                            @RequestParam(required = false) String sortBy) {

        String content = csvCarPostExport.export(
                brandId,
                modelId,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carposts.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(content.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/xml")
    public ResponseEntity<byte[]> exportXml(@RequestParam(required = false) Long brandId,
                                            @RequestParam(required = false) Long modelId,
                                            @RequestParam(required = false) Integer minPrice,
                                            @RequestParam(required = false) Integer maxPrice,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) FuelType fuelType,
                                            @RequestParam(required = false) String sortBy) {

        String content = xmlCarPostExport.export(
                brandId,
                modelId,
                minPrice,
                maxPrice,
                year,
                fuelType,
                sortBy
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carposts.xml")
                .contentType(MediaType.APPLICATION_XML)
                .body(content.getBytes(StandardCharsets.UTF_8));
    }
}