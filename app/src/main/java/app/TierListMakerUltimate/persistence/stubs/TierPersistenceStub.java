package app.TierListMakerUltimate.persistence.stubs;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierPersistenceStub implements TierPersistence {

    private final Map<Integer, Tier> tiers = new HashMap<>();
    private final Map<Integer, Integer> tierToListMapping = new HashMap<>();  // tierId -> tierListId
    private int nextId = 1;

    @Override
    public List<Tier> getTiersForList(int tierListId) {
        List<Tier> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : tierToListMapping.entrySet()) {
            if (entry.getValue() == tierListId) {
                Tier tier = tiers.get(entry.getKey());
                if (tier != null) {
                    result.add(tier);
                }
            }
        }
        return result;
    }

    @Override
    public Tier getTier(int tierId) {
        return tiers.get(tierId);
    }

    @Override
    public int insertTier(int tierListId, Tier currentTier) {
        int id = nextId++;
        Tier copy = new Tier(id, currentTier.getName(), currentTier.getColor());
        tiers.put(id, copy);
        tierToListMapping.put(id, tierListId);  // Track which list this tier belongs to
        return id;
    }

    @Override
    public void updateTier(Tier currentTier) {
        if (tiers.containsKey(currentTier.getId())) {
            tiers.put(currentTier.getId(), currentTier);
        }
    }

    @Override
    public void deleteTier(int tierId) {
        tiers.remove(tierId);
        tierToListMapping.remove(tierId);
    }
}