package ro.autodeal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.autodeal.model.Brand;
import ro.autodeal.model.CarModel;
import ro.autodeal.model.CarPost;
import ro.autodeal.model.FuelType;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.repository.BrandRepository;
import ro.autodeal.repository.CarModelRepository;
import ro.autodeal.repository.CarPostRepository;
import ro.autodeal.repository.UserRepository;

@Configuration
public class DataInitializer {

    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;
    private final CarPostRepository carPostRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(BrandRepository brandRepository,
                           CarModelRepository carModelRepository,
                           CarPostRepository carPostRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.carPostRepository = carPostRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {

            seedBrandsAndModels();
            seedUsersAndPosts();
        };
    }

    private void seedBrandsAndModels() {
        // BMW
        Brand bmw = createBrandIfMissing("BMW");
        saveModelIfMissing("1 Series", bmw);
        saveModelIfMissing("M135i", bmw);
        saveModelIfMissing("2 Series", bmw);
        saveModelIfMissing("M2", bmw);
        saveModelIfMissing("3 Series", bmw);
        saveModelIfMissing("M3", bmw);
        saveModelIfMissing("4 Series", bmw);
        saveModelIfMissing("M4", bmw);
        saveModelIfMissing("5 Series", bmw);
        saveModelIfMissing("M5", bmw);
        saveModelIfMissing("7 Series", bmw);
        saveModelIfMissing("M760i", bmw);
        saveModelIfMissing("8 Series", bmw);
        saveModelIfMissing("M8", bmw);
        saveModelIfMissing("X1", bmw);
        saveModelIfMissing("X3", bmw);
        saveModelIfMissing("X3 M", bmw);
        saveModelIfMissing("X5", bmw);
        saveModelIfMissing("X5 M", bmw);
        saveModelIfMissing("X6", bmw);
        saveModelIfMissing("X6 M", bmw);

        // Audi
        Brand audi = createBrandIfMissing("Audi");
        saveModelIfMissing("A3", audi);
        saveModelIfMissing("RS3", audi);
        saveModelIfMissing("A4", audi);
        saveModelIfMissing("RS4", audi);
        saveModelIfMissing("A5", audi);
        saveModelIfMissing("RS5", audi);
        saveModelIfMissing("A6", audi);
        saveModelIfMissing("RS6", audi);
        saveModelIfMissing("A7", audi);
        saveModelIfMissing("RS7", audi);
        saveModelIfMissing("A8", audi);
        saveModelIfMissing("S8", audi);
        saveModelIfMissing("Q3", audi);
        saveModelIfMissing("RSQ3", audi);
        saveModelIfMissing("Q5", audi);
        saveModelIfMissing("SQ5", audi);
        saveModelIfMissing("Q7", audi);
        saveModelIfMissing("SQ7", audi);
        saveModelIfMissing("Q8", audi);
        saveModelIfMissing("RSQ8", audi);

        // Mercedes
        Brand mercedes = createBrandIfMissing("Mercedes");
        saveModelIfMissing("A-Class", mercedes);
        saveModelIfMissing("A45 AMG", mercedes);
        saveModelIfMissing("C-Class", mercedes);
        saveModelIfMissing("C63 AMG", mercedes);
        saveModelIfMissing("CLA", mercedes);
        saveModelIfMissing("CLA45 AMG", mercedes);
        saveModelIfMissing("E-Class", mercedes);
        saveModelIfMissing("E63 AMG", mercedes);
        saveModelIfMissing("S-Class", mercedes);
        saveModelIfMissing("S63 AMG", mercedes);
        saveModelIfMissing("CLS", mercedes);
        saveModelIfMissing("CLS63 AMG", mercedes);
        saveModelIfMissing("GLC", mercedes);
        saveModelIfMissing("GLC63 AMG", mercedes);
        saveModelIfMissing("GLE", mercedes);
        saveModelIfMissing("GLE63 AMG", mercedes);
        saveModelIfMissing("G-Class", mercedes);
        saveModelIfMissing("G63 AMG", mercedes);

        // Porsche
        Brand porsche = createBrandIfMissing("Porsche");
        saveModelIfMissing("718 Cayman", porsche);
        saveModelIfMissing("718 Cayman GT4", porsche);
        saveModelIfMissing("718 Boxster", porsche);
        saveModelIfMissing("718 Spyder", porsche);
        saveModelIfMissing("911 Carrera", porsche);
        saveModelIfMissing("911 Carrera 4S", porsche);
        saveModelIfMissing("911 GT3", porsche);
        saveModelIfMissing("911 Turbo S", porsche);
        saveModelIfMissing("Panamera", porsche);
        saveModelIfMissing("Panamera GTS", porsche);
        saveModelIfMissing("Panamera Turbo S", porsche);
        saveModelIfMissing("Macan", porsche);
        saveModelIfMissing("Macan GTS", porsche);
        saveModelIfMissing("Cayenne", porsche);
        saveModelIfMissing("Cayenne GTS", porsche);
        saveModelIfMissing("Cayenne Turbo GT", porsche);

        // Lamborghini
        Brand lambo = createBrandIfMissing("Lamborghini");
        saveModelIfMissing("Huracan", lambo);
        saveModelIfMissing("Huracan Evo", lambo);
        saveModelIfMissing("Huracan STO", lambo);
        saveModelIfMissing("Huracan Performante", lambo);
        saveModelIfMissing("Aventador", lambo);
        saveModelIfMissing("Aventador S", lambo);
        saveModelIfMissing("Aventador SVJ", lambo);
        saveModelIfMissing("Urus", lambo);
        saveModelIfMissing("Urus S", lambo);
        saveModelIfMissing("Urus Performante", lambo);
        saveModelIfMissing("Revuelto", lambo);

        // Ferrari
        Brand ferrari = createBrandIfMissing("Ferrari");
        saveModelIfMissing("488 GTB", ferrari);
        saveModelIfMissing("488 Pista", ferrari);
        saveModelIfMissing("F8 Tributo", ferrari);
        saveModelIfMissing("F8 Spider", ferrari);
        saveModelIfMissing("296 GTB", ferrari);
        saveModelIfMissing("296 GTS", ferrari);
        saveModelIfMissing("Roma", ferrari);
        saveModelIfMissing("Roma Spider", ferrari);
        saveModelIfMissing("Portofino", ferrari);
        saveModelIfMissing("812 Superfast", ferrari);
        saveModelIfMissing("812 Competizione", ferrari);
        saveModelIfMissing("SF90 Stradale", ferrari);
        saveModelIfMissing("SF90 Spider", ferrari);
        saveModelIfMissing("Purosangue", ferrari);

        // Dacia
        Brand dacia = createBrandIfMissing("Dacia");
        saveModelIfMissing("Logan", dacia);
        saveModelIfMissing("Sandero", dacia);
        saveModelIfMissing("Sandero Stepway", dacia);
        saveModelIfMissing("Duster", dacia);
        saveModelIfMissing("Jogger", dacia);
        saveModelIfMissing("Spring", dacia);
        saveModelIfMissing("Bigster", dacia);

        // existing other brands from your original initializer
        Brand vw = createBrandIfMissing("Volkswagen");
        saveModelIfMissing("Golf", vw);
        saveModelIfMissing("Golf GTI", vw);
        saveModelIfMissing("Golf R", vw);
        saveModelIfMissing("Passat", vw);
        saveModelIfMissing("Arteon", vw);
        saveModelIfMissing("Tiguan", vw);
        saveModelIfMissing("Touareg", vw);

        Brand toyota = createBrandIfMissing("Toyota");
        saveModelIfMissing("Yaris", toyota);
        saveModelIfMissing("GR Yaris", toyota);
        saveModelIfMissing("Corolla", toyota);
        saveModelIfMissing("GR Corolla", toyota);
        saveModelIfMissing("Camry", toyota);
        saveModelIfMissing("Supra", toyota);
        saveModelIfMissing("RAV4", toyota);
        saveModelIfMissing("Land Cruiser", toyota);

        Brand lexus = createBrandIfMissing("Lexus");
        saveModelIfMissing("IS", lexus);
        saveModelIfMissing("IS F", lexus);
        saveModelIfMissing("RC", lexus);
        saveModelIfMissing("RC F", lexus);
        saveModelIfMissing("GS", lexus);
        saveModelIfMissing("LS", lexus);
        saveModelIfMissing("NX", lexus);
        saveModelIfMissing("RX", lexus);

        Brand alfa = createBrandIfMissing("Alfa Romeo");
        saveModelIfMissing("Giulia", alfa);
        saveModelIfMissing("Giulia Quadrifoglio", alfa);
        saveModelIfMissing("Stelvio", alfa);
        saveModelIfMissing("Stelvio Quadrifoglio", alfa);
        saveModelIfMissing("Tonale", alfa);

        Brand jaguar = createBrandIfMissing("Jaguar");
        saveModelIfMissing("XE", jaguar);
        saveModelIfMissing("XFR", jaguar);
        saveModelIfMissing("XF", jaguar);
        saveModelIfMissing("F-Type", jaguar);
        saveModelIfMissing("F-Pace", jaguar);
        saveModelIfMissing("F-Pace SVR", jaguar);

        Brand landRover = createBrandIfMissing("Land Rover");
        saveModelIfMissing("Range Rover Evoque", landRover);
        saveModelIfMissing("Range Rover Sport", landRover);
        saveModelIfMissing("Range Rover Sport SVR", landRover);
        saveModelIfMissing("Defender", landRover);
        saveModelIfMissing("Discovery", landRover);

        Brand maserati = createBrandIfMissing("Maserati");
        saveModelIfMissing("Ghibli", maserati);
        saveModelIfMissing("Ghibli Trofeo", maserati);
        saveModelIfMissing("Levante", maserati);
        saveModelIfMissing("Levante Trofeo", maserati);
        saveModelIfMissing("Quattroporte", maserati);
        saveModelIfMissing("MC20", maserati);

        Brand nissan = createBrandIfMissing("Nissan");
        saveModelIfMissing("370Z", nissan);
        saveModelIfMissing("GT-R", nissan);
        saveModelIfMissing("Qashqai", nissan);
        saveModelIfMissing("X-Trail", nissan);

        Brand honda = createBrandIfMissing("Honda");
        saveModelIfMissing("Civic", honda);
        saveModelIfMissing("Civic Type R", honda);
        saveModelIfMissing("Accord", honda);
        saveModelIfMissing("CR-V", honda);

        Brand hyundai = createBrandIfMissing("Hyundai");
        saveModelIfMissing("i20", hyundai);
        saveModelIfMissing("i20 N", hyundai);
        saveModelIfMissing("i30", hyundai);
        saveModelIfMissing("i30 N", hyundai);
        saveModelIfMissing("Tucson", hyundai);
        saveModelIfMissing("Santa Fe", hyundai);

        Brand kia = createBrandIfMissing("Kia");
        saveModelIfMissing("Ceed", kia);
        saveModelIfMissing("Proceed GT", kia);
        saveModelIfMissing("Stinger", kia);
        saveModelIfMissing("Sportage", kia);
        saveModelIfMissing("Sorento", kia);

        Brand skoda = createBrandIfMissing("Skoda");
        saveModelIfMissing("Octavia", skoda);
        saveModelIfMissing("Octavia RS", skoda);
        saveModelIfMissing("Superb", skoda);
        saveModelIfMissing("Kodiaq", skoda);
        saveModelIfMissing("Kodiaq RS", skoda);

        Brand cupra = createBrandIfMissing("Cupra");
        saveModelIfMissing("Leon", cupra);
        saveModelIfMissing("Leon VZ", cupra);
        saveModelIfMissing("Formentor", cupra);
        saveModelIfMissing("Formentor VZ", cupra);

        Brand peugeot = createBrandIfMissing("Peugeot");
        saveModelIfMissing("208", peugeot);
        saveModelIfMissing("208 GTi", peugeot);
        saveModelIfMissing("308", peugeot);
        saveModelIfMissing("308 GT", peugeot);
        saveModelIfMissing("3008", peugeot);
        saveModelIfMissing("508", peugeot);

        Brand renault = createBrandIfMissing("Renault");
        saveModelIfMissing("Clio", renault);
        saveModelIfMissing("Clio RS", renault);
        saveModelIfMissing("Megane", renault);
        saveModelIfMissing("Megane RS", renault);
        saveModelIfMissing("Kadjar", renault);
        saveModelIfMissing("Austral", renault);

        Brand volvo = createBrandIfMissing("Volvo");
        saveModelIfMissing("S60", volvo);
        saveModelIfMissing("S60 Polestar", volvo);
        saveModelIfMissing("S90", volvo);
        saveModelIfMissing("XC60", volvo);
        saveModelIfMissing("XC90", volvo);

        Brand ford = createBrandIfMissing("Ford");
        saveModelIfMissing("Focus", ford);
        saveModelIfMissing("Focus ST", ford);
        saveModelIfMissing("Focus RS", ford);
        saveModelIfMissing("Mondeo", ford);
        saveModelIfMissing("Mustang", ford);
        saveModelIfMissing("Kuga", ford);
        saveModelIfMissing("Ranger Raptor", ford);
    }

