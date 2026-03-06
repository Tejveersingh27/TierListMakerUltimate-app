package app.TierListMakerUltimate.models;

import java.util.Objects;

public class TierList {
    private int id;
    private String name;
    private String thumbnailPath;

    // For new tiers
    public TierList(String name) {
        this.name = name;
    }

    public TierList(String name, String thumbnailPath) {
        this.name = name;
        this.thumbnailPath = thumbnailPath;
    }

    // For DB load
    public TierList(int id, String name, String thumbnailPath) {
        this.id = id;
        this.name = name;
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnailImagePath() {
        return this.thumbnailPath;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
