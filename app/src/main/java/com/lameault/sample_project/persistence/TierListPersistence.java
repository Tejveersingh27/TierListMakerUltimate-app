package com.lameault.sample_project.persistence;

import com.lameault.sample_project.models.TierList;

import java.util.List;

public interface TierListPersistence {
    List<TierList> getTierLists();

    TierList getTierListById(int tierListId);

    int insertTierList(TierList currentTierList); // Returns ID

    void updateTierList(TierList currentTierList);

    void deleteTierList(int tierListId);
}