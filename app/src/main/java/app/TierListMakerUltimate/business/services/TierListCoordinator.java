package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public class TierListCoordinator implements ITierListCoordinator {
    private final ITierManager tierManager;
    private final TierListManager tierListManager;

    public TierListCoordinator(TierManager tierManager, TierListManager tierListManager) throws InitializationException {
        if (tierManager == null || tierListManager == null) {
            throw new InitializationException("TierManager and TierListManager cannot be null");
        }
        this.tierManager = tierManager;
        this.tierListManager = tierListManager;
    }

    @Override
    public TierList addTierList(String name) throws ValidationException {
        TierList tierList = tierListManager.createTierList(name);
        tierManager.createTier(tierList.getId(), "unranked", "#7A7A7A", true);
        return tierList;
    }

    @Override
    public void removeTierList(int tierListId) throws ValidationException {
        tierListManager.removeTierList(tierListId);

        // TODO: The persistence layer should handle cascading deletes for tiers/items
    }

    @Override
    public Tier getUrankedTier(int tierListId) throws ValidationException {
        List<Tier> tiers = tierManager.getTiersForList(tierListId);
        for (Tier tier : tiers) {
            if (tier.isUnranked()) {
                return tier;
            }
        }

        throw new NotFoundException("Unranked tier not found for TierList ID: " + tierListId);
    }
}
