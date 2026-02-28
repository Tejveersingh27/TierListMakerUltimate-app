package app.TierListMakerUltimate.presentation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.TierList;

public class TierListBrowserAdapter extends RecyclerView.Adapter<TierListBrowserAdapter.TierListBrowserViewHolder> {
    private final List<TierList> tierLists;
    private final TierListBrowserActionListener listener;

    public TierListBrowserAdapter(List<TierList> tierLists, TierListBrowserActionListener listener) {
        this.tierLists = tierLists;
        this.listener = listener;
    }

    public void removeItem(int position) {
        tierLists.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(TierList tierList) {
        tierLists.add(tierList);
        notifyItemInserted(tierLists.size() - 1);
    }

    public void updateItem(int position, TierList updated) {
        tierLists.set(position, updated);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public TierListBrowserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tier_list, parent, false);
        return new TierListBrowserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TierListBrowserViewHolder holder, int position) {
        TierList tierList = tierLists.get(position);
        holder.tierListName.setText(tierList.getName());

        holder.editButton.setOnClickListener(v -> listener.onEditButtonClick(tierList));
        holder.deleteButton.setOnClickListener(v -> {
            int currentBoundPosition = holder.getBindingAdapterPosition();
            listener.onDeleteButtonClick(currentBoundPosition, tierList);
        });
    }

    @Override
    public int getItemCount() {
        return tierLists.size();
    }


    public static class TierListBrowserViewHolder extends RecyclerView.ViewHolder {
        TextView tierListName;
        ImageButton editButton;
        ImageButton deleteButton;

        public TierListBrowserViewHolder(@NonNull View itemView) {
            super(itemView);
            tierListName = itemView.findViewById(R.id.titleText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface TierListBrowserActionListener {
        void onEditButtonClick(TierList tierList);

        void onDeleteButtonClick(int position, TierList tierList);
    }


}
