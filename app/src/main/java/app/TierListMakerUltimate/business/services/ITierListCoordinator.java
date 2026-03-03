package app.TierListMakerUltimate.business.services;

public interface ITierListCoordinator {
    int addTierList(String name);

    void removeTierList(int tierListId);
}
