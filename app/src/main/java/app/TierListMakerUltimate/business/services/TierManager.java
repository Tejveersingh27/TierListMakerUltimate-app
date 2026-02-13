package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.business.validation.TierValidator;

import java.util.List;

public class TierManager {
    private final TierPersistence tierStorage;
    private final TierValidator validator;

    public TierManager(TierPersistence tierStorage) {
        this(tierStorage, new TierValidator());
    }

    public TierManager(TierPersistence tierStorage, TierValidator validator) {
        this.tierStorage = tierStorage;
        this.validator = validator;
    }

    public Tier createTier(int tierListId, String label, String color) {
        validator.validateTier(label, color);
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
            tier.setColor(colorHex);
            tierStorage.updateTier(tier);
        }
    }

    public List<Tier> getTiersForList(int tierListId) {
        return tierStorage.getTiersForList(tierListId);
    }
}
