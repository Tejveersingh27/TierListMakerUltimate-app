package app.TierListMakerUltimate.business.constants;

import java.util.List;

public class DefaultTiers {

    public record TierRecord(String name, String color, boolean isUnranked) {
    }
    

    // Colors

    public static final String COLOR_S = "#FF7F7F";
    public static final String COLOR_A = "#FFBF7F";
    public static final String COLOR_B = "#FFFF7F";
    public static final String COLOR_C = "#7FFF7F";
    public static final String COLOR_D = "#7FBFFF";
    public static final String COLOR_F = "#7A0000";
    public static final String COLOR_UNRANKED = "#808080";

    // Standard Tier Blueprints

    public static final List<TierRecord> DEFAULT_TIERS = List.of(
            new TierRecord("S", COLOR_S, false),
            new TierRecord("A", COLOR_A, false),
            new TierRecord("B", COLOR_B, false),
            new TierRecord("C", COLOR_C, false),
            new TierRecord("D", COLOR_D, false),
            new TierRecord("F", COLOR_F, false),
            new TierRecord("Unranked", COLOR_UNRANKED, true)
    );

}
