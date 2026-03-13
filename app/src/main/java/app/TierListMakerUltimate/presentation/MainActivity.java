package app.TierListMakerUltimate.presentation;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exception.BusinessException;
import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.presentation.controllers.TierItemDragController;
import app.TierListMakerUltimate.presentation.fragments.TierEditorFragment;
import app.TierListMakerUltimate.presentation.fragments.TierItemCreationFragment;
import app.TierListMakerUltimate.presentation.fragments.TierItemEditFragment;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TierItemCreationFragment.TierItemCreationFragmentActionListener, TierItemEditFragment.TierItemEditFragmentActionListener, TierEditorFragment.TierEditorFragmentActionListener, TierItemDragController.DragDropListener, TierAdapter.TierActions, TierAdapter.TierItemActions {
    private int tierlistID;
    private String tierlistName;
    private int itemToEdit = -1;

    // Instance Variables
    private ITierManager tierManager;
    private TierAdapter tierAdapter;
    private ImageHelper imageHelper;
    private TierItemAdapter unrankedAdapter;
    private IItemPlacementManager placementManager;

    // Views
    TextView tierListTitle;
    ImageButton addTierItemButton;
    ImageButton tierConfigButton;
    RecyclerView tierRecycler;
    RecyclerView unrankedItemsRecycler;
    ImageButton addTierButton;
    ImageButton shareTemplateButton;

    // Gesture Detector to handle double tapping items
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageHelper = new ImageHelper(this);
        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();

        tierManager = app.getTierManager();
        placementManager = app.getItemPlacementManager();


        Intent intent = getIntent();
        tierlistID = intent.getIntExtra(INTENT_TIER_LIST_ID, 0);
        tierlistName = intent.getStringExtra(INTENT_TIER_LIST_NAME);
        bindViews();
        tierListTitle.setText(tierlistName);

        setupGestureDetector();
        setupTiersRecycler();
        setupUnrankedRecycler();
        setupAddItemButton();
        setupAddTierButton();
        refreshList();
    }

    // Puts any relevant menu items in a hashmap for easy access.
    private void bindViews() {
        tierListTitle = findViewById(R.id.tierListTitle);
        addTierItemButton = findViewById(R.id.plusIconItem);
        addTierButton = findViewById(R.id.plusIcon);
        tierConfigButton = findViewById(R.id.tierSettings);
        tierRecycler = findViewById(R.id.tierContainer);
        unrankedItemsRecycler = findViewById(R.id.itemHolderUnranked);
        shareTemplateButton = findViewById(R.id.shareTemplate);
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                openItemEdit(itemToEdit);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
    }

    private void setupTiersRecycler() {
        tierAdapter = new TierAdapter(this, this, imageHelper);
        tierRecycler.setLayoutManager(new LinearLayoutManager(this));
        tierRecycler.setAdapter(tierAdapter);

    }

    private void setupUnrankedRecycler() {
        unrankedAdapter = new TierItemAdapter(imageHelper, this);

        TierItemDragController unrankedDragController = new TierItemDragController(this, tierManager.getUnrankedTierForList(tierlistID).getId());

        unrankedItemsRecycler.setOnDragListener(unrankedDragController);
        unrankedItemsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        unrankedItemsRecycler.setAdapter(unrankedAdapter);
    }

    // Opens the tier settings menu
    private void openTierEditor(Tier tier) {
        TierEditorFragment fragment = TierEditorFragment.newInstance(tier.getId());
        fragment.setUpListener(this);
        showSingleDialog(fragment, FRAGMENT_TIER_EDITOR);
    }

    // Opens the tier item edit fragment
    private void openItemEdit(int itemId) {
        TierItemEditFragment fragment = TierItemEditFragment.newInstance(itemId);
        fragment.setUpListener(this);
        showSingleDialog(fragment, FRAGMENT_TIER_ITEM_EDITOR);
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
            return;
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

    // Moves an item to a target tier
    private void moveItem(int itemId, int targetTierId) {
        try {
            placementManager.moveItemToTier(itemId, targetTierId);
            refreshList();
        } catch (BusinessException e) {
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
        // Set items for unranked
        int unrankedId = tierManager.getUnrankedTierForList(tierlistID).getId();
        unrankedAdapter.setItems(placementManager.getItemsForTier(unrankedId));

        // Set items for non-unranked
        List<Tier> rankedTiers = tierManager.getRankedTiersForList(tierlistID);
        Map<Integer, List<TierItem>> tierItemsMap = new HashMap<>();
        for (Tier tier : rankedTiers) {
            tierItemsMap.put(tier.getId(), placementManager.getItemsForTier(tier.getId()));
        }

        tierAdapter.setTiers(rankedTiers, tierItemsMap);

    }


    // Tier Adapter Overrides
    @Override
    public void openTierSettings(Tier tier) {
        openTierEditor(tier);
    }

    @Override
    public void onItemDroppedTierOnTier(int itemId, int targetTierId) {
        moveItem(itemId, targetTierId);
    }

    @Override
    public void moveTier(int direction) {
        shiftTier(direction);
    }

    // TierItem Adapter Overrides
    @Override
    public void onItemDblClick(int itemId, MotionEvent event) {
        itemToEdit = itemId;
        gestureDetector.onTouchEvent(event);
    }

    // Tier Item Creation Fragment Overrides
    @Override
    public void onTierItemCreatedSuccessfully() {
        refreshList();
    }

    // Tier Item Editor Fragment Overrides
    @Override
    public void onTierItemEditedSuccessfully() {
        refreshList();
    }
    @Override
    public void onTierItemDeletedSuccessfully() {
        refreshList();
    }

    // Tier Editor Fragment Overrides
    @Override
    public void onTierEditorFragmentEditSuccess() {
        refreshList();
    }

    @Override
    public void onTierEditorFragmentDeleteSuccess() {
        refreshList();
    }

    // Drag Controller Overrides
    @Override
    public void onItemDropped(int itemId, int targetTierId) {
        moveItem(itemId, targetTierId);
    }

}
