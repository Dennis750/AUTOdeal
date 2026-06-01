package ro.autodeal.dto;

public class BrandOptionDto {

    private Long id;
    private String name;

    public BrandOptionDto() {
    }

    public BrandOptionDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}