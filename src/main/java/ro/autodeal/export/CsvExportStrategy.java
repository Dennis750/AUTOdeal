package ro.autodeal.export;

import org.springframework.stereotype.Component;
import ro.autodeal.model.CarPost;

import java.util.List;

@Component
public class CsvExportStrategy implements ExportStrategy {

    @Override
    public String export(List<CarPost> carPosts) {
        StringBuilder sb = new StringBuilder();

        sb.append("id,brand,model,year,price,mileage,fuelType,horsepower,city,seller\n");

        for (CarPost post : carPosts) {
            sb.append(post.getId()).append(",");
            sb.append(escape(post.getBrand().getName())).append(",");
            sb.append(escape(post.getModel().getName())).append(",");
            sb.append(post.getYear()).append(",");
            sb.append(post.getPrice()).append(",");
            sb.append(post.getMileage()).append(",");
            sb.append(post.getFuelType()).append(",");
            sb.append(post.getHorsepower()).append(",");
            sb.append(escape(post.getCity())).append(",");
            sb.append(escape(post.getSeller().getUsername())).append("\n");
        }

        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}