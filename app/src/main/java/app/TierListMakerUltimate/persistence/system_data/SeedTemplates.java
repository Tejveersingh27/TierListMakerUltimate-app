package app.TierListMakerUltimate.persistence.system_data;

import app.TierListMakerUltimate.models.SystemTemplate;
import app.TierListMakerUltimate.models.SystemTemplateItem;

import java.util.List;

public class SeedTemplates {

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
            new SystemTemplateItem("hob", "", "android.resource://app.TierListMakerUltimate/drawable/hob"),
            new SystemTemplateItem("thursday", "", "android.resource://app.TierListMakerUltimate/drawable/thursday"),
            new SystemTemplateItem("echoes", "", "android.resource://app.TierListMakerUltimate/drawable/echoes"),
            new SystemTemplateItem("kissland", "", "android.resource://app.TierListMakerUltimate/drawable/kissland"),
            new SystemTemplateItem("bbtm", "", "android.resource://app.TierListMakerUltimate/drawable/bbtm"),
            new SystemTemplateItem("starboy", "", "android.resource://app.TierListMakerUltimate/drawable/starboy"),
            new SystemTemplateItem("mdm", "", "android.resource://app.TierListMakerUltimate/drawable/mdm"),
            new SystemTemplateItem("afterhours", "", "android.resource://app.TierListMakerUltimate/drawable/afterhours"),
            new SystemTemplateItem("dawnfm", "", "android.resource://app.TierListMakerUltimate/drawable/dawnfm"),
            new SystemTemplateItem("hut", "", "android.resource://app.TierListMakerUltimate/drawable/hut")
    );


    public static final List<SystemTemplate> SYSTEM_TEMPLATES = List.of(
            new SystemTemplate(
                    "Pokemon Starters",
                    "android.resource://app.TierListMakerUltimate/drawable/pokemon_mudkip",
                    POKEMON_STARTERS_ITEMS
            ),
            new SystemTemplate(
                    "Weeeknd Songs",
                    "android.resource://app.TierListMakerUltimate/drawable/starboy",
                    WEEKND_SONG_ITEMS
            )
    );
}
