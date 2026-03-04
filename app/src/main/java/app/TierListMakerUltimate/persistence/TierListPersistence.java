package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.TierList;

import java.util.List;

public interface TierListPersistence {
    List<TierList> getTierLists();

    TierList getTierListById(int tierListId);

    TierList insertTierList(TierList currentTierList); // Returns ID

    TierList updateTierList(TierList currentTierList);


    void deleteTierList(int tierListId);

    List<TierList> getAllTierLists();
}