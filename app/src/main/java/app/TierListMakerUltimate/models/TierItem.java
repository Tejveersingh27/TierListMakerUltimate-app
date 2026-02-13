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
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
                return false;
        }

        TierItem item = (TierItem) obj;
        return id == item.id && tierId == item.tierId &&
                imagePath == item.imagePath &&
                description.equals(item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imagePath, description, tierId);
    }
}