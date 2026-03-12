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
import app.TierListMakerUltimate.presentation.controllers.TierItemDragController;
import app.TierListMakerUltimate.presentation.fragments.TierEditorFragment;
import app.TierListMakerUltimate.presentation.fragments.TierItemCreationFragment;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TierItemCreationFragment.TierItemCreationFragmentActionListener, TierEditorFragment.TierEditorFragmentActionListener, TierItemDragController.DragDropListener {
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

        TierItemDragController unrankedDragController = new TierItemDragController(this, tierManager.getUnrankedTierForList(tierlistID).getId());

        unrankedItemsRecycler.setOnDragListener(unrankedDragController);


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

    @Override
    public void onItemDropped(int itemId, int targetTierId) {
        moveItem(itemId, targetTierId);
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
