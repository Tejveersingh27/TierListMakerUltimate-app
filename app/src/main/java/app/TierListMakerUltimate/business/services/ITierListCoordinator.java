package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.TierList;

public interface ITierListCoordinator {
    public TierList createDefaultTierListWithDefaults();

    public TierList duplicateTierList(TierList tierList);
    
}
