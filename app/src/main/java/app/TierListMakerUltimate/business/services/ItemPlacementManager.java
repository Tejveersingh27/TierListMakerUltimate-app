package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.business.validation.ItemValidator;

public class ItemPlacementManager {
    private final TierItemPersistence itemStorage;
    private final ItemValidator validator;

    public ItemPlacementManager(TierItemPersistence itemStorage, ItemValidator validator) {
        if (itemStorage == null || validator == null) {
            throw new IllegalArgumentException("TierItemPersistence and ItemValidator cannot be null");
        }
        this.itemStorage = itemStorage;
        this.validator = validator;
    }

    public TierItem createItem(String localImagePath, int tierId, String description) {
        validator.validateCreateItem(localImagePath, tierId, description);
        TierItem newTierItem = new TierItem(localImagePath, description, tierId);
        int tierItemId = itemStorage.insertItem(tierId, newTierItem);
        newTierItem.setId(tierItemId);
        return newTierItem;
    }

    public void moveItemToTier(int itemId, int targetTierId) {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem targetItem = itemStorage.getItem(itemId);
        if (targetItem == null) {
            throw new ValidationException("Tier Item not Found");
        }
        targetItem.setTierId(targetTierId);
        itemStorage.updateItem(targetItem);
    }

    public void removeItem(int itemId) {
        validator.validateItemId(itemId);
        itemStorage.deleteItem(itemId);
    }

    public TierItem getItem(int itemId) {
        validator.validateItemId(itemId);
        return itemStorage.getItem(itemId);
    }

    public List<TierItem> getItemsForTier(int tierId) {
        validator.validateTierId(tierId);
        return itemStorage.getItemsForTier(tierId);
    }
}
