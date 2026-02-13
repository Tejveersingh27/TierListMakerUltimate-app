package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.TierList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierListStub implements TierListPersistence {
    private static final Map<Integer, TierList> storage = new HashMap<>();

    public TierListStub() {
        storage.put(1, new TierList("TierList 1"));
        storage.put(2, new TierList("TierList 2"));
        storage.put(3, new TierList("TierList 3"));
    }

    @Override
    public List<TierList> getTierLists() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public TierList getTierListById(int tierListId) {
        return storage.get(tierListId);
    }

    @Override
    public int insertTierList(TierList currentTierList) {
        int newID = storage.size() + 1;
        currentTierList.setId(newID);
        storage.put(newID, currentTierList);

        return newID;
    }

    @Override
    public void updateTierList(TierList currentTierList) {
        storage.remove(currentTierList.getId());
        storage.put(currentTierList.getId(), currentTierList);
    }

    @Override
    public void deleteTierList(int tierListId) {
        storage.remove(tierListId);
    }
}