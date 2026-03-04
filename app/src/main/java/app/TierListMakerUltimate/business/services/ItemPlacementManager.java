package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.business.validation.ItemValidator;

public class ItemPlacementManager implements IItemPlacementManager {
    private final TierItemPersistence itemStorage;
    private final ItemValidator validator;

    public ItemPlacementManager(TierItemPersistence itemStorage, ItemValidator validator) throws InitializationException {
        if (itemStorage == null || validator == null) {
            throw new InitializationException("TierItemPersistence and ItemValidator cannot be null");
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
    public TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException, NotFoundException {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem targetItem = itemStorage.getItem(itemId);
        if (targetItem == null) {
            throw new NotFoundException("Tier Item not found with ID: " + itemId);
        }
        targetItem.setTierId(targetTierId);
        return itemStorage.updateItem(targetItem);
    } // TODO use update instead

    @Override
    public void updateItem(TierItem updatedItem) throws ValidationException, NotFoundException {
        validator.validateUpdateItem(updatedItem);

        if (itemStorage.getItem(updatedItem.getId()) == null) {
            throw new NotFoundException("Tier Item not found with ID: " + updatedItem.getId());
        }

        itemStorage.updateItem(updatedItem);
    }

    @Override
    public void removeItem(int itemId) throws ValidationException, NotFoundException {
        validator.validateRemoveItem(itemId);
        if (itemStorage.getItem(itemId) == null) {
            throw new NotFoundException("Tier Item not found with ID: " + itemId);
        }
        itemStorage.deleteItem(itemId);
    }

    @Override
    public TierItem getItem(int itemId) throws ValidationException, NotFoundException {
        validator.validateItemId(itemId);
        TierItem item = itemStorage.getItem(itemId);
        if (item == null) {
            throw new NotFoundException("Tier Item not found with ID: " + itemId);
        }
        return item;
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) throws ValidationException {
        validator.validateTierId(tierId);
        return itemStorage.getItemsForTier(tierId);
    }
}
