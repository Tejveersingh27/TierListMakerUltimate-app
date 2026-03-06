package app.TierListMakerUltimate.persistence;

import java.util.List;

import app.TierListMakerUltimate.models.SystemTemplate;

public interface ITierListSeedProvider {
    List<SystemTemplate> getTemplates();
}


