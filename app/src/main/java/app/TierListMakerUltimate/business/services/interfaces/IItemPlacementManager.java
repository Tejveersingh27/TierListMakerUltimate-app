package app.TierListMakerUltimate.business.services.interfaces;

import app.TierListMakerUltimate.business.exceptions.NotFoundException;
import app.TierListMakerUltimate.business.exceptions.ImageException;
import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

import java.io.InputStream;
import java.util.List;

/**
 * Manages basic TierItem processes.
 */
public interface IItemPlacementManager {
    /**
     * Creates and returns a new TierItem without creating a new image.
     */
    TierItem createItem(String imagePath, String name, int tierId, String description, String explanation) throws ValidationException;

    /**
     * Creates and returns a new TierItem with a new image.
     */
    TierItem createItem(int tierId, String name, String description, String explanation, InputStream inputStream, String extension) throws ValidationException, ImageException;

    /**
     * Moves an item to a new tier and returns the updated TierItem.
     */
    TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException, NotFoundException;

    /**
     * Updates an existing TierItem and creates a new image for it.
     */
    void updateItem(TierItem updatedItem, InputStream inputStream, String extension) throws ValidationException, NotFoundException, ImageException;

    /**
     * Updates an existing TierItem without creating a new image.
     */
    void updateItem(TierItem updatedItem) throws ValidationException, NotFoundException;

    /**
     * Copies an existing TierItem to a new tier and returns the new TierItem.
     */
    TierItem copyItem(int itemId, int targetTierId) throws ValidationException, NotFoundException;

    /**
     * Removes an existing TierItem.
     */
    void removeItem(int itemId) throws ValidationException, NotFoundException;

    /**
     * Returns an existing TierItem.
     */
    TierItem getItem(int itemId) throws ValidationException, NotFoundException;

    /**
     * Returns all TierItems for a given tier.
     */
    List<TierItem> getItemsForTier(int tierId) throws ValidationException;
}
