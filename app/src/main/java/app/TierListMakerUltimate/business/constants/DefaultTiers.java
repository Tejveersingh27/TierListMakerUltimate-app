package app.TierListMakerUltimate.business.constants;

public enum DefaultTiers {
    S("S", "#E91E63", false, 0),
    A("A", "#FF7322", false, 1),
    B("B", "#FFEB3B", false, 2),
    C("C", "#8BC34A", false, 3),
    D("D", "#03A9F4", false, 4),
    E("E", "#3F51B5", false, 5),
    F("F", "#ED0925", false, 6),
    UNRANKED("Unranked", "#808080", true, -1);

    public static final String DEFAULT_NAME = "New Tier";
    public static final String DEFAULT_COLOR = "#808080";

    public final String label;
    public final String color;
    public final boolean isUnranked;
    public final int position;

    DefaultTiers(String label, String color, boolean isUnranked, int position) {
        this.label = label;
        this.color = color;
        this.isUnranked = isUnranked;
        this.position = position;
    }
}