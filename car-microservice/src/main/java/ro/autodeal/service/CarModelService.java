package ro.autodeal.service;

import org.springframework.stereotype.Service;
import ro.autodeal.model.CarModel;
import ro.autodeal.repository.CarModelRepository;

import java.util.List;

@Service
public class CarModelService {

    private final CarModelRepository carModelRepository;

    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public List<CarModel> getAllModels() {
        return carModelRepository.findAll();
    }

    public List<CarModel> getModelsByBrandId(Long brandId) {
        return carModelRepository.findByBrandId(brandId);
    }
}