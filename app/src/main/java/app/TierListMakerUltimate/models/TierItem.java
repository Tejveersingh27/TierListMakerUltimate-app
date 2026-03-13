package app.TierListMakerUltimate.models;

import java.util.Objects;

public class TierItem {
    private int id;
    private String imagePath;
    private String name;
    private String description;
    private String explanation;
    private int tierId;

    // For new items
    public TierItem(String imagePath, String name, String description, String explanation, int tierId) {
        this.imagePath = imagePath;
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.tierId = tierId;
    }

    // For DB load
    public TierItem(int id, String imagePath, String name, String description, String explanation, int tierId) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.description = description;
        this.explanation = explanation;
        this.tierId = tierId;
    }

    public int getId() {
        return this.id;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public int getTierId() {
        return this.tierId;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    public void setDescription(String newDescription)
    {
        description = newDescription;
    }

    public void setExplanation(String newExplanation)
    {
        explanation = newExplanation;
    }
}