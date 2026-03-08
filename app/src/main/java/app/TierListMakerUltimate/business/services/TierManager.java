package app.TierListMakerUltimate.business.services;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.business.validation.TierValidator;

import java.util.List;

public class TierManager implements ITierManager {
    private final TierPersistence tierStorage;
    private final TierValidator validator;

    public TierManager(TierPersistence tierStorage, TierValidator validator) throws InitializationException {
        if (tierStorage == null || validator == null) {
            throw new InitializationException(ERROR_DEPENDENCIES_NULL);
        }
        this.tierStorage = tierStorage;
        this.validator = validator;
    }

    @Override
    public Tier createTier(int tierListId, String label, String color) throws ValidationException {
        return createTier(tierListId, label, color, false);
    }

    @Override
    public Tier createTier(int tierListId, String label, String color, boolean isUnranked) throws ValidationException {
        validator.validateCreateTier(label, color);
        Tier newTier = new Tier(tierListId, label, color, isUnranked);
        return tierStorage.insertTier(tierListId, newTier);
    }

    @Override
    public void removeTier(int tierId) throws ValidationException, NotFoundException {
        validator.validateRemoveTier(tierId);
        getVerifiedTier(tierId);
        tierStorage.deleteTier(tierId);
    }

    @Override
    public Tier getTier(int tierId) throws ValidationException, NotFoundException {
        validator.validateTierId(tierId);
        return getVerifiedTier(tierId);
    }

    @Override
    public void updateTier(Tier updatedTier) throws ValidationException, NotFoundException {
        validator.validateUpdateTier(updatedTier);
        getVerifiedTier(updatedTier.getId());
        tierStorage.updateTier(updatedTier);
    }

    private Tier getVerifiedTier(int tierId) throws NotFoundException {
        Tier tier = tierStorage.getTier(tierId);
        if (tier == null) {
            throw new NotFoundException(ERROR_TIER_NOT_FOUND + tierId);
        }
        return tier;
    }

    @Override
    public List<Tier> getTiersForList(int tierListId) throws ValidationException {
        validator.validateTierListId(tierListId);
        return tierStorage.getTiersForList(tierListId);
    }
}
