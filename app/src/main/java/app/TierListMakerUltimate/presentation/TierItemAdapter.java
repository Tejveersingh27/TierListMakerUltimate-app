package app.TierListMakerUltimate.presentation;

import android.content.ClipData;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.TierItem;

import java.util.ArrayList;
import java.util.List;

public class TierItemAdapter extends RecyclerView.Adapter<TierItemAdapter.TierItemViewHolder> {
    private List<TierItem> items;

    public TierItemAdapter() {
        items = new ArrayList<>();
    }

    public void setItems(List<TierItem> items) {
        this.items = (items != null ? items : new ArrayList<>());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TierItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tier_image, parent, false);
        return new TierItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TierItemViewHolder holder, int position) {
        TierItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TierItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public TierItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tier_image);
        }

        public void bind(TierItem item) {
            int image = item.getImagePath();

            imageView.setImageResource(image);

            // Set up long click listener to start drag
            itemView.setOnLongClickListener(v -> {
                ClipData data = ClipData.newPlainText("item_id", String.valueOf(item.getId()));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            });
        }
    }
}