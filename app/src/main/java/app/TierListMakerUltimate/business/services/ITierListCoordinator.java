package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.Tier;

public interface ITierListCoordinator {
    int addTierList(String name);

    void removeTierList(int tierListId);

    Tier getUrankedTier(int tierListId);
}
