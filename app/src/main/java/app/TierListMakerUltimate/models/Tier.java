package app.TierListMakerUltimate.models;

public class Tier {
    private int id;
    private int tierListId;
    private String name;
    private String colorHex;
    private boolean isUnranked;

    private int ordinalPosition;  // Ordinal, not relative.

    // For new tiers
    public Tier(int tierListId, String name, String colorHex, boolean isUnranked, int ordinalPosition) {
        this.tierListId = tierListId;
        this.name = name;
        this.colorHex = colorHex;
        this.isUnranked = isUnranked;
        this.ordinalPosition = ordinalPosition;
    }

    // For DB load
    public Tier(int id, int tierListId, String name, String colorHex, boolean isUnranked, int ordinalPosition) {
        this.id = id;
        this.tierListId = tierListId;
        this.name = name;
        this.colorHex = colorHex;
        this.isUnranked = isUnranked;
        this.ordinalPosition = ordinalPosition;

    }

    public int getId() {
        return id;
    }

    public int getTierListId() {
        return tierListId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return colorHex;
    }

    public boolean isUnranked() {
        return isUnranked;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }
}
