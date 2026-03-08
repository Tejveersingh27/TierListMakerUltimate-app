package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.TierList;

import java.util.List;

/**
 * Handles persistence for TierLists.
 */
public interface TierListPersistence {
    /**
     * Returns all TierLists.
     */
    List<TierList> getTierLists();

    /**
     * Returns an existing TierList.
     */
    TierList getTierListById(int tierListId);

    /**
     * Saves and returns a new TierList.
     */
    TierList insertTierList(TierList currentTierList);

    /**
     * Updates an existing TierList and returns it.
     */
    TierList updateTierList(TierList currentTierList);

    /**
     * Deletes an existing TierList.
     */
    void deleteTierList(int tierListId);
    
}