    private void seedUsersAndPosts() {
        User seller = userRepository.findByUsername("seller1").orElseGet(() -> {
            User user = new User();
            user.setUsername("seller1");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setName("Autoklass Cluj");
            user.setPhoneNumber("0744123456");
            user.setRole(Role.SELLER);
            return userRepository.save(user);
        });

        userRepository.findByUsername("admin").orElseGet(() -> {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setName("AUTOdeal Admin");
            user.setPhoneNumber("0700000000");
            user.setRole(Role.ADMIN);
            return userRepository.save(user);
        });

        if (carPostRepository.count() == 0) {
            CarModel m3 = carModelRepository.findByName("M3").orElse(null);
            CarModel a4 = carModelRepository.findByName("A4").orElse(null);
            CarModel c63 = carModelRepository.findByName("C63 AMG").orElse(null);

            if (m3 != null) {
                createPost(m3, seller, 2018, 35000, 90000, FuelType.PETROL, 425, "Cluj-Napoca");
            }
            if (a4 != null) {
                createPost(a4, seller, 2020, 27000, 60000, FuelType.DIESEL, 190, "Bucharest");
            }
            if (c63 != null) {
                createPost(c63, seller, 2017, 55000, 80000, FuelType.PETROL, 510, "Timisoara");
            }
        }
    }

