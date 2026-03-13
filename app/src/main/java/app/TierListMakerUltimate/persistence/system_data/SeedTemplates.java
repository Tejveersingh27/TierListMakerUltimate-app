package app.TierListMakerUltimate.persistence.system_data;

import java.util.List;


/*
Seed Data for grader
 */

public class SeedTemplates {

    public record SystemTemplate(String name, String thumbnailPath,
                                 List<SystemTemplateItem> items) {
    }

    public record SystemTemplateItem(String name, String description, String imagePath) {
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

    public static final List<SystemTemplate> SYSTEM_TEMPLATES = List.of(
            new SystemTemplate(
                    "Pokemon Starters",
                    "android.resource://app.TierListMakerUltimate/drawable/pokemon_mudkip",
                    POKEMON_STARTERS_ITEMS
            ),
            new SystemTemplate(
                    "Weeknd Albums",
                    "android.resource://app.TierListMakerUltimate/drawable/starboy",
                    WEEKND_SONG_ITEMS
            )
    );
}
