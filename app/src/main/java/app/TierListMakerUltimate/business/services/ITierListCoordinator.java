package app.TierListMakerUltimate.business.services;

import java.io.InputStream;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;


/**
 * Coordinates TierList processes by using TierListManager ,TierManager and ItemPlacementManager
 * to handle multi step processes like creating a TierList with its default tiers.
 */
public interface ITierListCoordinator {

    /**
     * Creates and returns a new TierList with default tiers and creates a thumbnail.
     */
    TierList createTierListWithDefaults(String name, boolean isTemplate, InputStream inputStream, String extension) throws ValidationException, PersistenceException;


    /**
     * Creates and returns a new TierList with default tiers without a creating a thumbnail.
     */
    TierList createTierListWithDefaults(String name, String thumbnailPath, boolean isTemplate) throws ValidationException;

    /**
     * Removes a TierList and all associated tiers and items.
     */
    void removeTierList(int tierListId) throws ValidationException;

    /**
     * Copies a TierList and all associated tiers and items.
     * Items copied to unranked tier.
     */
    TierList deepCopyAsTemplate(int tierListId, boolean resultIsTemplate) throws ValidationException, NotFoundException;

    /**
     * Removes a Tier and moves all items to the unranked tier for the same tier list.
     */
    void removeTierAndMoveAllItemsToUnranked(int tierId) throws ValidationException, NotFoundException;
}
