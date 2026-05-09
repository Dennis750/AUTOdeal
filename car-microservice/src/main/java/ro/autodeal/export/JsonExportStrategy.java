package ro.autodeal.export;

import org.springframework.stereotype.Component;
import ro.autodeal.model.CarPost;

import java.util.List;

@Component
public class JsonExportStrategy implements ExportStrategy {

    @Override
    public String export(List<CarPost> carPosts) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < carPosts.size(); i++) {
            CarPost post = carPosts.get(i);

            sb.append("  {\n");
            sb.append("    \"id\": ").append(post.getId()).append(",\n");
            sb.append("    \"brand\": \"").append(escape(post.getBrand().getName())).append("\",\n");
            sb.append("    \"model\": \"").append(escape(post.getModel().getName())).append("\",\n");
            sb.append("    \"year\": ").append(post.getYear()).append(",\n");
            sb.append("    \"price\": ").append(post.getPrice()).append(",\n");
            sb.append("    \"mileage\": ").append(post.getMileage()).append(",\n");
            sb.append("    \"fuelType\": \"").append(post.getFuelType()).append("\",\n");
            sb.append("    \"horsepower\": ").append(post.getHorsepower()).append(",\n");
            sb.append("    \"city\": \"").append(escape(post.getCity())).append("\",\n");
            sb.append("    \"seller\": \"").append(escape(post.getSeller().getUsername())).append("\"\n");
            sb.append("  }");

            if (i < carPosts.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");
        return sb.toString();
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }
}