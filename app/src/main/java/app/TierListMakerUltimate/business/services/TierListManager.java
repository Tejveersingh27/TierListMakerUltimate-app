package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.business.validation.TierListValidator;

public class TierListManager implements ITierListManager {
    private final TierListPersistence tierListStorage;
    private final TierListValidator validator;

    public TierListManager(TierListPersistence tierListStorage, TierListValidator validator) {
        if (tierListStorage == null || validator == null) {
            throw new IllegalArgumentException("TierListPersistence and TierListValidator cannot be null"); // TODO custom exception
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
    public TierList getTierList(int tierListId) throws ValidationException {
        validator.validateTierListId(tierListId);
        return tierListStorage.getTierListById(tierListId);
    }

    @Override
    public void removeTierList(int tierListId) throws ValidationException {
        validator.validateDeleteTierList(tierListId);
        tierListStorage.deleteTierList(tierListId);
    }

    @Override
    public void updateTierList(TierList updatedTierList) throws ValidationException {
        validator.validateUpdateTierList(updatedTierList);

        if (tierListStorage.getTierListById(updatedTierList.getId()) == null) {
            throw new RuntimeException("TierList not found"); // TODO custom exception
        }

        tierListStorage.updateTierList(updatedTierList);
    }

    @Override
    public List<TierList> getAllTierLists() throws ValidationException {
        return tierListStorage.getAllTierLists();
    }
}
