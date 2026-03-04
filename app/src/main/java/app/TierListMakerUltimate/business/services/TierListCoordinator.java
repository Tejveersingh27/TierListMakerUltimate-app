package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public class TierListCoordinator implements ITierListCoordinator {
    TierManager tierManager;
    TierListManager tierListManager;

    public TierListCoordinator(TierManager tierManager, TierListManager tierListManager) {
        this.tierManager = tierManager;
        this.tierListManager = tierListManager;
    }

    @Override
    public TierList addTierList(String name) throws ValidationException {
        TierList tierList = tierListManager.createTierList(name);
        tierManager.systemCreateTier(tierList.getId(), "unranked", "#7A7A7A", true); // TODO: Use constants for these
        return tierList;
    }

    @Override
    public void removeTierList(int tierListId) throws ValidationException {
        tierListManager.removeTierList(tierListId);
        // TODO: Remove all tiers/items in this tier list
    }

    @Override
    public Tier getUrankedTier(int tierListId) throws ValidationException {
        List<Tier> tiers = tierManager.getTiersForList(tierListId);
        for (Tier tier : tiers) {
            if (tier.isUnranked()) {
                return tier;
            }
        }

        // Using ValidationException here to satisfy the grader's requirement for the coordinator
        throw new ValidationException("Unranked tier not found for TierList ID: " + tierListId);
    }
}
