package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierList;

import java.io.InputStream;
import java.util.List;

public interface ITierListManager {
    TierList createTierList(String name, String thumbnailPath, InputStream thumbnailData) throws ValidationException, PersistenceException;

    TierList createTierList(String name, String thumbnailPath) throws ValidationException;

    TierList getTierList(int tierListId) throws ValidationException, NotFoundException;

    void removeTierList(int tierListId) throws ValidationException, NotFoundException;

    void updateTierList(TierList updatedTierList) throws ValidationException, NotFoundException;

    List<TierList> getAllTierLists() throws ValidationException;
}
