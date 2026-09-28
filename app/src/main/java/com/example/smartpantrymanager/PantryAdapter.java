package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    // Lets the screen decide what happens when a row is tapped
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
        void onItemLongClick(PantryItem item);
    }

    private List<PantryItem> items;
    private final OnItemClickListener listener;

    public PantryAdapter(List<PantryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    // Replace the list and redraw
    public void setItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // Holds the views of one row so they can be reused
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
        }
    }

    // Creates a new empty row from item_pantry.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    // Fills a row with the data of the item at this position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(formatQuantity(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            holder.tvExpiry.setText("No expiry date");
        } else {
            holder.tvExpiry.setText("Expires: " + expiry);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Shows 3 instead of 3.0, but keeps 2.5 as 2.5
    private String formatQuantity(double q) {
        if (q == (long) q) {
            return String.valueOf((long) q);
        }
        return String.valueOf(q);
    }
}