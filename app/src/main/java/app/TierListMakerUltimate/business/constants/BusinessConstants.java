package app.TierListMakerUltimate.business.constants;

public class BusinessConstants {
    // TierItem validation
    public static final int MAX_LENGTH_DESCRIPTION = 100;
    public static final String ERROR_ITEM_NOT_FOUND = "Tier Item not found with ID: ";
    public static final String ERROR_DESCRIPTION_NULL = "Description cannot be null";
    public static final String ERROR_DESCRIPTION_TOO_LONG = "Description should be less than " + MAX_LENGTH_DESCRIPTION + " characters";
    public static final String ERROR_ITEM_ID_REQUIRED = "Item Id is required.";
    public static final String ERROR_IMAGE_PATH_REQUIRED = "A valid image path is required";

    // Tier validation
    public static final int MAX_LABEL_LENGTH = 60;
    public static final String COLOR_REGEX = "^#[0-9A-Fa-f]{6}$";
    public static final String ERROR_LABEL_REQUIRED = "Label is required.";
    public static final String ERROR_LABEL_TOO_LONG = "Label must be " + MAX_LABEL_LENGTH + " characters or less.";
    public static final String ERROR_COLOR_INVALID = "Color must be a valid hex code.";
    public static final String ERROR_UNRANKED_EDIT = "Unranked tiers cannot be edited.";
    public static final String ERROR_TIER_ID_REQUIRED = "Tier Id is required.";
    public static final String ERROR_TIER_NOT_FOUND = "Tier not found with ID: ";

    // TierList validation
    public static final int MAX_NAME_LENGTH = 50;
    public static final String ERROR_NAME_REQUIRED = "Valid name is Required";
    public static final String ERROR_NAME_TOO_LONG = "Name must be " + MAX_NAME_LENGTH + " characters or less";
    public static final String ERROR_TIER_LIST_ID_REQUIRED = "TierList Id is required";
    public static final String ERROR_TIER_LIST_NOT_FOUND = "TierList not found with ID: ";
    public static final String ERROR_THUMBNAIL_PATH_REQUIRED = "Valid Thumbnail path is required";


    // Initialization
    public static final String ERROR_PERSISTENCE_VALIDATOR_NULL = "Persistence and Validator cannot be null";
    public static final String ERROR_MANAGERS_NULL = "Managers cannot be null";
}
