package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.business.validation.TierListValidator;

public class TierListManager implements ITierListManager {
    private final TierListPersistence tierListStorage;
    private final TierListValidator validator;

    public TierListManager(TierListPersistence tierListStorage, TierListValidator validator) throws InitializationException {
        if (tierListStorage == null || validator == null) {
            throw new InitializationException("TierListPersistence and TierListValidator cannot be null");
        }
        this.tierListStorage = tierListStorage;
        this.validator = validator;
    }

    @Override
    public TierList createTierList(String name) throws ValidationException {
        validator.validateCreateTierList(name);
        TierList newList = new TierList(name);
        return tierListStorage.insertTierList(newList);
    }

    @Override
    public TierList getTierList(int tierListId) throws ValidationException, NotFoundException {
        validator.validateTierListId(tierListId);
        TierList tierList = tierListStorage.getTierListById(tierListId);
        if (tierList == null) {
            throw new NotFoundException("TierList with ID " + tierListId + " not found");
        }
        return tierList;
    }

    @Override
    public void removeTierList(int tierListId) throws ValidationException, NotFoundException {
        validator.validateDeleteTierList(tierListId);
        if (tierListStorage.getTierListById(tierListId) == null) {
            throw new NotFoundException("TierList with ID " + tierListId + " not found");
        }
        tierListStorage.deleteTierList(tierListId);
    }

    @Override
    public void updateTierList(TierList updatedTierList) throws ValidationException, NotFoundException {
        validator.validateUpdateTierList(updatedTierList);

        if (tierListStorage.getTierListById(updatedTierList.getId()) == null) {
            throw new NotFoundException("TierList not found with ID: " + updatedTierList.getId());
        }

        tierListStorage.updateTierList(updatedTierList);
    }

    @Override
    public List<TierList> getAllTierLists() throws ValidationException {
        return tierListStorage.getAllTierLists();
    }
}
