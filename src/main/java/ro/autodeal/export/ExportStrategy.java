package ro.autodeal.export;

import ro.autodeal.model.CarPost;

import java.util.List;

public interface ExportStrategy {
    String export(List<CarPost> carPosts);
}