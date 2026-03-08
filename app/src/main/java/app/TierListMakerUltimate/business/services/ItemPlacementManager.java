package app.TierListMakerUltimate.business.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.constants.BusinessConstants;


public class ItemPlacementManager implements IItemPlacementManager {
    private final TierItemPersistence itemStorage;
    private final ImageFilePersistence imageFilePersistence;
    private final ItemValidator validator;

    public ItemPlacementManager(TierItemPersistence itemStorage, ImageFilePersistence imageFilePersistence, ItemValidator validator) throws InitializationException {
        if (itemStorage == null || imageFilePersistence == null || validator == null) {
            throw new InitializationException(BusinessConstants.ERROR_DEPENDENCIES_NULL);
        }
        this.imageFilePersistence = imageFilePersistence;
        this.itemStorage = itemStorage;
        this.validator = validator;
    }

    @Override
    public TierItem createItem(int tierId, String description, InputStream inputStream, String extension) throws ValidationException, PersistenceException {
        validator.validateCreateItem(tierId, description);
        String imagePath = storeImage(inputStream, extension);
        TierItem newTierItem = new TierItem(imagePath, description, tierId);
        return itemStorage.insertItem(tierId, newTierItem);
    }

    @Override
    public TierItem createItem(String imagePath, int tierId, String description) throws ValidationException {
        validator.validateCreateItem(tierId, description);
        TierItem newTierItem = new TierItem(imagePath, description, tierId);
        return itemStorage.insertItem(tierId, newTierItem);
    }

    @Override
    public TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException, NotFoundException {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem currentItem = getVerifiedItem(itemId);
        TierItem updatedItem = new TierItem(currentItem.getId(), currentItem.getImagePath(), currentItem.getDescription(), targetTierId);
        updateItem(updatedItem);
        return itemStorage.updateItem(updatedItem);
    }

    @Override
    public void updateItem(TierItem updatedItem) throws ValidationException, NotFoundException {
        validator.validateUpdateItem(updatedItem);
        getVerifiedItem(updatedItem.getId());
        itemStorage.updateItem(updatedItem);
    }

    @Override
    public void updateItem(TierItem updatedItem, InputStream inputStream, String extension) throws ValidationException, NotFoundException, PersistenceException {
        validator.validateUpdateItem(updatedItem);
        getVerifiedItem(updatedItem.getId());
        String imagePath = storeImage(inputStream, extension);
        updateItem(new TierItem(updatedItem.getId(), imagePath, updatedItem.getDescription(), updatedItem.getTierId()));
    }

    private String storeImage(InputStream inputStream, String extension) {
        try {
            return imageFilePersistence.saveImage(inputStream, extension);
        } catch (IOException ioe) {
            throw new PersistenceException(BusinessConstants.ERROR_STORING_IMAGE);
        }
    }

    @Override
    public void removeItem(int itemId) throws ValidationException, NotFoundException {
        validator.validateRemoveItem(itemId);
        getVerifiedItem(itemId);
        itemStorage.deleteItem(itemId);
    }

    @Override
    public TierItem getItem(int itemId) throws ValidationException, NotFoundException {
        validator.validateItemId(itemId);
        return getVerifiedItem(itemId);
    }

    private TierItem getVerifiedItem(int itemId) throws NotFoundException {
        TierItem item = itemStorage.getItem(itemId);
        if (item == null) {
            throw new NotFoundException(BusinessConstants.ERROR_ITEM_NOT_FOUND + itemId);
        }
        return item;
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) throws ValidationException {
        validator.validateTierId(tierId);
        return itemStorage.getItemsForTier(tierId);
    }
}
