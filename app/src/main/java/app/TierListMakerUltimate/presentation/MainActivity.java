package app.TierListMakerUltimate.presentation;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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
    // Variables
    private static final String TAG = "epic_games";     // Used for debugging

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

        int stierid = tierManager.createTier(TIER_LIST_ID, "S Tier", "#EF4343").getId();
        int atierid = tierManager.createTier(TIER_LIST_ID, "A Tier", "#FFBF7F").getId();
        int btierid = tierManager.createTier(TIER_LIST_ID, "B Tier", "#FFFF7F").getId();
        int ctierid = tierManager.createTier(TIER_LIST_ID, "C Tier", "#85E75D").getId();
        int dtierid = tierManager.createTier(TIER_LIST_ID, "D Tier", "#5DE7D9").getId();
        int etierid = tierManager.createTier(TIER_LIST_ID, "E Tier", "#104FDE").getId();
        int ftierid = tierManager.createTier(TIER_LIST_ID, "F Tier", "#E12FE4").getId();

        // Default items (My personal ranking) XOTWOD
        placementManager.createItem(R.drawable.hob, stierid, "Sample Item -2");
        placementManager.createItem(R.drawable.thursday, atierid, "Sample Item -1");
        placementManager.createItem(R.drawable.echoes, atierid, "Sample Item 0");
        placementManager.createItem(R.drawable.kissland, stierid, "Sample Item 1");
        placementManager.createItem(R.drawable.bbtm, btierid, "Sample Item 2");
        placementManager.createItem(R.drawable.starboy, btierid, "Sample Item 3");
        placementManager.createItem(R.drawable.mdm, atierid, "Sample Item 4");
        placementManager.createItem(R.drawable.afterhours, stierid, "Sample Item 5");
        placementManager.createItem(R.drawable.dawnfm, atierid, "Sample Item 6");
        placementManager.createItem(R.drawable.hut, stierid, "Sample Item 7");

        // Other placeholders to showcase the unranked items and lower tiers
        placementManager.createItem(R.drawable.placeholder, ftierid, "Sample Item 8");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 9");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 10");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 11");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 12");
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
            RecyclerView recycler = rootView.findViewWithTag("recycler" + tierId);
            setupRecycler(recycler, 4, images);
        }
    }

    private void createTier(Tier tierData)
    {
        int tierId = tierData.getId();

        // Find tier container
        LinearLayout tierContainer = findViewById(R.id.tier_container);

        // Inflate new tier and change color
        ViewGroup newTier = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.tier_layout, tierContainer, false);
        int color = Color.parseColor(tierData.getColor());
        newTier.setBackgroundColor(color);
        tierContainer.addView(newTier);

        // Set up recycler for having tier items
        RecyclerView recycler = (RecyclerView) newTier.getChildAt(1);
        setupRecycler(recycler, 4, new ArrayList<>());
        recycler.setTag("recycler" + tierId);

        // Apply tier name
        TextView text = (TextView) newTier.getChildAt(0);
        text.setText(tierData.getName());
    }

    private void setupRecycler(RecyclerView recycler, int spanCount, List<Integer> images)
    {
        recycler.setLayoutManager(new GridLayoutManager(this, spanCount));
        recycler.setAdapter(new RecycleAdapter(images));
    }
}
