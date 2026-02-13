package app.TierListMakerUltimate.models;

public class TierList {
    private int id;
    private String name;

    // For new tiers
    public TierList(String name) {
        this.name = name;
    }

    // For DB load
    public TierList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
