package app.TierListMakerUltimate.application;

import android.app.Application;

import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ISystemTemplateCoordinator;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.SystemTemplateCoordinator;
import app.TierListMakerUltimate.business.services.TierListCoordinator;
import app.TierListMakerUltimate.business.services.TierListManager;
import app.TierListMakerUltimate.business.services.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.persistence.ITierListSeedProvider;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.ImageFilePersistenceReal;
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
    private ImageFilePersistence imageStorage;
    private ITierListSeedProvider seedProvider;

    // Business logic instances
    private ITierListCoordinator tierListCoordinator;
    private ISystemTemplateCoordinator systemTemplateCoordinator;
    private ITierListManager tierListManager;
    private ITierManager tierManager;
    private IItemPlacementManager itemPlacementManager;

    @Override
    public void onCreate() {
        super.onCreate();

        if (isTestEnvironment()) {
            tierListStorage = new TierListPersistenceStub();
            tierStorage = new TierPersistenceStub();
            itemStorage = new TierItemPersistenceStub();
            seedProvider = new SystemTemplateProvider();
        } else {
            // TODO: Connect to real database
        }

        imageStorage = new ImageFilePersistenceReal(this);


        tierListManager = new TierListManager(tierListStorage, imageStorage, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        itemPlacementManager = new ItemPlacementManager(itemStorage, new ItemValidator());
        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager);
        systemTemplateCoordinator = new SystemTemplateCoordinator(tierListCoordinator, itemPlacementManager, seedProvider);

        systemTemplateCoordinator.loadSystemTemplates();
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
    

    private boolean isTestEnvironment() {
        return true; // TODO: Need to add an environment variable for this
    }
}

