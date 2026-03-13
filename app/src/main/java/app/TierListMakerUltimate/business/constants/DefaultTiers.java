package app.TierListMakerUltimate.business.constants;

public enum DefaultTiers {
    S("S", "#E91E63", false),
    A("A", "#FF7322", false),
    B("B", "#FFEB3B", false),
    C("C", "#8BC34A", false),
    D("D", "#03A9F4", false),
    F("F", "#ED0925", false),
    UNRANKED("Unranked", "#808080", true);

    public static final String DEFAULT_NAME = "New Tier";
    public static final String DEFAULT_COLOR = "#808080";

    public final String label;
    public final String color;
    public final boolean isUnranked;

    DefaultTiers(String label, String color, boolean isUnranked) {
        this.label = label;
        this.color = color;
        this.isUnranked = isUnranked;
    }
}