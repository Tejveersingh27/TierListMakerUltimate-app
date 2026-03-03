package app.TierListMakerUltimate.business.services;

import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;

public class TierListFactory {

    public TierList createWeekndAlbumTemplate() {
        return new TierList("The Weeknd Albums (System)");
    }

    public List<Tier> getStandardTiers() {
        List<Tier> tiers = new ArrayList<>();
        tiers.add(new Tier("S", "#FF7F7F"));
        tiers.add(new Tier("A", "#FFBF7F"));
        tiers.add(new Tier("B", "#FFFF7F"));
        tiers.add(new Tier("C", "#7FFF7F"));
        tiers.add(new Tier("D", "#7FBFFF"));
        return tiers;
    }

    public static class TemplateItem {
        public final int imageResource;
        public final String description;

        public TemplateItem(int imageResource, String description) {
            this.imageResource = imageResource;
            this.description = description;
        }
    }

    public List<TemplateItem> getWeekndAlbumItems(int[] resources) {
        String[] titles = {
            "After Hours", "Dawn FM", "Starboy", "Echoes of Silence",
            "Kiss Land", "My Dear Melancholy,", "Beauty Behind the Madness",
            "Thursday", "House of Balloons"
        };
        
        List<TemplateItem> items = new ArrayList<>();
        for (int i = 0; i < resources.length && i < titles.length; i++) {
            items.add(new TemplateItem(resources[i], titles[i]));
        }
        return items;
    }
}
