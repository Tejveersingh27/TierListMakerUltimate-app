package app.TierListMakerUltimate.business.services;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import app.TierListMakerUltimate.business.constants.DefaultTiers;
import app.TierListMakerUltimate.business.exceptions.InitializationException;
import app.TierListMakerUltimate.business.exceptions.NotFoundException;
import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.interfaces.TierPersistence;
import app.TierListMakerUltimate.business.validation.TierValidator;

import java.util.Comparator;
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
        return createTier(tierListId, label, color, false, getRankedTiersForList(tierListId).size());
    }

    @Override
    public Tier createTier(int tierListId, String label, String color, boolean isUnranked, int position) throws ValidationException {
        validator.validateCreateTier(label, color);
        Tier newTier = new Tier(tierListId, label, color, isUnranked, position);
        return tierStorage.insertTier(tierListId, newTier);
    }

    @Override
    public Tier createDefaultTier(int tierListId) {
        return createTier(tierListId, DefaultTiers.DEFAULT_NAME, DefaultTiers.DEFAULT_COLOR);
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

    @Override
    public Tier copyTier(int tierId, int targetTierListId) throws ValidationException, NotFoundException {
        Tier verifiedTier = getVerifiedTier(tierId);
        return createTier(targetTierListId, verifiedTier.getName(), verifiedTier.getColor(), verifiedTier.isUnranked(), verifiedTier.getOrdinalPosition());
    }


    @Override
    public void moveRankedTier(int tierId, int delta) throws ValidationException, NotFoundException {
        System.out.println("AAAAAAAAAAAAAHHHHHHHHHHHH");
        Tier targetTier = getVerifiedTier(tierId);
        List<Tier> tiers = getRankedTiersForList(targetTier.getTierListId());

        int currentIndex = calculateRankedTierIndex(tierId, tiers);
        int newIndex = currentIndex + delta;
        if (currentIndex >= 0 && newIndex >= 0 && newIndex < tiers.size()) {
            swapTierPositions(targetTier, tiers.get(newIndex));
        }
    }


    private int calculateRankedTierIndex(int tierId, List<Tier> tiers) {
        for (int i = 0; i < tiers.size(); i++) {
            if (tiers.get(i).getId() == tierId) {
                return i;
            }
        }
        return -1;
    }

    private void swapTierPositions(Tier tier1, Tier tier2) throws NotFoundException {
        tierStorage.updateTier(new Tier(tier1.getId(), tier1.getTierListId(), tier1.getName(), tier1.getColor(), tier1.isUnranked(), tier2.getOrdinalPosition()));
        tierStorage.updateTier(new Tier(tier2.getId(), tier2.getTierListId(), tier2.getName(), tier2.getColor(), tier2.isUnranked(), tier1.getOrdinalPosition()));
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
        List<Tier> tiers = tierStorage.getTiersForList(tierListId);
        tiers.sort(Comparator.comparingInt(Tier::getOrdinalPosition));
        return tiers;
    }

    @Override
    public Tier getUnrankedTierForList(int tierListId) throws ValidationException, NotFoundException {
        validator.validateTierListId(tierListId);
        List<Tier> tiers = tierStorage.getTiersForList(tierListId);
        for (Tier tier : tiers) {
            if (tier.isUnranked()) {
                return tier;
            }
        }
        throw new NotFoundException(ERROR_TIER_NOT_FOUND);
    }

    @Override
    public List<Tier> getRankedTiersForList(int tierListId) throws ValidationException {
        validator.validateTierListId(tierListId);
        List<Tier> tiers = getTiersForList(tierListId);
        tiers.removeIf(Tier::isUnranked);
        return tiers;
    }
}
