package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.business.validation.ItemValidator;

public class ItemPlacementManager implements IItemPlacementManager {
    private final TierItemPersistence itemStorage;
    private final ItemValidator validator;

    public ItemPlacementManager(TierItemPersistence itemStorage, ItemValidator validator) {
        if (itemStorage == null || validator == null) {
            throw new IllegalArgumentException("TierItemPersistence and ItemValidator cannot be null"); // TODO custom exception
        }
        this.itemStorage = itemStorage;
        this.validator = validator;
    }

    @Override
    public TierItem createItem(int localImagePath, int tierId, String description) throws ValidationException {
        validator.validateCreateItem(localImagePath, tierId, description);
        TierItem newTierItem = new TierItem(localImagePath, description, tierId);
        return itemStorage.insertItem(tierId, newTierItem);
    }

    @Override
    public TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem targetItem = itemStorage.getItem(itemId);
        if (targetItem == null) {
            throw new RuntimeException("Tier Item not Found"); // TODO custom exception
        }
        targetItem.setTierId(targetTierId);
        return itemStorage.updateItem(targetItem);
    }

    @Override
    public void updateItem(TierItem updatedItem) throws ValidationException {
        validator.validateUpdateItem(updatedItem);

        if (itemStorage.getItem(updatedItem.getId()) == null) {
            throw new RuntimeException("Tier Item not found"); //TODO: custom exception
        }

        itemStorage.updateItem(updatedItem);
    }

    @Override
    public void removeItem(int itemId) throws ValidationException {
        validator.validateRemoveItem(itemId);
        itemStorage.deleteItem(itemId);
    }

    @Override
    public TierItem getItem(int itemId) throws ValidationException {
        validator.validateItemId(itemId);
        return itemStorage.getItem(itemId);
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) throws ValidationException {
        validator.validateTierId(tierId);
        return itemStorage.getItemsForTier(tierId);
    }
}