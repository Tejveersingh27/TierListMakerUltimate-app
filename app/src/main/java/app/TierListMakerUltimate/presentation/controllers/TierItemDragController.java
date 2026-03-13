package app.TierListMakerUltimate.presentation.controllers;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.NOT_SELECTED_ALPHA;
import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.SELECTED_ALPHA;

import android.view.DragEvent;
import android.view.View;

public class TierItemDragController implements View.OnDragListener {

    private DragDropListener listener;
    private int targetTierId;

    public TierItemDragController(DragDropListener listener, int targetTierId) {
        this.listener = listener;
        this.targetTierId = targetTierId;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                view.setAlpha(SELECTED_ALPHA);
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                view.setAlpha(NOT_SELECTED_ALPHA);
                return true;
            case DragEvent.ACTION_DROP:
                String itemIdStr = dragEvent.getClipData().getItemAt(0).getText().toString();
                int itemId = Integer.parseInt(itemIdStr);
                listener.onItemDropped(itemId, targetTierId);
                return true;
        }
        return false;
    }

    public interface DragDropListener {
        void onItemDropped(int itemId, int targetTierId);
    }
}
