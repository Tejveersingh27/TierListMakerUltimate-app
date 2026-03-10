package app.TierListMakerUltimate.persistence.system_data;

import java.util.List;


import app.TierListMakerUltimate.models.SystemTemplate;

public class SystemTemplateProvider implements ITierListSeedProvider {


    @Override
    public List<SystemTemplate> getTemplates() {
        return SeedTemplates.SYSTEM_TEMPLATES;
    }
}
