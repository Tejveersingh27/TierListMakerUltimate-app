package app.TierListMakerUltimate.presentation;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.business.services.TierListManager;
import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Vars for main page go here
    private static final String TAG = "epic_games";
    private TierListManager activeList;
    private TierManager tierManager;
    private ItemPlacementManager placementManager;

    // Only using 1 tier list for iteration 1
    private static final int TIER_LIST_ID = 1;

    private int unplacedItemsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TierPersistence tierStorage = new TierPersistenceStub();
        TierItemPersistence itemStorage = new TierItemPersistenceStub();
        tierManager = new TierManager(tierStorage, new TierValidator());
        placementManager = new ItemPlacementManager(itemStorage, new ItemValidator());

        initializeDefaultData();

        // Set up unplaced items section
        RecyclerView unplacedItems = findViewById(R.id.unplaced_items);
        unplacedItems.setTag("recycler" + unplacedItemsId);

        refreshList();
    }

    private void initializeDefaultData() {
        // Make default tiers
        unplacedItemsId = tierManager.createTier(TIER_LIST_ID, "unranked", "#7A7A7A").getId();

        tierManager.createTier(TIER_LIST_ID, "S Tier", "#EF4343");
        tierManager.createTier(TIER_LIST_ID, "A Tier", "#FFBF7F");
        tierManager.createTier(TIER_LIST_ID, "B Tier", "#FFFF7F");

        // Default items XOTWOD
        placementManager.createItem(R.drawable.kissland, unplacedItemsId, "Sample Item 1");
        placementManager.createItem(R.drawable.bbtm, unplacedItemsId, "Sample Item 2");
        placementManager.createItem(R.drawable.starboy, unplacedItemsId, "Sample Item 3");
        placementManager.createItem(R.drawable.mdm, unplacedItemsId, "Sample Item 4");
        placementManager.createItem(R.drawable.afterhours, unplacedItemsId, "Sample Item 5");
        placementManager.createItem(R.drawable.dawnfm, unplacedItemsId, "Sample Item 6");
        placementManager.createItem(R.drawable.hut, unplacedItemsId, "Sample Item 7");
    }

    private void refreshList()
    {
        // Get all tiers for this tier list
        List<Tier> tiers = tierManager.getTiersForList(TIER_LIST_ID);
        View rootView = this.getWindow().getDecorView().findViewById(android.R.id.content);

        for (Tier tier : tiers) {
            int tierId = tier.getId();
            View physicalTier = rootView.findViewWithTag("tier" + tierId);

            // if the tier isn't already on screen, then make it
            if (physicalTier == null && tierId != unplacedItemsId)
                createTier(tier);

            // Load up images of items.
            List<TierItem> items = placementManager.getItemsForTier(tierId);
            List<Integer> images = new ArrayList<>();

            for (TierItem item: items)
                images.add(item.getImagePath());

            // Set recycler to have images inside
            //RecyclerView recycler = rootView.findViewWithTag("recycler" + tierId);
            //setupRecycler(recycler, 4, images);
        }
    }

    private void createTier(Tier tierData)
    {
        int tierId = tierData.getId();

        // Find tier container
        LinearLayout tierContainer = findViewById(R.id.tier_container);

        // Inflate new tier and change color
        View newTier = LayoutInflater.from(this).inflate(R.layout.tier_layout, tierContainer);
        newTier.setTag("tier" + tierId);
        newTier.setId(View.generateViewId());
        int color = Color.parseColor(tierData.getColor());
        newTier.setBackgroundColor(color);

        // Set up recycler for having tier items
        RecyclerView recycler = newTier.findViewById(R.id.recycler0);
        setupRecycler(recycler, 4, new ArrayList<>());
        recycler.setTag("recycler" + tierId);
        recycler.setId(View.generateViewId());

        // Apply tier name
        TextView text = newTier.findViewById(R.id.tierTitle0);
        text.setText(tierData.getName());
        text.setTag("title" + tierId);
        text.setId(View.generateViewId());
    }

    private void setupRecycler(RecyclerView recycler, int spanCount, List<Integer> images)
    {
        recycler.setLayoutManager(new GridLayoutManager(this, spanCount));
        recycler.setAdapter(new RecycleAdapter(images));
    }
}
