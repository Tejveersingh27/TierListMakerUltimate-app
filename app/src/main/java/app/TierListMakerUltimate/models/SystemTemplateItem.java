package app.TierListMakerUltimate.models;

public class SystemTemplateItem {
    private String name;
    private String thumbnailPath;

    public SystemTemplateItem(String name, String thumbnailPath) {
        this.name = name;
        this.thumbnailPath = thumbnailPath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return thumbnailPath;
    }
}
