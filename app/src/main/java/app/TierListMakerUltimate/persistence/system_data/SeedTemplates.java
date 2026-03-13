package app.TierListMakerUltimate.persistence.system_data;

import java.util.List;


/*
Seed Data for grader
 */

public class SeedTemplates {

    public record SystemTemplate(String name, String thumbnailPath,
                                 List<SystemTemplateItem> items,
                                 List<SystemTemplateTier> tiers) {
    }

    public record SystemTemplateItem(String name, String description, String imagePath) {
    }

    public record SystemTemplateTier(String label, String color, boolean isUnranked, int position) {
    }

    private static final List<SystemTemplateItem> POKEMON_STARTERS_ITEMS = List.of(
            new SystemTemplateItem("Bulbasaur", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_bulbasaur"),
            new SystemTemplateItem("Charmander", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_charmander"),
            new SystemTemplateItem("Squirtle", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_squirtle"),
            new SystemTemplateItem("Chikorita", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_chikorita"),
            new SystemTemplateItem("Cyndaquil", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_cyndaquil"),
            new SystemTemplateItem("Totodile", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_totodile"),
            new SystemTemplateItem("Treecko", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_treecko"),
            new SystemTemplateItem("Torchic", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_torchic"),
            new SystemTemplateItem("Mudkip", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_mudkip"),
            new SystemTemplateItem("Turtwig", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_turtwig"),
            new SystemTemplateItem("Chimchar", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_chimchar"),
            new SystemTemplateItem("Piplup", "", "android.resource://app.TierListMakerUltimate/drawable/pokemon_piplup")
    );

    private static final List<SystemTemplateItem> WEEKND_SONG_ITEMS = List.of(
            new SystemTemplateItem("House of Balloons", "", "android.resource://app.TierListMakerUltimate/drawable/hob"),
            new SystemTemplateItem("Thursday", "", "android.resource://app.TierListMakerUltimate/drawable/thursday"),
            new SystemTemplateItem("Echoes of Silence", "", "android.resource://app.TierListMakerUltimate/drawable/echoes"),
            new SystemTemplateItem("Kiss Land", "", "android.resource://app.TierListMakerUltimate/drawable/kissland"),
            new SystemTemplateItem("Beauty Behind the Madness", "", "android.resource://app.TierListMakerUltimate/drawable/bbtm"),
            new SystemTemplateItem("Starboy", "", "android.resource://app.TierListMakerUltimate/drawable/starboy"),
            new SystemTemplateItem("My Dear Melancholy", "", "android.resource://app.TierListMakerUltimate/drawable/mdm"),
            new SystemTemplateItem("After Hours", "", "android.resource://app.TierListMakerUltimate/drawable/afterhours"),
            new SystemTemplateItem("Dawn FM", "", "android.resource://app.TierListMakerUltimate/drawable/dawnfm"),
            new SystemTemplateItem("Hurry Up Tomorrow", "", "android.resource://app.TierListMakerUltimate/drawable/hut")
    );

    private static final List<SystemTemplateTier> POKEMON_TIERS = List.of(
            new SystemTemplateTier("Legendary", "#E91E63", false, 0),
            new SystemTemplateTier("Elite", "#FF7322", false, 1),
            new SystemTemplateTier("Strong", "#FFEB3B", false, 2),
            new SystemTemplateTier("Average", "#8BC34A", false, 3),
            new SystemTemplateTier("Weak", "#03A9F4", false, 4),
            new SystemTemplateTier("Unranked", "#808080", true, -1)
    );

    private static final List<SystemTemplateTier> WEEKND_TIERS = List.of(
            new SystemTemplateTier("Masterpiece", "#E91E63", false, 0),
            new SystemTemplateTier("Classic", "#FF7322", false, 1),
            new SystemTemplateTier("Great", "#FFEB3B", false, 2),
            new SystemTemplateTier("Decent", "#8BC34A", false, 3),
            new SystemTemplateTier("Skip", "#03A9F4", false, 4),
            new SystemTemplateTier("Unranked", "#808080", true, -1)
    );
    public static final List<SystemTemplate> SYSTEM_TEMPLATES = List.of(
            new SystemTemplate(
                    "Pokemon Starters",
                    "android.resource://app.TierListMakerUltimate/drawable/pokemon_mudkip",
                    POKEMON_STARTERS_ITEMS,
                    POKEMON_TIERS
            ),
            new SystemTemplate(
                    "Weeknd Albums",
                    "android.resource://app.TierListMakerUltimate/drawable/starboy",
                    WEEKND_SONG_ITEMS,
                    WEEKND_TIERS
            )
    );
}
