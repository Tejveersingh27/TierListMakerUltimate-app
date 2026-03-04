package app.TierListMakerUltimate.business.services;

import android.content.Context;

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

    public TierItem createItem(int localImagePath, int tierId, String description) throws ValidationException {
        validator.validateCreateItem(localImagePath, tierId, description);
        TierItem newTierItem = new TierItem(localImagePath, description, tierId);
        return itemStorage.insertItem(tierId, newTierItem);
    }

    public TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem targetItem = itemStorage.getItem(itemId);
        if (targetItem == null) {
            throw new ValidationException("Tier Item not Found");
        }
        targetItem.setTierId(targetTierId);
        return itemStorage.updateItem(targetItem);
    } // TODO: USE UPDATE INSTEAD

    public void updateItem(TierItem updatedItem) throws ValidationException {
        validator.validateUpdateItem(updatedItem);

        if (itemStorage.getItem(updatedItem.getId()) == null) {
            throw new RuntimeException("Tier Item not found"); //TODO: custom exception
        }

        itemStorage.updateItem(updatedItem);
    }


    public void removeItem(int itemId) throws ValidationException {
        validator.validateRemoveItem(itemId);
        itemStorage.deleteItem(itemId);
    }

    public TierItem getItem(int itemId) throws ValidationException {
        validator.validateItemId(itemId);
        return itemStorage.getItem(itemId);
    }

    public List<TierItem> getItemsForTier(int tierId) throws ValidationException {
        validator.validateTierId(tierId);
        return itemStorage.getItemsForTier(tierId);
    }
}
