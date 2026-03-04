package app.TierListMakerUltimate.persistence.system_data;

import app.TierListMakerUltimate.models.SystemTemplate;
import app.TierListMakerUltimate.models.SystemTemplateItem;

import java.util.List;

public class SeedTemplates {

    private static final List<SystemTemplateItem> POKEMON_STARTERS_ITEMS = List.of(
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_bulbasaur", "Bulbasaur"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_charmander", "Charmander"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_squirtle", "Squirtle"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_chikorita", "Chikorita"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_cyndaquil", "Cyndaquil"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_totodile", "Totodile"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_treecko", "Treecko"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_torchic", "Torchic"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_mudkip", "Mudkip"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_turtwig", "Turtwig"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_chimchar", "Chimchar"),
            new SystemTemplateItem("android.resource://app.TierListMakerUltimate/drawable/pokemon_piplup", "Piplup")
    );

    public static final List<SystemTemplate> SYSTEM_TEMPLATES = List.of(
            new SystemTemplate("Pokemon Starters", POKEMON_STARTERS_ITEMS)
    );
}