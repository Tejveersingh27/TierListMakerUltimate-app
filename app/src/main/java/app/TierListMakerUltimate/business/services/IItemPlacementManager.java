package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

import java.io.InputStream;
import java.util.List;

public interface IItemPlacementManager {
    TierItem createItem(String imagePath, int tierId, String description) throws ValidationException;

    TierItem createItem(int tierId, String description, InputStream inputStream, String extension) throws ValidationException, PersistenceException;


    TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException, NotFoundException;

    void updateItem(TierItem updatedItem, InputStream inputStream, String extension) throws ValidationException, NotFoundException, PersistenceException;

    void updateItem(TierItem updatedItem) throws ValidationException, NotFoundException;

    void removeItem(int itemId) throws ValidationException, NotFoundException;

    TierItem getItem(int itemId) throws ValidationException, NotFoundException;

    List<TierItem> getItemsForTier(int tierId) throws ValidationException;
}
