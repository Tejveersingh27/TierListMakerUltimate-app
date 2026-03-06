package app.TierListMakerUltimate.business.services;

import java.io.InputStream;

import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public interface ITierListCoordinator {
    TierList addTierList(String name, String thumbnailPath, InputStream thumbnailData) throws ValidationException, PersistenceException;

    TierList addTierList(String name, String thumbnailPath) throws ValidationException;

    void removeTierList(int tierListId) throws ValidationException;

    Tier getUrankedTier(int tierListId) throws ValidationException;
}
