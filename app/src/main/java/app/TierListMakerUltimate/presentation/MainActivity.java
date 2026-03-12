package app.TierListMakerUltimate.presentation;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.widget.ImageButton;
import android.widget.TextView;
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
    private static String tierlistName;             // The name of the current tierlist.
    private static final String TAG = "epic_games";     // Used for debugging

    // Instance Variables
    private ITierManager tierManager;
    private TierAdapter tierAdapter;
    private ImageHelper imageHelper = new ImageHelper(this);

    private TierItemAdapter unrankedAdapter;
    private IItemPlacementManager placementManager;

    TextView tierListTitle;
    ImageButton addTierItemButton;
    ImageButton tierConfigButton;
    RecyclerView tierRecycler;
    RecyclerView unrankedItemsRecycler;
    ImageButton addTierButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();

        tierManager = app.getTierManager();
        placementManager = app.getItemPlacementManager();


        getMenuItems();

        Intent intent = getIntent();
        tierlistID = intent.getIntExtra(INTENT_TIER_LIST_ID, 0);
        tierlistName = intent.getStringExtra(INTENT_TIER_LIST_NAME);

        tierListTitle.setText(tierlistName);

        setupRecyclerView();
        setupAddItemButton();
        setupAddTierButton();
        refreshList();
    }

    // Puts any relevant menu items in a hashmap for easy access.
    private void getMenuItems() {
        tierListTitle = findViewById(R.id.tierListTitle);
        addTierItemButton = findViewById(R.id.plusIconItem);
        addTierButton = findViewById(R.id.plusIcon);
        tierConfigButton = findViewById(R.id.tierSettings);
        tierRecycler = findViewById(R.id.tierContainer);
        unrankedItemsRecycler = findViewById(R.id.itemHolderUnranked);
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

        unrankedItemsRecycler.setOnDragListener((v, event) -> {
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


        tierRecycler.setLayoutManager(new LinearLayoutManager(this));
        tierRecycler.setAdapter(tierAdapter);

        unrankedItemsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        unrankedItemsRecycler.setAdapter(unrankedAdapter);
    }

    // Implement these later

    // Opens the tier settings menu
    private void openTierEditor(Tier tier) {
        TierEditorFragment fragment = TierEditorFragment.newInstance(tier.getId());
        fragment.setUpListener(this);
        showSingleDialog(fragment, FRAGMENT_TIER_EDITOR);
    }

    // Should delete tier and move all items in that tier to unranked
    private void confirmDeleteTier(Tier tier) {
        return;
    }


    // Open tier item creation fragment
    private void setupAddItemButton() {
        addTierItemButton.setOnClickListener(v -> {
            TierItemCreationFragment fragment = TierItemCreationFragment.newInstance(tierlistID);
            fragment.setUpListener(this);
            showSingleDialog(fragment, FRAGMENT_TIER_ITEM_CREATION);
        });
    }

    private void showSingleDialog(androidx.fragment.app.DialogFragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            return; // It's already on screen, do nothing!
        }
        fragment.show(getSupportFragmentManager(), tag);
    }

    // Add new default tier
    private void setupAddTierButton() {
        addTierButton.setOnClickListener(v -> {
            tierManager.createDefaultTier(tierlistID);
            refreshList();
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


    }

    // Refreshes the tierlist to reflect item movements.
    private void refreshList() {

        List<Tier> tiers = tierManager.getTiersForList(tierlistID);

        // Set items for unranked
        int unrankedId = tierManager.getUnrankedTierForList(tierlistID).getId();
        unrankedAdapter.setItems(placementManager.getItemsForTier(unrankedId));

        // Set items for non-unranked
        Map<Integer, List<TierItem>> tierItemsMap = new HashMap<>();
        for (Tier tier : tiers) {
            if (tier.getId() != unrankedId) {
                tierItemsMap.put(tier.getId(), placementManager.getItemsForTier(tier.getId()));
            }
        }

        tierAdapter.setTiers(tiers, tierItemsMap);

    }

}
