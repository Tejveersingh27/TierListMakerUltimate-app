package app.TierListMakerUltimate.business.services;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.business.validation.TierListValidator;

public class TierListManager implements ITierListManager {
    private final TierListPersistence tierListStorage;
    private final ImageFilePersistence imageFilePersistence;
    private final TierListValidator validator;

    public TierListManager(TierListPersistence tierListStorage, ImageFilePersistence imageFilePersistence, TierListValidator validator) throws InitializationException {
        if (tierListStorage == null || imageFilePersistence == null || validator == null) {
            throw new InitializationException(ERROR_DEPENDENCIES_NULL);
        }
        this.tierListStorage = tierListStorage;
        this.imageFilePersistence = imageFilePersistence;
        this.validator = validator;
    }

    @Override
    public TierList createTierList(String name, InputStream inputStream, String extension) throws ValidationException {
        String thumbnailPath = storeImage(inputStream, extension);
        return createTierList(name, thumbnailPath);
    }

    @Override
    public TierList createTierList(String name, String thumbnailPath) throws ValidationException {
        validator.validateCreateTierList(name);
        TierList newList = new TierList(name, thumbnailPath);
        return tierListStorage.insertTierList(newList);
    }

    @Override
    public TierList getTierList(int tierListId) throws ValidationException, NotFoundException {
        validator.validateTierListId(tierListId);
        return getVerifiedTierList(tierListId);
    }

    @Override
    public void removeTierList(int tierListId) throws ValidationException, NotFoundException {
        validator.validateDeleteTierList(tierListId);
        getVerifiedTierList(tierListId);
        tierListStorage.deleteTierList(tierListId);
    }

    @Override
    public void updateTierList(TierList updatedTierList) throws ValidationException, NotFoundException {
        validator.validateUpdateTierList(updatedTierList);
        getVerifiedTierList(updatedTierList.getId());
        tierListStorage.updateTierList(updatedTierList);
    }

    @Override
    public void updateTierList(TierList updatedTierList, InputStream inputStream, String extension) throws ValidationException, NotFoundException, PersistenceException {
        validator.validateUpdateTierList(updatedTierList);
        getVerifiedTierList(updatedTierList.getId());
        String thumbnailPath = storeImage(inputStream, extension);
        updateTierList(new TierList(updatedTierList.getId(), updatedTierList.getName(), thumbnailPath));
    }

    private String storeImage(InputStream inputStream, String extension) {
        try {
            return imageFilePersistence.saveImage(inputStream, extension);
        } catch (IOException ioe) {
            throw new PersistenceException(ERROR_STORING_IMAGE);
        }
    }

    private TierList getVerifiedTierList(int tierListId) throws NotFoundException {
        TierList tierList = tierListStorage.getTierListById(tierListId);
        if (tierList == null) {
            throw new NotFoundException(ERROR_TIER_LIST_NOT_FOUND + tierListId);
        }
        return tierList;
    }

    @Override
    public List<TierList> getAllTierLists() throws ValidationException {
        return tierListStorage.getTierLists();
    }
}
