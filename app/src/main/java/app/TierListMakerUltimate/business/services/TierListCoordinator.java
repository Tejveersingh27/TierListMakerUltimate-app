package app.TierListMakerUltimate.business.services;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.business.constants.DefaultTiers;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.models.TierList;

public class TierListCoordinator implements ITierListCoordinator {
    private final ITierManager tierManager;
    private final ITierListManager tierListManager;

    private final IItemPlacementManager itemPlacementManager;


    public TierListCoordinator(ITierManager tierManager, ITierListManager tierListManager, IItemPlacementManager itemPlacementManager) throws InitializationException {
        if (tierManager == null || tierListManager == null || itemPlacementManager == null) {
            throw new InitializationException(ERROR_DEPENDENCIES_NULL);
        }
        this.tierManager = tierManager;
        this.tierListManager = tierListManager;
        this.itemPlacementManager = itemPlacementManager;
    }

    @Override
    public TierList createTierListWithDefaults(String name, String thumbnailPath, boolean isTemplate) throws ValidationException {
        TierList tierList = tierListManager.createTierList(name, thumbnailPath, isTemplate);
        createDefaultTiers(tierList.getId());
        return tierList;
    }

    public TierList createTierListWithDefaults(String name, boolean isTemplate, InputStream inputStream, String extension) throws ValidationException, PersistenceException {
        TierList tierList = tierListManager.createTierList(name, isTemplate, inputStream, extension);
        createDefaultTiers(tierList.getId());
        return tierList;
    }


    private void createDefaultTiers(int tierListId) {
        for (DefaultTiers tier : DefaultTiers.values()) {
            tierManager.createTier(tierListId, tier.label, tier.color, tier.isUnranked);
        }
    }

    @Override
    public void removeTierList(int tierListId) throws ValidationException, NotFoundException {
        tierListManager.removeTierList(tierListId);
        for (Tier tier : tierManager.getTiersForList(tierListId)) {
            tierManager.removeTier(tier.getId());

            for (TierItem item : itemPlacementManager.getItemsForTier(tier.getId())) {
                itemPlacementManager.removeItem(item.getId());
            }
        }
    }

    @Override
    public TierList deepCopyAsTemplate(int tierListId) throws ValidationException, NotFoundException {
        TierList newTierList = copyTierList(tierListId);
        copyAllItems(tierListId, newTierList.getId());
        return newTierList;
    }

    private TierList copyTierList(int tierListId) throws ValidationException, NotFoundException {
        TierList currentTierList = tierListManager.getTierList(tierListId);
        TierList newTierList = tierListManager.copy(currentTierList.getId());

        List<Tier> currentTiers = tierManager.getTiersForList(tierListId);
        for (Tier tier : currentTiers) {
            tierManager.copyTier(tier.getId(), newTierList.getId());
        }

        return newTierList;
    }

    private void copyAllItems(int sourceTierListId, int newTierListId) throws ValidationException, NotFoundException {
        Tier newUnrankedTier = tierManager.getUnrankedTierForList(newTierListId);
        List<Tier> sourceTiers = tierManager.getTiersForList(sourceTierListId);

        for (Tier tier : sourceTiers) {
            List<TierItem> items = itemPlacementManager.getItemsForTier(tier.getId());
            for (TierItem item : items) {
                itemPlacementManager.copyItem(item.getId(), newUnrankedTier.getId());
            }
        }
    }

}
