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

    public void setId(int id) {
        this.id = id;
    }

    public void setTierListId(int tierListId) {
        this.tierListId = tierListId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String colorHex) {
        this.colorHex = colorHex;
    }

    public void setUnranked(boolean unranked) {
        isUnranked = unranked;
    }


}
