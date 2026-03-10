package app.TierListMakerUltimate.persistence.stubs;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierPersistenceStub implements TierPersistence {

    private final Map<Integer, Tier> tiers = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<Tier> getTiersForList(int tierListId) {
        List<Tier> result = new ArrayList<>();
        for (Tier tier : tiers.values()) {
            if (tier.getTierListId() == tierListId) {
                result.add(tier);
            }
        }
        return result;
    }

    @Override
    public Tier getTier(int tierId) {
        return tiers.get(tierId);
    }

    @Override
    public Tier insertTier(int tierListId, Tier currentTier) {
        int id = nextId++;
        Tier copy = new Tier(
                id,
                currentTier.getTierListId(),
                currentTier.getName(),
                currentTier.getColor(),
                currentTier.isUnranked()
        );
        tiers.put(id, copy);
        return copy;
    }

    @Override
    public Tier updateTier(Tier currentTier) {
        if (tiers.containsKey(currentTier.getId())) {
            Tier copy = new Tier(
                    currentTier.getId(),
                    currentTier.getTierListId(),
                    currentTier.getName(),
                    currentTier.getColor(),
                    currentTier.isUnranked()
            );
            tiers.put(currentTier.getId(), copy);
            return copy;
        }
        return null;
    }

    @Override
    public void deleteTier(int tierId) {
        tiers.remove(tierId);
    }
}
