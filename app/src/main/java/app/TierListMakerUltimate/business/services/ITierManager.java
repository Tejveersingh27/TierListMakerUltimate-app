package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;

import java.util.List;

/**
 * Manages basic Tier processes.
 */
public interface ITierManager {
    /**
     * Creates and returns a new Tier.
     */
    Tier createTier(int tierListId, String label, String color) throws ValidationException;

    /**
     * Creates and returns a new Tier.
     * Mostly for internal (business layer) use.
     */
    Tier createTier(int tierListId, String label, String color, boolean isUnranked, int position) throws ValidationException;

    /**
     * Creates and returns a new default Tier.
     */
    Tier createDefaultTier(int tierListId);

    /**
     * Removes an existing Tier.
     */
    void removeTier(int tierId) throws ValidationException, NotFoundException;

    /**
     * Returns an existing Tier.
     */
    Tier getTier(int tierId) throws ValidationException, NotFoundException;

    /**
     * Updates an existing Tier.
     */
    void updateTier(Tier updatedTier) throws ValidationException, NotFoundException;

    /**
     * Copies an existing Tier to a new tier list and returns the new Tier.
     */
    Tier copyTier(int tierId, int targetTierListId) throws ValidationException, NotFoundException;

    /**
     * Returns all tiers for a given tier list.
     */
    List<Tier> getTiersForList(int tierListId) throws ValidationException;

    /**
     * Returns the unranked tier for a given tier list.
     */
    Tier getUnrankedTierForList(int tierListId) throws ValidationException, NotFoundException;
}
