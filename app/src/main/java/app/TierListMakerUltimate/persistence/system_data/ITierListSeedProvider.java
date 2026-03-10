package app.TierListMakerUltimate.persistence.system_data;

import java.util.List;

import app.TierListMakerUltimate.models.SystemTemplate;

/**
 * Provides system templates for TierList creation.
 */
public interface ITierListSeedProvider {
    /**
     * Returns all system templates.
     */
    List<SystemTemplate> getTemplates();
}


