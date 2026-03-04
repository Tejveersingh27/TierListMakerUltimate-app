package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.business.validation.TierListValidator;

public class TierListManager {
    private final TierListPersistence tierListStorage;
    private final TierListValidator validator;

    public TierListManager(TierListPersistence tierListStorage, TierListValidator validator) {
        if (tierListStorage == null || validator == null) {
            throw new IllegalArgumentException("TierListPersistence and TierListValidator cannot be null"); // TODO custom exception
        }
        this.tierListStorage = tierListStorage;
        this.validator = validator;
    }

    public TierList createTierList(String name) {
        validator.validateCreateTierList(name);
        TierList newList = new TierList(name);
        return tierListStorage.insertTierList(newList);
    }

    public TierList getTierList(int tierListId) {
        validator.validateTierListId(tierListId);
        return tierListStorage.getTierListById(tierListId);
    }

    public void removeTierList(int tierListId) {
        validator.validateDeleteTierList(tierListId);
        tierListStorage.deleteTierList(tierListId);
    }

    public void updateTierList(TierList updatedTierList) {
        validator.validateUpdateTierList(updatedTierList);

        if (tierListStorage.getTierListById(updatedTierList.getId()) == null) {
            throw new RuntimeException("TierList not found"); // TODO custom exception
        }

        tierListStorage.updateTierList(updatedTierList);
    }

    public List<TierList> getAllTierLists() {
        return tierListStorage.getAllTierLists();
    }

}
