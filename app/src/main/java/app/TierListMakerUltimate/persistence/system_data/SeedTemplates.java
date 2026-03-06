package app.TierListMakerUltimate.persistence.system_data;

import app.TierListMakerUltimate.models.SystemTemplate;
import app.TierListMakerUltimate.models.SystemTemplateItem;

import java.util.List;

public class SeedTemplates {

    private static final List<SystemTemplateItem> POKEMON_STARTERS_ITEMS = List.of(
            new SystemTemplateItem("Bulbasaur", "android.resource://app.TierListMakerUltimate/drawable/pokemon_bulbasaur"),
            new SystemTemplateItem("Charmander", "android.resource://app.TierListMakerUltimate/drawable/pokemon_charmander"),
            new SystemTemplateItem("Squirtle", "android.resource://app.TierListMakerUltimate/drawable/pokemon_squirtle"),
            new SystemTemplateItem("Chikorita", "android.resource://app.TierListMakerUltimate/drawable/pokemon_chikorita"),
            new SystemTemplateItem("Cyndaquil", "android.resource://app.TierListMakerUltimate/drawable/pokemon_cyndaquil"),
            new SystemTemplateItem("Totodile", "android.resource://app.TierListMakerUltimate/drawable/pokemon_totodile"),
            new SystemTemplateItem("Treecko", "android.resource://app.TierListMakerUltimate/drawable/pokemon_treecko"),
            new SystemTemplateItem("Torchic", "android.resource://app.TierListMakerUltimate/drawable/pokemon_torchic"),
            new SystemTemplateItem("Mudkip", "android.resource://app.TierListMakerUltimate/drawable/pokemon_mudkip"),
            new SystemTemplateItem("Turtwig", "android.resource://app.TierListMakerUltimate/drawable/pokemon_turtwig"),
            new SystemTemplateItem("Chimchar", "android.resource://app.TierListMakerUltimate/drawable/pokemon_chimchar"),
            new SystemTemplateItem("Piplup", "android.resource://app.TierListMakerUltimate/drawable/pokemon_piplup")
    );

    public static final List<SystemTemplate> SYSTEM_TEMPLATES = List.of(
            new SystemTemplate(
                    "Pokemon Starters",
                    "android.resource://app.TierListMakerUltimate/drawable/pokemon_bulbasaur",
                    POKEMON_STARTERS_ITEMS
            )
    );
}
