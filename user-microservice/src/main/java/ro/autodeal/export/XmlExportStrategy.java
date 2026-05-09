package ro.autodeal.export;

import org.springframework.stereotype.Component;
import ro.autodeal.model.CarPost;

import java.util.List;

@Component
public class XmlExportStrategy implements ExportStrategy {

    @Override
    public String export(List<CarPost> carPosts) {
        StringBuilder sb = new StringBuilder();

        sb.append("<carPosts>\n");

        for (CarPost post : carPosts) {
            sb.append("  <carPost>\n");
            sb.append("    <id>").append(post.getId()).append("</id>\n");
            sb.append("    <brand>").append(escape(post.getBrand().getName())).append("</brand>\n");
            sb.append("    <model>").append(escape(post.getModel().getName())).append("</model>\n");
            sb.append("    <year>").append(post.getYear()).append("</year>\n");
            sb.append("    <price>").append(post.getPrice()).append("</price>\n");
            sb.append("    <mileage>").append(post.getMileage()).append("</mileage>\n");
            sb.append("    <fuelType>").append(post.getFuelType()).append("</fuelType>\n");
            sb.append("    <horsepower>").append(post.getHorsepower()).append("</horsepower>\n");
            sb.append("    <city>").append(escape(post.getCity())).append("</city>\n");
            sb.append("    <seller>").append(escape(post.getSeller().getUsername())).append("</seller>\n");
            sb.append("  </carPost>\n");
        }

        sb.append("</carPosts>");
        return sb.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}