package app.TierListMakerUltimate.business.constants;

public enum DefaultTiers {
    S("S", "#FF7F7F", false),
    A("A", "#FFBF7F", false),
    B("B", "#FFFF7F", false),
    C("C", "#7FFF7F", false),
    D("D", "#7FBFFF", false),
    F("F", "#7A0000", false),
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