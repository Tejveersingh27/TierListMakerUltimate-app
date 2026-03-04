package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public interface ITierListCoordinator {
    TierList addTierList(String name);

    void removeTierList(int tierListId);

    Tier getUrankedTier(int tierListId);
}
