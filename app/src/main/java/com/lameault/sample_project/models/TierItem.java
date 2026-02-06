package com.lameault.sample_project.models;


public class TierItem {
    private int id;
    private String imagePath;
    private String description;
    private int tierId;

    // For new items
    public TierItem(String imagePath, String description, int tierId);  
    // For DB load
    public TierItem(int id, String imagePath, String description, int tierId);  

    public int getId(){
        return this.id;
    }
    public String getImagePath(){
        return this.imagePath;
    }
    public String getDescription(){
        return this.description;
    }
    public int getTierId(){
        return this.tierId;
    }

    public void setTierId(int tierId){
        this.tierId = tierId;
    }
    public void setId(int id){
        this.id = id;
    }
}
