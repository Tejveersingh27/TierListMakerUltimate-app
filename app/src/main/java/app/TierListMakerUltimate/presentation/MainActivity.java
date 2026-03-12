package app.TierListMakerUltimate.presentation;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.presentation.fragments.TierEditorFragment;
import app.TierListMakerUltimate.presentation.fragments.TierItemCreationFragment;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TierItemCreationFragment.TierItemCreationFragmentActionListener, TierEditorFragment.TierEditorFragmentActionListener {
    // Static Variables
    private static int tierlistID = 0;                   // The value of the current tierlist ID. If 1 then default data is loaded.
    private static final String TAG = "epic_games";     // Used for debugging

    // Instance Variables
    private ITierManager tierManager;
    private TierAdapter tierAdapter;
    private TierItemCreationFragment tierItemCreationFragment;
    private ImageHelper imageHelper = new ImageHelper(this);

    private TierItemAdapter unrankedAdapter;
    private IItemPlacementManager placementManager;
    private HashMap<String, View> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();

        tierManager = app.getTierManager();
        placementManager = app.getItemPlacementManager();

        menuItems = new HashMap<>();

        getMenuItems();

        Intent intent = getIntent();
        tierlistID = intent.getIntExtra(INTENT_TIER_LIST_ID, 0);

        // id of 0 means default fallback tierlist is generated
        if (tierlistID == 0)
            initializeDefaultData();

        setupRecyclerView();
        setupAddButton();

        refreshList();
    }

    // Puts any relevant menu items in a hashmap for easy access.
    private void getMenuItems() {
        menuItems.put("mainLayout", findViewById(R.id.mainLayout));
        menuItems.put("tierListTitle", findViewById(R.id.tierListTitle));
        menuItems.put("plusIcon", findViewById(R.id.plusIcon));
        menuItems.put("tierSettings", findViewById(R.id.tierSettings));
        menuItems.put("tierContainer", findViewById(R.id.tierContainer));
        menuItems.put("unrankedContainer", findViewById(R.id.unrankedContainer));
        menuItems.put("plusIconItem", findViewById(R.id.plusIconItem));
        menuItems.put("unrankedItems", findViewById(R.id.itemHolderUnranked));
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
        }, imageHelper);

        unrankedAdapter = new TierItemAdapter(imageHelper);

        menuItems.get("unrankedContainer").setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setAlpha(0.7f);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setAlpha(1.0f);
                    return true;
                case DragEvent.ACTION_DROP:
                    String itemIdStr = event.getClipData().getItemAt(0).getText().toString();
                    int itemId = Integer.parseInt(itemIdStr);
                    moveItem(itemId, tierManager.getUnrankedTierForList(tierlistID).getId());
                    return true;
            }
            return false;
        });

        RecyclerView tierContainer = (RecyclerView) menuItems.get("tierContainer");
        tierContainer.setLayoutManager(new LinearLayoutManager(this));
        tierContainer.setAdapter(tierAdapter);

        RecyclerView unrankedItems = (RecyclerView) menuItems.get("unrankedItems");
        unrankedItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        unrankedItems.setAdapter(unrankedAdapter);
    }

    private void setupTierItemCreationFragment() {
        tierItemCreationFragment = TierItemCreationFragment.newInstance(tierlistID);
        tierItemCreationFragment.setUpListener(this);
        tierItemCreationFragment.show(getSupportFragmentManager(), "");
    }

    // Implement these later

    // Opens the tier settings menu
    private void openTierEditor(Tier tier) {
        TierEditorFragment fragment = TierEditorFragment.newInstance(tier.getId());
        fragment.setUpListener(this);
        fragment.show(getSupportFragmentManager(), "");
    }

    // Should delete tier and move all items in that tier to unranked
    private void confirmDeleteTier(Tier tier) {
        return;
    }


    // Open tier item creation fragment
    private void setupAddButton() {
        menuItems.get("plusIconItem").setOnClickListener(v -> {
            setupTierItemCreationFragment();
        });
    }


    // Save tier item
    @Override
    public void onTierItemCreatedSuccessfully() {
        refreshList();
    }

    @Override
    public void onTierEditorFragmentEditSuccess() {
        refreshList();
    }

    @Override
    public void onTierEditorFragmentDeleteSuccess() {
        refreshList();
    }


    // Moves an item to a target tier
    private void moveItem(int itemId, int targetTierId) {
        try {
            placementManager.moveItemToTier(itemId, targetTierId);
            refreshList();
        } catch (Exception e) {
            Toast.makeText(this, "Error moving item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    // This function should make the tier physically move up or down on the list
    // based on the direction parameter
    private void shiftTier(int direction) {
        return;
    }

    // Initializes some default data in the case that we don't load have an appropriate tierlistId.
    private void initializeDefaultData() {
        // Make default tiers
        int unplacedItemsId = tierManager.createTier(0, "unranked", "#7A7A7A", true).getId();

        int stierid = tierManager.createTier(0, "S Tier", "#EF4343").getId();
        int atierid = tierManager.createTier(0, "A Tier", "#FFBF7F").getId();
        int btierid = tierManager.createTier(0, "B Tier", "#FFFF7F").getId();
        int ctierid = tierManager.createTier(0, "C Tier", "#85E75D").getId();
        int dtierid = tierManager.createTier(0, "D Tier", "#5DE7D9").getId();
        int etierid = tierManager.createTier(0, "E Tier", "#104FDE").getId();
        int ftierid = tierManager.createTier(0, "F Tier", "#E12FE4").getId();

        // Default items (My personal ranking) XOTWOD
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/hob", -2, stierid, "", "Sample Item -2");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/thursday", -1, atierid, "", "Sample Item -1");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/echoes", 0, atierid, "", "Sample Item 0");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/kissland", 1, stierid, "", "Sample Item 1");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/bbtm", 2, btierid, "", "Sample Item 2");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/starboy", 3, btierid, "", "Sample Item 3");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/mdm", 4, atierid, "", "Sample Item 4");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/afterhours", 5, stierid, "", "Sample Item 5");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/dawnfm", 6, atierid, "", "Sample Item 6");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/hut", 7, stierid, "", "Sample Item 7");

        // Other placeholders to showcase the unranked items and lower tiers
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/placeholder", "", ftierid, "Sample Item 8");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/placeholder", "", unplacedItemsId, "Sample Item 9");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/placeholder", "", unplacedItemsId, "Sample Item 10");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/placeholder", "", unplacedItemsId, "Sample Item 11");
        placementManager.createItem("android.resource://app.TierListMakerUltimate/drawable/placeholder", "", unplacedItemsId, "Sample Item 12");
    }

    // Refreshes the tierlist to reflect item movements.
    private void refreshList() {
        // Get all tiers for this tier list
        List<Tier> tiers = tierManager.getTiersForList(tierlistID);
        int unrankedID = -1;
        int unrankedIndex = -1;
        int i = -1;
        // Get items for each tier
        Map<Integer, List<TierItem>> tierItemsMap = new HashMap<>();
        for (Tier tier : tiers) {
            i += 1;
            List<TierItem> items = placementManager.getItemsForTier(tier.getId());
            if (tier.isUnranked()) {
                unrankedAdapter.setItems(items);
                unrankedID = tier.getId();
                unrankedIndex = i;
            } else
                tierItemsMap.put(tier.getId(), items);
        }

        if (unrankedID != -1) {
            tierItemsMap.remove(unrankedID);
            tiers.remove(unrankedIndex);
        }
        // Update adapter
        tierAdapter.setTiers(tiers, tierItemsMap);
    }
}
