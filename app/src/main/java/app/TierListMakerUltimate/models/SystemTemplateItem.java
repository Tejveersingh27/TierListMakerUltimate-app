package app.TierListMakerUltimate.models;

public class SystemTemplateItem {
    private String name;
    private String imagePath;

    public SystemTemplateItem(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
