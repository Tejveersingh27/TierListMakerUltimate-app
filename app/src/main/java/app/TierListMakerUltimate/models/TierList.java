package app.TierListMakerUltimate.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TierList tierList = (TierList) obj;
        return id == tierList.id &&
                name.equals(tierList.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
