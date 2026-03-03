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
            throw new IllegalArgumentException("TierListPersistence and TierListValidator cannot be null");
        }
        this.tierListStorage = tierListStorage;
        this.validator = validator;
    }

    int createTierList(String name) {
        validator.validateCreateTierList(name);
        TierList newList = new TierList(name);
        return tierListStorage.insertTierList(newList);
    }

    public TierList getTierList(int tierListId) {
        validator.validateTierListId(tierListId);
        return tierListStorage.getTierListById(tierListId);
    }

    public void removeTierList(int tierListId) {
        validator.validateRemoveTierList(tierListId);
        tierListStorage.deleteTierList(tierListId);
    }

    public List<TierList> getAllTierLists() {
        return tierListStorage.getAllTierLists();
    }

}
