package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public interface ITierListCoordinator {
    TierList addTierList(String name) throws ValidationException;

    void removeTierList(int tierListId) throws ValidationException;

    Tier getUrankedTier(int tierListId) throws ValidationException;
}
