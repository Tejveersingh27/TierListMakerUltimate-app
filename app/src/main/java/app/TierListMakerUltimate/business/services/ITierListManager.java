package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierList;

import java.util.List;

public interface ITierListManager {
    TierList createTierList(String name) throws ValidationException;

    TierList getTierList(int tierListId) throws ValidationException;

    void removeTierList(int tierListId) throws ValidationException;

    void updateTierList(TierList updatedTierList) throws ValidationException;

    List<TierList> getAllTierLists() throws ValidationException;
}
