package app.TierListMakerUltimate.persistence.system_data;

import java.util.List;

public class SeedTemplates {

    public record TierListRecord(String name, List<TierItemRecord> items) {
    }

    public record TierItemRecord(String localImagePath, String description) {
    }

    private static final List<TierItemRecord> POKEMON_STARTERS_ITEMS = List.of(
            new TierItemRecord("pokemon_bulbasaur", "Bulbasaur"),
            new TierItemRecord("pokemon_charmander", "Charmander"),
            new TierItemRecord("pokemon_squirtle", "Squirtle"),
            new TierItemRecord("pokemon_chikorita", "Chikorita"),
            new TierItemRecord("pokemon_cyndaquil", "Cyndaquil"),
            new TierItemRecord("pokemon_totodile", "Totodile"),
            new TierItemRecord("pokemon_treecko", "Treecko"),
            new TierItemRecord("pokemon_torchic", "Torchic"),
            new TierItemRecord("pokemon_mudkip", "Mudkip"),
            new TierItemRecord("pokemon_turtwig", "Turtwig"),
            new TierItemRecord("pokemon_chimchar", "Chimchar"),
            new TierItemRecord("pokemon_piplup", "Piplup")
    );

    public static final List<TierListRecord> SYSTEM_TEMPLATES = List.of(
            new TierListRecord("Pokemon Starters", POKEMON_STARTERS_ITEMS)
    );

}
