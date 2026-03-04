package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.business.constants.DefaultTiers;

public class TierListCoordinator implements ITierListCoordinator {
    TierManager tierManager;
    TierListManager tierListManager;

    public TierListCoordinator(TierManager tierManager, TierListManager tierListManager) {
        this.tierManager = tierManager;
        this.tierListManager = tierListManager;
    }

    @Override
    public int addTierList(String name) {
        int tierListId = tierListManager.createTierList(name);
        createDefaultTiers(tierListId);
        return tierListId;
    }

    private void createDefaultTiers(int tierListId) {
        for (DefaultTiers tier : DefaultTiers.values()) {
            tierManager.systemCreateTier(tierListId, tier.label, tier.color, tier.isUnranked);
        }
    }

    @Override
    public void removeTierList(int tierListId) {
        tierListManager.removeTierList(tierListId);
        // TODO: Remove all tiers/items in this tier list
    }

    @Override
    public Tier getUrankedTier(int tierListId) {
        List<Tier> tiers = tierManager.getTiersForList(tierListId);
        for (Tier tier : tiers) {
            if (tier.isUnranked()) {
                return tier;
            }
        }

        throw new RuntimeException("Unranked tier not found"); // TODO: Use custom exception
    }
}
