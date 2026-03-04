package app.TierListMakerUltimate.business.services;

import java.util.List;

import app.TierListMakerUltimate.models.SystemTemplate;
import app.TierListMakerUltimate.models.SystemTemplateItem;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.ITierListSeedProvider;

public class SystemTemplateCoordinator implements ISystemTemplateCoordinator {
    private final ITierListCoordinator tierListCoordinator;
    private final ItemPlacementManager itemPlacementManager;
    private final ITierListSeedProvider seedProvider;

    public SystemTemplateCoordinator(ITierListCoordinator tierListCoordinator,
                                     ItemPlacementManager itemPlacementManager,
                                     ITierListSeedProvider seedProvider) {
        this.tierListCoordinator = tierListCoordinator;
        this.itemPlacementManager = itemPlacementManager;
        this.seedProvider = seedProvider;
    }

    @Override
    public void loadSystemTemplates() {
        List<SystemTemplate> templates = seedProvider.getTemplates();
        for (SystemTemplate template : templates) {
            processSystemTemplate(template);
        }
    }

    private void processSystemTemplate(SystemTemplate template) {
        TierList tierList = tierListCoordinator.addTierList(template.getName());
        Tier unrankedTier = tierListCoordinator.getUrankedTier(tierList.getId());
        for (SystemTemplateItem item : template.getItems()) {
            itemPlacementManager.createItem(item.getImagePath(), unrankedTier.getId(), item.getName());
        }
    }
}
