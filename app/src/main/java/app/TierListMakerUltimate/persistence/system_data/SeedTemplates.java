package app.TierListMakerUltimate.persistence.system_data;

import app.TierListMakerUltimate.models.SystemTemplate;
import app.TierListMakerUltimate.models.SystemTemplateItem;

import java.util.List;

public class SeedTemplates {

    private static final List<SystemTemplateItem> POKEMON_STARTERS_ITEMS = List.of(
            new SystemTemplateItem("pokemon_bulbasaur", "Bulbasaur"),
            new SystemTemplateItem("pokemon_charmander", "Charmander"),
            new SystemTemplateItem("pokemon_squirtle", "Squirtle"),
            new SystemTemplateItem("pokemon_chikorita", "Chikorita"),
            new SystemTemplateItem("pokemon_cyndaquil", "Cyndaquil"),
            new SystemTemplateItem("pokemon_totodile", "Totodile"),
            new SystemTemplateItem("pokemon_treecko", "Treecko"),
            new SystemTemplateItem("pokemon_torchic", "Torchic"),
            new SystemTemplateItem("pokemon_mudkip", "Mudkip"),
            new SystemTemplateItem("pokemon_turtwig", "Turtwig"),
            new SystemTemplateItem("pokemon_chimchar", "Chimchar"),
            new SystemTemplateItem("pokemon_piplup", "Piplup")
    );

    public static final List<SystemTemplate> SYSTEM_TEMPLATES = List.of(
            new SystemTemplate("Pokemon Starters", POKEMON_STARTERS_ITEMS)
    );
}