package com.example.aikaanapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aikaanapp.R;
import com.example.aikaanapp.models.ui.BatteryCard;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BatteryRVAdapter extends RecyclerView.Adapter<BatteryRVAdapter.DashboardViewHolder> {

    static class DashboardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public ImageView icon;
        public TextView label;
        public TextView value;
        public View indicator;

        DashboardViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            icon = itemView.findViewById(R.id.icon);
            label = itemView.findViewById(R.id.label);
            value = itemView.findViewById(R.id.value);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }

    private ArrayList<BatteryCard> mBatteryCards;

    public BatteryRVAdapter(ArrayList<BatteryCard> batteryCards) {
        this.mBatteryCards = batteryCards;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.battery_item_card_view,
                viewGroup,
                false
        );
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardViewHolder viewHolder, int i) {
        viewHolder.icon.setImageResource(mBatteryCards.get(i).icon);
        viewHolder.label.setText(mBatteryCards.get(i).label);
        viewHolder.value.setText(mBatteryCards.get(i).value);
        viewHolder.indicator.setBackgroundColor(mBatteryCards.get(i).indicator);
    }

    @Override
    public int getItemCount() {
        return mBatteryCards.size();
    }

    public void swap(ArrayList<BatteryCard> list) {
        if (mBatteryCards != null) {
            mBatteryCards.clear();
            mBatteryCards.addAll(list);
        } else {
            mBatteryCards = list;
        }
        notifyDataSetChanged();
    }
}