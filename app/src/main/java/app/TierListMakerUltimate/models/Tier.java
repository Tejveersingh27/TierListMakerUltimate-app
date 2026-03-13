package app.TierListMakerUltimate.models;

import java.util.Objects;

public class Tier {
    private int id;
    private int tierListId;
    private String name;
    private String colorHex;
    private boolean isUnranked;

    private int tierPosition;  // Position (index) relative to the associated tierList (for ordering)

    // For new tiers
    public Tier(int tierListId, String name, String colorHex, boolean isUnranked, int tierPosition) {
        this.tierListId = tierListId;
        this.name = name;
        this.colorHex = colorHex;
        this.isUnranked = isUnranked;
    }

    // For DB load
    public Tier(int id, int tierListId, String name, String colorHex, boolean isUnranked, int tierPosition) {
        this.id = id;
        this.tierListId = tierListId;
        this.name = name;
        this.colorHex = colorHex;
        this.isUnranked = isUnranked;
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

    public int getPosition() {
        return tierPosition;
    }

}
