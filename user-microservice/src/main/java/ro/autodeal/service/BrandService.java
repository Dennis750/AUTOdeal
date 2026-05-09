package ro.autodeal.service;

import org.springframework.stereotype.Service;
import ro.autodeal.model.Brand;
import ro.autodeal.repository.BrandRepository;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}