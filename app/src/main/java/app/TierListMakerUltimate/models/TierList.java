package app.TierListMakerUltimate.models;

import java.util.Objects;

public class TierList {
    private int id;
    private String name;
    private String thumbnailImagePath;

    // For new tiers
    public TierList(String name) {
        this.name = name;
    }

    public TierList(String name, String thumbnailImagePath) {
        this.name = name;
        this.thumbnailImagePath = thumbnailImagePath;
    }

    // For DB load
    public TierList(int id, String name, String thumbnailImagePath) {
        this.id = id;
        this.name = name;
    }

    public String getThumbnailImagePath() {
        return this.thumbnailImagePath;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
