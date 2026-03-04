package app.TierListMakerUltimate.models;

import java.util.List;

public class SystemTemplate {
    private String name;
    private List<SystemTemplateItem> items;

    public SystemTemplate(String name, List<SystemTemplateItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<SystemTemplateItem> getItems() {
        return items;
    }

}
