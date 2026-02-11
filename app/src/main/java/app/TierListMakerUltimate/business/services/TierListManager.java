package app.TierListMakerUltimate.business.services;
import app.TierListMakerUltimate.business.validation.ValidationException;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.business.validation.TierListValidator;
public class TierListManager
{
    private final TierListPersistence tierListStorage;
    private final TierListValidator validator;
    public TierListManager( TierListPersistence tierListStorage)
    {
        this.tierListStorage = tierListStorage; //Tier list database
        validator = new TierListValidator();
    }
    public TierListManager( TierListPersistence tierListStorage, TierListValidator validator)
    {
        if(tierListStorage == null)
        {
            throw new ValidationException("Tier List persistence required");
        }
        this.tierListStorage = tierListStorage;
        this.validator = validator;
    }
    public TierList createTierList(String name)
    {
        validator.validateCreateTierList(name);
        TierList newList = new TierList(name);
        int id = tierListStorage.insertTierList(newList);
        newList.setId(id);
        return newList;
    }
    public void removeTierList(int tierListId){ // Remove the tier List from database
        validator.validateRemoveTierList(tierListId);
        tierListStorage.deleteTierList(tierListId);
    }


}
