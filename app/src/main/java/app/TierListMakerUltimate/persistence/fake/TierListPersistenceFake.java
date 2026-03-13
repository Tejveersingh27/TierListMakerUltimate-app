package app.TierListMakerUltimate.persistence.fake;

import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.interfaces.TierListPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierListPersistenceFake implements TierListPersistence {

    private final Map<Integer, TierList> tierLists = new HashMap<>();
    private int nextId = 1;


    @Override
    public List<TierList> getTierLists() {
        return new ArrayList<>(tierLists.values());
    }

    @Override
    public List<TierList> getTemplates() {
        return getListHelper(true);
    }

    @Override
    public List<TierList> getNonTemplateTierLists() {
        return getListHelper(false);
    }

    private List<TierList> getListHelper(boolean isTemplates) {
        List<TierList> list = new ArrayList<>();
        for (TierList tierList : tierLists.values()) {
            if (tierList.isTemplate() == isTemplates) {
                list.add(tierList);
            }
        }
        return list;
    }

    @Override
    public TierList getTierList(int tierListId) {
        return tierLists.get(tierListId);
    }

    @Override
    public TierList insertTierList(TierList currentTierList) {
        int id = nextId++;
        TierList copy = new TierList(id, currentTierList.getName(), currentTierList.getThumbnailPath(), currentTierList.isTemplate());
        tierLists.put(id, copy);
        return copy;
    }

    @Override
    public TierList updateTierList(TierList currentTierList) {
        if (tierLists.containsKey(currentTierList.getId())) {
            tierLists.put(currentTierList.getId(), currentTierList);
            return currentTierList;
        }
        return null;
    }

    @Override
    public void deleteTierList(int tierListId) {
        tierLists.remove(tierListId);
    }

}
