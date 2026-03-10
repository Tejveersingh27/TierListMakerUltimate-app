package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierList;

import java.io.InputStream;
import java.util.List;

/**
 * Manages basic TierList processes.
 */
public interface ITierListManager {
    /**
     * Creates a returns new TierList without creating a thumbnail.
     */
    TierList createTierList(String name, boolean isTemplate, InputStream inputStream, String extension) throws ValidationException;

    /**
     * Creates a new TierList with a thumbnail.
     */
    TierList createTierList(String name, String thumbnailPath, boolean isTemplate) throws ValidationException;


    /**
     * Returns an existing TierList.
     */
    TierList getTierList(int tierListId) throws ValidationException, NotFoundException;

    /**
     * Removes an existing TierList.
     */
    void removeTierList(int tierListId) throws ValidationException, NotFoundException;

    /**
     * Updates an existing TierList without creating a new thumbnail.
     */
    void updateTierList(TierList updatedTierList) throws ValidationException, NotFoundException;

    /**
     * Updates an existing TierList with a new thumbnail.
     */
    void updateTierList(TierList updatedTierList, InputStream inputStream, String extension) throws ValidationException, NotFoundException, PersistenceException;

    /**
     * Returns all TierLists.
     */
    List<TierList> getAllTierLists() throws ValidationException;

    /**
     * Returns all Templates.
     */
    List<TierList> getAllTemplates() throws ValidationException;
}
