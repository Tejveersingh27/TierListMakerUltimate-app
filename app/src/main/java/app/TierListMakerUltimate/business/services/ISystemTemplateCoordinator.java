package app.TierListMakerUltimate.business.services;

/**
 * Coordinates the loading and saving of system templates.
 * Gets templates from the seed provider and creates
 * TierLists with their items placed in the unranked tier.
 */
public interface ISystemTemplateCoordinator {
    /**
     * Loads and saves all system templates from the seed provider.
     * Each template is created as a default TierList with its items placed in the unranked tier.
     */
    void loadSystemTemplates();
}
