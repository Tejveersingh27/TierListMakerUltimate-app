package app.TierListMakerUltimate.business.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.exceptions.InitializationException;
import app.TierListMakerUltimate.business.exceptions.NotFoundException;
import app.TierListMakerUltimate.business.exceptions.ImageException;
import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.interfaces.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.interfaces.TierItemPersistence;
import app.TierListMakerUltimate.business.validation.ItemValidator;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;


public class ItemPlacementManager implements IItemPlacementManager {
    private final TierItemPersistence itemStorage;
    private final ImageFilePersistence imageFilePersistence;
    private final ItemValidator validator;

    public ItemPlacementManager(TierItemPersistence itemStorage, ImageFilePersistence imageFilePersistence, ItemValidator validator) throws InitializationException {
        if (itemStorage == null || imageFilePersistence == null || validator == null) {
            throw new InitializationException(ERROR_DEPENDENCIES_NULL);
        }
        this.imageFilePersistence = imageFilePersistence;
        this.itemStorage = itemStorage;
        this.validator = validator;
    }

    @Override
    public TierItem createItem(int tierId, String name, String description, InputStream inputStream, String extension) throws ValidationException, ImageException {
        String imagePath = storeImage(inputStream, extension);
        return createItem(imagePath, name, tierId, description);
    }

    @Override
    public TierItem createItem(String imagePath, String name, int tierId, String description) throws ValidationException {
        validator.validateCreateItem(tierId, name, description);
        TierItem newTierItem = new TierItem(imagePath, name, description, tierId);
        return itemStorage.insertItem(tierId, newTierItem);
    }


    @Override
    public TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException, NotFoundException {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem currentItem = getVerifiedItem(itemId);
        TierItem updatedItem = new TierItem(currentItem.getId(), currentItem.getImagePath(), currentItem.getName(), currentItem.getDescription(), targetTierId);
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
    public void updateItem(TierItem updatedItem, InputStream inputStream, String extension) throws ValidationException, NotFoundException, ImageException {
        validator.validateUpdateItem(updatedItem);
        getVerifiedItem(updatedItem.getId());
        String imagePath = storeImage(inputStream, extension);
        updateItem(new TierItem(updatedItem.getId(), imagePath, updatedItem.getName(), updatedItem.getDescription(), updatedItem.getTierId()));
    }

    @Override
    public TierItem copyItem(int itemId, int targetTierId) throws ValidationException, NotFoundException {
        TierItem verifiedItem = getVerifiedItem(itemId);
        return createItem(verifiedItem.getImagePath(), verifiedItem.getName(), targetTierId, verifiedItem.getDescription());
    }

    private String storeImage(InputStream inputStream, String extension) {
        try {
            return imageFilePersistence.saveImage(inputStream, extension);
        } catch (IOException ioe) {
            throw new ImageException(ERROR_STORING_IMAGE);
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
            throw new NotFoundException(ERROR_ITEM_NOT_FOUND + itemId);
        }
        return item;
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) throws ValidationException {
        validator.validateTierId(tierId);
        return itemStorage.getItemsForTier(tierId);
    }
}
