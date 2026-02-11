package app.TierListMakerUltimate.models;

public class Tier {
    private int id;
    private String name;
    private String colorHex; // Format: "#RRGGBB" 

    // For new tiers
    public Tier(String name, String colorHex) {
        this.name = name;
        this.colorHex = colorHex;
    }

    // For DB load
    public Tier(int id, String name, String colorHex) {
        this.id = id;
        this.name = name;
        this.colorHex = colorHex;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.colorHex;
    }

    public void setColor(String colorHex) {
        this.colorHex = colorHex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
