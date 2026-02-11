package app.TierListMakerUltimate.models;


public class TierItem {
    private int id;
    private String imagePath;
    private String description;
    private int tierId;

    // For new items
    public TierItem(String imagePath, String description, int tierId) {
        this.imagePath = imagePath;
        this.description = description;
        this.tierId = tierId;
    }

    // For DB load
    public TierItem(int id, String imagePath, String description, int tierId) {
        this.id = id;
        this.imagePath = imagePath;
        this.description = description;
        this.tierId = tierId;
    }

    public int getId() {
        return this.id;
    }

    public String getImagePath() {
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
}
