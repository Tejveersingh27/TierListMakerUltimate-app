package app.TierListMakerUltimate.persistence.interfaces;

import app.TierListMakerUltimate.models.TierItem;

import java.util.List;


/**
 * Handles persistence for TierItems.
 */
public interface TierItemPersistence {
    /**
     * Returns all TierItems for a given tier.
     */
    List<TierItem> getItemsForTier(int tierId);

    /**
     * Returns an existing TierItem.
     */
    TierItem getItem(int itemId);

    /**
     * Saves and returns a new TierItem.
     */
    TierItem insertItem(int tierId, TierItem currentItem);

    /**
     * Updates an existing TierItem and returns it.
     */
    TierItem updateItem(TierItem currentItem);

    /**
     * Deletes an existing TierItem.
     */
    void deleteItem(int itemId);
}