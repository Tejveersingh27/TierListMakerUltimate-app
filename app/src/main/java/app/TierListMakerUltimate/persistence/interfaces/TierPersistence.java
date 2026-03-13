package app.TierListMakerUltimate.persistence.interfaces;

import app.TierListMakerUltimate.models.Tier;

import java.util.List;

/**
 * Handles persistence for Tiers.
 */
public interface TierPersistence {
    /**
     * Returns all Tiers for a given TierList.
     */
    List<Tier> getTiersForList(int tierListId);

    /**
     * Returns an existing Tier.
     */
    Tier getTier(int tierId);

    /**
     * Saves and returns a new Tier.
     */
    Tier insertTier(int tierListId, Tier currentTier); // Returns ID

    /**
     * Updates an existing Tier and returns it.
     */
    Tier updateTier(Tier currentTier);

    /**
     * Deletes an existing Tier.
     */
    void deleteTier(int tierId);
}