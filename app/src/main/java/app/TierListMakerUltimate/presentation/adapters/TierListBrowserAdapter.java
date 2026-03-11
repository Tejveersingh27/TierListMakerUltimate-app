package app.TierListMakerUltimate.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.TierList;

public class TierListBrowserAdapter extends RecyclerView.Adapter<TierListBrowserAdapter.TierListBrowserViewHolder> {
    private final List<TierList> tierLists;
    private final TierListBrowserItemActionListener listener;

    public TierListBrowserAdapter(List<TierList> tierLists, TierListBrowserItemActionListener listener) {
        this.tierLists = tierLists;
        this.listener = listener;
    }

    public void updateData(List<TierList> newList) {
        this.tierLists.clear();
        this.tierLists.addAll(newList);
        notifyDataSetChanged();
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
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteButtonClick(tierList));
        holder.configButton.setOnClickListener(v -> listener.onConfigButtonClick(tierList));
    }

    @Override
    public int getItemCount() {
        return tierLists.size();
    }


    public static class TierListBrowserViewHolder extends RecyclerView.ViewHolder {
        TextView tierListName;
        ImageButton editButton;
        ImageButton configButton;
        ImageButton deleteButton;

        public TierListBrowserViewHolder(@NonNull View itemView) {
            super(itemView);
            tierListName = itemView.findViewById(R.id.titleText);
            editButton = itemView.findViewById(R.id.editButton);
            configButton = itemView.findViewById(R.id.configButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface TierListBrowserItemActionListener {
        void onEditButtonClick(TierList tierList);

        void onConfigButtonClick(TierList tierList);

        void onDeleteButtonClick(TierList tierList);
    }


}
