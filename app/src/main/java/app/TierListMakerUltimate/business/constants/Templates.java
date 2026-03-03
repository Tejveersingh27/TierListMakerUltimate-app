package app.TierListMakerUltimate.business.constants;

import java.util.List;

public class Templates {

    public record TierRecord(String name, String color) {
    }

    public record ItemRecord(String imageKey, String description) {
    }

    public record TierListRecord(String name, List<TierRecord> tiers,
                                 List<ItemRecord> initialItems) {
    }

    // Colors

    public static final String COLOR_S = "#FF7F7F";
    public static final String COLOR_A = "#FFBF7F";
    public static final String COLOR_B = "#FFFF7F";
    public static final String COLOR_C = "#7FFF7F";
    public static final String COLOR_D = "#7FBFFF";
    public static final String COLOR_F = "#7FBFFF";
    public static final String COLOR_UNRANKED = "#808080";

    // Lists

    public static final List<TierRecord> DEFAULT_TIERS = List.of(
            new TierRecord("S", COLOR_S),
            new TierRecord("A", COLOR_A),
            new TierRecord("B", COLOR_B),
            new TierRecord("C", COLOR_C),
            new TierRecord("D", COLOR_D),
            new TierRecord("F", COLOR_F)
    );
}
