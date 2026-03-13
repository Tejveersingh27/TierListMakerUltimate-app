package app.TierListMakerUltimate.persistence.interfaces;

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
     * Returns all template TierLists.
     */
    List<TierList> getTemplates();

    /**
     * Returns an existing TierList.
     */
    TierList getTierList(int tierListId);

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

    /**
     * Returns all non-template TierLists.
     */
    List<TierList> getNonTemplateTierLists();
}