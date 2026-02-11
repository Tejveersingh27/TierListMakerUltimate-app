package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.business.validation.TierValidator;


import java.util.List;


public class TierManager {
    private final TierPersistence tierStorage;


    public TierManager(TierPersistence tierStorage) {
        this.tierStorage = tierStorage;
    }

    public Tier createTier(int tierListId, String label, String color) {
        TierValidator.validateTier(label, color);
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
        TierValidator.validateLabel(newLabel);
        Tier tier = tierStorage.getTier(tierId);
        tier.setName(newLabel);
        tierStorage.updateTier(tier);
    }

    public void changeTierColor(int tierId, String colorHex) {
        TierValidator.validateColor(colorHex);
        Tier tier = tierStorage.getTier(tierId);
        tier.setColor(colorHex);
        tierStorage.updateTier(tier);
    }

    public List<Tier> getTiersForList(int tierListId) {
        return tierStorage.getTiersForList(tierListId);
    }
}