    private Brand createBrandIfMissing(String brandName) {
        return brandRepository.findAll().stream()
                .filter(b -> b.getName().equalsIgnoreCase(brandName))
                .findFirst()
                .orElseGet(() -> {
                    Brand brand = new Brand();
                    brand.setName(brandName);
                    return brandRepository.save(brand);
                });
    }

    private void saveModelIfMissing(String modelName, Brand brand) {
        boolean exists = carModelRepository.findAll().stream()
                .anyMatch(m -> m.getName().equalsIgnoreCase(modelName)
                        && m.getBrand().getId().equals(brand.getId()));

        if (!exists) {
            CarModel model = new CarModel();
            model.setName(modelName);
            model.setBrand(brand);
            carModelRepository.save(model);
        }
    }

    private void createPost(CarModel model, User seller,
                            int year, int price, int mileage,
                            FuelType fuelType, int horsepower, String city) {

        CarPost post = new CarPost();
        post.setBrand(model.getBrand());
        post.setModel(model);
        post.setYear(year);
        post.setPrice(price);
        post.setMileage(mileage);
        post.setFuelType(fuelType);
        post.setHorsepower(horsepower);
        post.setCity(city);
        post.setVisible(true);
        post.setSeller(seller);

        carPostRepository.save(post);
    }
}