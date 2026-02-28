package app.TierListMakerUltimate.presentation;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // Static Variables
    public static int tierlistID = 1;                   // The value of the current tierlist ID. If 1 then default data is loaded.
    private static final String TAG = "epic_games";     // Used for debugging

    // Instance Variables
    //private TierListManager activeList;
    private TierManager tierManager;
    private TierAdapter tierAdapter;
    private ItemPlacementManager placementManager;
    private HashMap<String, View> menuItems;
    private int unplacedItemsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TierPersistence tierStorage = new TierPersistenceStub();
        TierItemPersistence itemStorage = new TierItemPersistenceStub();
        tierManager = new TierManager(tierStorage, new TierValidator());
        placementManager = new ItemPlacementManager(itemStorage, new ItemValidator());
        menuItems = new HashMap<>();

        getMenuItems();

        setupRecyclerView();
        initializeDefaultData();

        // Set up unplaced items section
        menuItems.get("unplacedItems").setTag("recycler" + unplacedItemsId);

        refreshList();
    }

    private void getMenuItems()
    {
        menuItems.put("mainLayout", findViewById(R.id.mainLayout));
        menuItems.put("tierListTitle", findViewById(R.id.tierListTitle));
        menuItems.put("plusIcon", findViewById(R.id.plusIcon));
        menuItems.put("tierSettings", findViewById(R.id.tierSettings));
        menuItems.put("tierContainer", findViewById(R.id.tierContainer));
        menuItems.put("unplacedItems", findViewById(R.id.unplacedItems));
    }

    private void setupRecyclerView() {
        tierAdapter = new TierAdapter(new TierAdapter.TierActions() {
            @Override
            public void openTierSettings(Tier tier) {
                openTierEditor(tier);
            }

            @Override
            public void onDeleteTier(Tier tier) {
                confirmDeleteTier(tier);
            }

            @Override
            public void onItemDropped(int itemId, int targetTierId) {
                moveItem(itemId, targetTierId);
            }

            public void moveTier(int direction) {
                shiftTier(direction);
            }
        });

        RecyclerView tierContainer = (RecyclerView)menuItems.get("tierContainer");
        tierContainer.setLayoutManager(new LinearLayoutManager(this));
        tierContainer.setAdapter(tierAdapter);
    }

    // Implement these later
    private void openTierEditor(Tier tier) {
        return;
    }

    private void confirmDeleteTier(Tier tier) {
        return;
    }

    private void moveItem(int itemId, int targetTierId) {
        try {
            placementManager.moveItemToTier(itemId, targetTierId);
            refreshList();
        } catch (Exception e) {
            Toast.makeText(this, "Error moving item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void shiftTier(int direction) {
        return;
    }

    private void initializeDefaultData() {
        // Make default tiers
        unplacedItemsId = tierManager.createTier(tierlistID, "unranked", "#7A7A7A").getId();

        int stierid = tierManager.createTier(tierlistID, "S Tier", "#EF4343").getId();
        int atierid = tierManager.createTier(tierlistID, "A Tier", "#FFBF7F").getId();
        int btierid = tierManager.createTier(tierlistID, "B Tier", "#FFFF7F").getId();
        int ctierid = tierManager.createTier(tierlistID, "C Tier", "#85E75D").getId();
        int dtierid = tierManager.createTier(tierlistID, "D Tier", "#5DE7D9").getId();
        int etierid = tierManager.createTier(tierlistID, "E Tier", "#104FDE").getId();
        int ftierid = tierManager.createTier(tierlistID, "F Tier", "#E12FE4").getId();

        // Default items (My personal ranking) XOTWOD
        placementManager.createItem(R.drawable.hob, stierid, "Sample Item -2").setId(-2);
        placementManager.createItem(R.drawable.thursday, atierid, "Sample Item -1").setId(-1);
        placementManager.createItem(R.drawable.echoes, atierid, "Sample Item 0").setId(0);
        placementManager.createItem(R.drawable.kissland, stierid, "Sample Item 1").setId(1);
        placementManager.createItem(R.drawable.bbtm, btierid, "Sample Item 2").setId(2);
        placementManager.createItem(R.drawable.starboy, btierid, "Sample Item 3").setId(3);
        placementManager.createItem(R.drawable.mdm, atierid, "Sample Item 4").setId(4);
        placementManager.createItem(R.drawable.afterhours, stierid, "Sample Item 5").setId(5);
        placementManager.createItem(R.drawable.dawnfm, atierid, "Sample Item 6").setId(6);
        placementManager.createItem(R.drawable.hut, stierid, "Sample Item 7").setId(7);

        // Other placeholders to showcase the unranked items and lower tiers
        placementManager.createItem(R.drawable.placeholder, ftierid, "Sample Item 8");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 9");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 10");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 11");
        placementManager.createItem(R.drawable.placeholder, unplacedItemsId, "Sample Item 12");
    }

    private void refreshList() {
        // Get all tiers for this tier list
        List<Tier> tiers = tierManager.getTiersForList(tierlistID);

        // Get items for each tier
        Map<Integer, List<TierItem>> tierItemsMap = new HashMap<>();
        for (Tier tier : tiers) {
            List<TierItem> items = placementManager.getItemsForTier(tier.getId());
            tierItemsMap.put(tier.getId(), items);
        }

        // Update adapter
        tierAdapter.setTiers(tiers, tierItemsMap);
    }

    /*private void refreshList()
    {
        // Get all tiers for this tier list
        List<Tier> tiers = tierManager.getTiersForList(tierlistID);

        for (Tier tier : tiers) {
            int tierId = tier.getId();
            View physicalTier = menuItems.get("tierContainer").findViewWithTag("tier" + tierId);

            // if the tier isn't already on screen, then make it
            if (physicalTier == null && tierId != unplacedItemsId)
                createTier(tier);

            // Load up images of items.
            List<TierItem> items = placementManager.getItemsForTier(tierId);

            // Set recycler to have images inside
            RecyclerView recycler = menuItems.get("tierContainer").findViewWithTag("recycler" + tierId);
            setupRecycler(recycler, 4, items);
        }
    }*/

    private void createTier(Tier tierData)
    {
        int tierId = tierData.getId();

        // Find tier container
        LinearLayout tierContainer = findViewById(R.id.tierContainer);

        // Inflate new tier and change color
        ViewGroup newTier = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.tier_layout, tierContainer, false);
        int color = Color.parseColor(tierData.getColor());
        newTier.setBackgroundColor(color);
        tierContainer.addView(newTier);

        // Set up recycler for having tier items
        RecyclerView recycler = newTier.findViewById(R.id.itemHolder);
        setupRecycler(recycler, 4, new ArrayList<>());
        recycler.setTag("recycler" + tierId);

        recycler.setOnDragListener((v, event) -> {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                ((LinearLayout) v).addView(view);
                view.setVisibility(View.VISIBLE);
            }
            return true;
        });

        // Apply tier name
        TextView text = newTier.findViewById(R.id.tierTitle);
        text.setText(tierData.getName());
    }

    private void setupRecycler(RecyclerView recycler, int spanCount, List<TierItem> items)
    {
        recycler.setLayoutManager(new GridLayoutManager(this, spanCount));
        recycler.setAdapter(new TierItemAdapter());
    }
}
