package app.TierListMakerUltimate.application;

import android.app.Application;

import app.TierListMakerUltimate.business.services.ISystemTemplateCoordinator;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.SystemTemplateCoordinator;
import app.TierListMakerUltimate.business.services.TierListCoordinator;
import app.TierListMakerUltimate.business.services.TierListManager;
import app.TierListMakerUltimate.business.services.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.persistence.ITierListSeedProvider;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;
import app.TierListMakerUltimate.persistence.system_data.SystemTemplateProvider;


public class TierListMakerUltimate extends Application {

    // Storage instances
    private TierListPersistence tierListStorage;
    private TierPersistence tierStorage;
    private TierItemPersistence itemStorage;
    private ITierListSeedProvider seedProvider;

    // Business logic instances
    private ITierListCoordinator tierListCoordinator;
    private ISystemTemplateCoordinator systemTemplateCoordinator;
    private TierListManager tierListManager;
    private TierManager tierManager;
    private ItemPlacementManager itemPlacementManager;


    @Override
    public void onCreate() {
        super.onCreate();

        if (isTestEnvironment()) {
            tierListStorage = new TierListPersistenceStub();
            tierStorage = new TierPersistenceStub();
            itemStorage = new TierItemPersistenceStub();
            seedProvider = new SystemTemplateProvider();
        } else {
            // Connect to real database
        }

        tierListManager = new TierListManager(tierListStorage, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        itemPlacementManager = new ItemPlacementManager(itemStorage, new ItemValidator());
        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager);
        systemTemplateCoordinator = new SystemTemplateCoordinator(tierListCoordinator, itemPlacementManager, seedProvider);

    }

    public TierListManager getTierListManager() {
        return tierListManager;
    }

    public TierManager getTierManager() {
        return tierManager;
    }

    public ItemPlacementManager getItemPlacementManager() {
        return itemPlacementManager;
    }

    public ITierListCoordinator getTierListCoordinator() {
        return tierListCoordinator;
    }

    public ISystemTemplateCoordinator getSystemTemplateCoordinator() {
        return systemTemplateCoordinator;
    }


    private boolean isTestEnvironment() {
        return true; // Need to add an environment variable for this
    }
}
