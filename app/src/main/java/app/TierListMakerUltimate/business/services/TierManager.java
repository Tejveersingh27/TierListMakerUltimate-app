package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.business.validation.TierValidator;

import java.util.List;

public class TierManager {
    private final TierPersistence tierStorage;
    private final TierValidator validator;

    public TierManager(TierPersistence tierStorage, TierValidator validator) {
        if (tierStorage == null || validator == null) {
            throw new IllegalArgumentException("TierPersistence and TierValidator cannot be null");
        }
        this.tierStorage = tierStorage;
        this.validator = validator;
    }

    public int createTier(int tierListId, String label, String color) {
        return internalCreateTier(tierListId, label, color, false);
    }

    int createUnrankedTier(int tierListId, String label, String color) {
        return internalCreateTier(tierListId, label, color, true);
    }

    private int internalCreateTier(int tierListId, String label, String color, boolean isUnranked) {
        validator.validateTier(label, color, isUnranked); // Re-added validation call
        Tier newTier = new Tier(tierListId, label, color, isUnranked);
        return tierStorage.insertTier(tierListId, newTier);
    }

    public void removeTier(int tierId) {
        tierStorage.deleteTier(tierId);
    }

    public Tier getTier(int tierId) {
        return tierStorage.getTier(tierId);
    }

    public void renameTier(int tierId, String newLabel) {
        validator.validateLabel(newLabel);
        Tier tier = tierStorage.getTier(tierId);
        if (tier != null) {
            tier.setName(newLabel);
            tierStorage.updateTier(tier);
        }
    }

    public void changeTierColor(int tierId, String colorHex) {
        validator.validateColor(colorHex);
        Tier tier = tierStorage.getTier(tierId);
        if (tier != null) {
            validator.validateWritePermission(tier.isUnranked());
            tier.setColor(colorHex);
            tierStorage.updateTier(tier);
        }
    }

    public List<Tier> getTiersForList(int tierListId) {
        validator.validateTierListId(tierListId);
        return tierStorage.getTiersForList(tierListId);
    }
}
