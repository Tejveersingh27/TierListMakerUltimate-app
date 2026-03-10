package app.TierListMakerUltimate.models;

import java.util.Objects;

public class TierList {
    private int id;
    private String name;
    private String thumbnailPath;
    private boolean isTemplate;


    // For new tiers
    public TierList(String name, String thumbnailPath, boolean isTemplate) {
        this.name = name;
        this.thumbnailPath = thumbnailPath;
        this.isTemplate = isTemplate;
    }

    // For DB load
    public TierList(int id, String name, String thumbnailPath, boolean isTemplate) {
        this.id = id;
        this.name = name;
        this.thumbnailPath = thumbnailPath;
        this.isTemplate = isTemplate;
    }

    public String getThumbnailPath() {
        return this.thumbnailPath;
    }

    public boolean isTemplate() {
        return this.isTemplate;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
