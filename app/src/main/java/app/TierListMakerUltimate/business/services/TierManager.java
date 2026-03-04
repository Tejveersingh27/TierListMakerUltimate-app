package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.business.validation.TierValidator;

import java.util.List;

public class TierManager {
    private final TierPersistence tierStorage;
    private final TierValidator validator;

    public TierManager(TierPersistence tierStorage, TierValidator validator) {
        if (tierStorage == null || validator == null) {
            throw new IllegalArgumentException("TierPersistence and TierValidator cannot be null"); // TODO custom exception
        }
        this.tierStorage = tierStorage;
        this.validator = validator;
    }

    public Tier createTier(int tierListId, String label, String color) throws ValidationException {
        return systemCreateTier(tierListId, label, color, false);
    }

    Tier systemCreateTier(int tierListId, String label, String color, boolean isUnranked) throws ValidationException {
        validator.validateCreateTier(label, color);
        Tier newTier = new Tier(tierListId, label, color, isUnranked);
        return tierStorage.insertTier(tierListId, newTier);
    }

    public void removeTier(int tierId) throws ValidationException {
        validator.validateRemoveTier(tierId);
        tierStorage.deleteTier(tierId);
    }

    public Tier getTier(int tierId) {
        return tierStorage.getTier(tierId);
    }

    public void updateTier(Tier updatedTier) throws ValidationException {
        validator.validateUpdateTier(updatedTier);

        if (tierStorage.getTier(updatedTier.getId()) == null) {
            throw new RuntimeException("Tier not found"); // TODO custom exception)
        }

        tierStorage.updateTier(updatedTier);
    }


    public List<Tier> getTiersForList(int tierListId) throws ValidationException {
        validator.validateTierListId(tierListId);
        return tierStorage.getTiersForList(tierListId);
    }
}
