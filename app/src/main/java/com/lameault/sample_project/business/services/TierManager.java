package com.lameault.sample_project.business.services;

import com.lameault.sample_project.models.Tier;
import com.lameault.sample_project.persistence.TierPersistence;

import java.util.List;


public class TierManager {
    private TierPersistence tierStorage;

    public TierManager(TierPersistence tierStorage) {
        this.tierStorage = tierStorage;
    }

    public Tier createTier(int tierListId, String label, String color) {
        Tier newTier = new Tier(label, color);
        int newTierId = tierStorage.insertTier(tierListId, newTier);
        newTier.setId(newTierId);
        return newTier;
    }

    public void removeTier(int tierId) {
        tierStorage.deleteTier(tierId);
    }

    public Tier getTier(int tierId) {
        return tierStorage.getTier(tierId);
    }

    public void renameTier(int tierId, String newLabel) {
        Tier tier = tierStorage.getTier(tierId);
        tier.setName(newLabel);
        tierStorage.updateTier(tier);
    }

    public void changeTierColor(int tierId, String colorHex) {
        Tier tier = tierStorage.getTier(tierId);
        tier.setColor(colorHex);
        tierStorage.updateTier(tier);
    }

    public List<Tier> getTiersForList(int tierListId) {
        return tierStorage.getTiersForList(tierListId);
    }
}