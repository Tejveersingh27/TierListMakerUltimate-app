package app.TierListMakerUltimate.business.services;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public class TierListCoordinator implements ITierListCoordinator {
    private final ITierManager tierManager;
    private final ITierListManager tierListManager;

    public TierListCoordinator(ITierManager tierManager, ITierListManager tierListManager) throws InitializationException {
        if (tierManager == null || tierListManager == null) {
            throw new InitializationException(ERROR_MANAGERS_NULL);
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
    public void removeTierList(int tierListId) throws ValidationException, NotFoundException {
        tierListManager.removeTierList(tierListId);
    }

    @Override
    public Tier getUrankedTier(int tierListId) throws ValidationException, NotFoundException {
        List<Tier> tiers = tierManager.getTiersForList(tierListId);
        for (Tier tier : tiers) {
            if (tier.isUnranked()) {
                return tier;
            }
        }

        throw new NotFoundException(ERROR_TIER_NOT_FOUND + "unranked for list " + tierListId);
    }
}
