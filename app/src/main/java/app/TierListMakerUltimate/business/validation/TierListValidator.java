package app.TierListMakerUltimate.business.validation;

public class TierListValidator
{

    public void validateCreateTierList(String name)
    {
        if(name == null || name.trim().isEmpty())
        {
            throw new ValidationException("Valid name is Required");
        }
    }
    public void validateRemoveTierList(int tierListId)
    {
        if(tierListId <=0)
        {
            throw new ValidationException("TierList Id is required");
        }
    }
}
