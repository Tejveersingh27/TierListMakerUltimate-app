package app.TierListMakerUltimate.models;

import java.util.Objects;

public class TierItem {
    private int id;
    private int imagePath;
    private String description;
    private int tierId;

    // For new items
    public TierItem(int imagePath, String description, int tierId) {
        this.imagePath = imagePath;
        this.description = description;
        this.tierId = tierId;
    }

    // For DB load
    public TierItem(int id, int imagePath, String description, int tierId) {
        this.id = id;
        this.imagePath = imagePath;
        this.description = description;
        this.tierId = tierId;
    }

    public int getId() {
        return this.id;
    }

    public int getImagePath() {
        return this.imagePath;
    }

    public String getDescription() {
        return this.description;
    }

    public int getTierId() {
        return this.tierId;
    }

    public void setTierId(int tierId) {
        this.tierId = tierId;
    } // TODO: remove after updating ItemPlacementManager to not use this


}