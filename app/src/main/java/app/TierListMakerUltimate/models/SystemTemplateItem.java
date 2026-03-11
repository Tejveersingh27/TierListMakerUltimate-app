package app.TierListMakerUltimate.models;

public class SystemTemplateItem {
    private String name;

    private String description;

    private String thumbnailPath;

    public SystemTemplateItem(String name, String description, String thumbnailPath) {
        this.name = name;
        this.description = "";
        this.thumbnailPath = thumbnailPath;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public String getImagePath() {
        return thumbnailPath;
    }
}
