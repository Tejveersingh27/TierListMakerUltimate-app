package app.TierListMakerUltimate.models;

import java.util.Objects;

public class Tier {
    private int id;
    private int tierListId;
    private String name;
    private String colorHex;
    private boolean isUnranked;

    // For new tiers
    public Tier(int tierListId, String name, String colorHex, boolean isUnranked) {
        this.tierListId = tierListId;
        this.name = name;
        this.colorHex = colorHex;
        this.isUnranked = isUnranked;
    }

    // For DB load
    public Tier(int id, int tierListId, String name, String colorHex, boolean isUnranked) {
        this.id = id;
        this.tierListId = tierListId;
        this.name = name;
        this.colorHex = colorHex;
        this.isUnranked = isUnranked;
    }

    public int getId() { return id; }
    public int getTierListId() { return tierListId; }
    public String getName() { return name; }
    public String getColor() { return colorHex; }
    public boolean isUnranked() { return isUnranked; }

    public void setId(int id) { this.id = id; }
    public void setTierListId(int tierListId) { this.tierListId = tierListId; }
    public void setName(String name) { this.name = name; }
    public void setColor(String colorHex) { this.colorHex = colorHex; }
    public void setUnranked(boolean unranked) { isUnranked = unranked; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tier tier = (Tier) o;
        return id == tier.id && tierListId == tier.tierListId && isUnranked == tier.isUnranked && Objects.equals(name, tier.name) && Objects.equals(colorHex, tier.colorHex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tierListId, name, colorHex, isUnranked);
    }
}
