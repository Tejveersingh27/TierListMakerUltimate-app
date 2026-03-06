package app.TierListMakerUltimate.models;

import java.util.List;

public class SystemTemplate {
    private String name;
    private String thumbnailPath;
    private List<SystemTemplateItem> items;

    public SystemTemplate(String name, String thumbnailPath, List<SystemTemplateItem> items) {
        this.name = name;
        this.thumbnailPath = thumbnailPath;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public List<SystemTemplateItem> getItems() {
        return items;
    }

}
