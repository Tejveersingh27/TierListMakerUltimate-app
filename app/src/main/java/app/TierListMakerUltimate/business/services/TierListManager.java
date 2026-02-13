package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.business.validation.TierListValidator;

public class TierListManager {
    private final TierListPersistence tierListStorage;
    private final TierListValidator validator;

    public TierListManager(TierListPersistence tierListStorage, TierListValidator validator) {
        this.tierListStorage = tierListStorage;
        this.validator = validator;
    }

    public TierList createTierList(String name) {
        validator.validateCreateTierList(name);
        TierList newList = new TierList(name);
        int id = tierListStorage.insertTierList(newList);
        newList.setId(id);
        return newList;
    }

    public void removeTierList(int tierListId) {
        validator.validateRemoveTierList(tierListId);
        tierListStorage.deleteTierList(tierListId);
    }
}
