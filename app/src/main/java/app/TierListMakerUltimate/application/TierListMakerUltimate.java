package app.TierListMakerUltimate.application;

// import java.beans.PersistenceDelegate;

import android.app.Application;

import app.TierListMakerUltimate.business.services.interfaces.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.interfaces.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.interfaces.ITierListManager;
import app.TierListMakerUltimate.business.services.interfaces.ITierManager;
import app.TierListMakerUltimate.business.services.implementations.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.implementations.TierListCoordinator;
import app.TierListMakerUltimate.business.services.implementations.TierListManager;
import app.TierListMakerUltimate.business.services.implementations.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;

import app.TierListMakerUltimate.persistence.interfaces.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.files.AndroidImageFilePersistence;
import app.TierListMakerUltimate.persistence.interfaces.TierItemPersistence;
import app.TierListMakerUltimate.persistence.interfaces.TierListPersistence;
import app.TierListMakerUltimate.persistence.interfaces.TierPersistence;
import app.TierListMakerUltimate.persistence.factory.PersistenceFactory;
import app.TierListMakerUltimate.persistence.utils.IUUIDGenerator;
import app.TierListMakerUltimate.persistence.utils.UUIDGenerator;

public class TierListMakerUltimate extends Application {

    // Business logic instances
    private ITierListCoordinator tierListCoordinator;
    private ITierListManager tierListManager;
    private ITierManager tierManager;
    private IItemPlacementManager itemPlacementManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // Change SQLITE to STUB here to change the persistence implementation
        PersistenceFactory.Implementations implementation = PersistenceFactory.Implementations.SQLITE;

        PersistenceFactory.Set persistence =
                (implementation == PersistenceFactory.Implementations.SQLITE)
                        ? PersistenceFactory.SQLite(this)
                        : PersistenceFactory.Stubs();

        // Storage instances
        TierListPersistence tierListStorage = persistence.tierLists;
        TierPersistence tierStorage = persistence.tiers;
        TierItemPersistence itemStorage = persistence.items;

        IUUIDGenerator uuidGenerator = new UUIDGenerator();
        ImageFilePersistence imageStorage = new AndroidImageFilePersistence(this, uuidGenerator);

        tierListManager = new TierListManager(tierListStorage, imageStorage, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        itemPlacementManager = new ItemPlacementManager(itemStorage, imageStorage, new ItemValidator());
        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager, itemPlacementManager);
    }

    public ITierListManager getTierListManager() {
        return tierListManager;
    }

    public ITierManager getTierManager() {
        return tierManager;
    }

    public IItemPlacementManager getItemPlacementManager() {
        return itemPlacementManager;
    }

    public ITierListCoordinator getTierListCoordinator() {
        return tierListCoordinator;
    }
    
}
