package com.example.aikaanapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aikaanapp.R;
import com.example.aikaanapp.models.ui.ChartCard;
import com.example.aikaanapp.util.DateUtils;
import com.example.aikaanapp.util.StringHelper;
import com.example.aikaanapp.view.ChartMarkerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChartRVAdapter extends RecyclerView.Adapter<ChartRVAdapter.DashboardViewHolder> {

    public static final int BATTERY_LEVEL = 1;
    public static final int BATTERY_TEMPERATURE = 2;
    public static final int BATTERY_VOLTAGE = 3;

    private ArrayList<ChartCard> mChartCards;

    private int mInterval;

    private Context mContext;

    static class DashboardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView label;
        TextView interval;
        LineChart chart;
        RelativeLayout extras;
        TextView min;
        TextView avg;
        TextView max;

        DashboardViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            label = itemView.findViewById(R.id.label);
            interval = itemView.findViewById(R.id.interval);
            chart = itemView.findViewById(R.id.chart);
            extras = itemView.findViewById(R.id.extra_details);
            min = itemView.findViewById(R.id.minValue);
            avg = itemView.findViewById(R.id.avgValue);
            max = itemView.findViewById(R.id.maxValue);
        }
    }

    public ChartRVAdapter(ArrayList<ChartCard> chartCards, int interval, Context context) {
        this.mChartCards = chartCards;
        this.mInterval = interval;
        this.mContext = context;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ChartRVAdapter.DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chart_card_view,
                parent,
                false
        );
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        ChartCard card = mChartCards.get(position);
        setup(holder, card);
        holder.chart.setData(loadData(card));
        holder.chart.invalidate();
        holder.label.setText(card.label);

        if (card.extras != null) {
            String value;
            if (card.type == BATTERY_TEMPERATURE) {
                value = "Min: " + StringHelper.formatNumber(card.extras[0]) + " ºC";
                holder.min.setText(value);
                value = mContext.getString(R.string.chart_average_title) +
                        ": " + StringHelper.formatNumber(card.extras[1]) + " ºC";
                holder.avg.setText(value);
                value = "Max: " + StringHelper.formatNumber(card.extras[2]) + " ºC";
                holder.max.setText(value);
            } else if (card.type == BATTERY_VOLTAGE) {
                value = "Min: " + StringHelper.formatNumber(card.extras[0]) + " V";
                holder.min.setText(value);
                value = mContext.getString(R.string.chart_average_title) +
                        ": " + StringHelper.formatNumber(card.extras[1]) + " V";
                holder.avg.setText(value);
                value = "Max: " + StringHelper.formatNumber(card.extras[2]) + " V";
                holder.max.setText(value);
            }
            holder.extras.setVisibility(View.VISIBLE);
        }

        if (mInterval == DateUtils.INTERVAL_24H) {
            holder.interval.setText("Last 24h");
        } else if (mInterval == DateUtils.INTERVAL_3DAYS) {
            holder.interval.setText("Last 3 days");
        } else if (mInterval == DateUtils.INTERVAL_5DAYS) {
            holder.interval.setText("Last 5 days");
        }
    }

    @Override
    public int getItemCount() {
        return mChartCards.size();
    }

    public void swap(ArrayList<ChartCard> list) {
        if (mChartCards != null) {
            mChartCards.clear();
            mChartCards.addAll(list);
        } else {
            mChartCards = list;
        }
        notifyDataSetChanged();
    }

    private LineData loadData(ChartCard card) {
        // add entries to dataset
        LineDataSet lineDataSet = new LineDataSet(card.entries, null);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setColor(card.color);
        lineDataSet.setCircleColor(card.color);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(card.color);

        return new LineData(lineDataSet);
    }

    private void setup(DashboardViewHolder holder, final ChartCard card) {

        holder.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DateUtils.convertMilliSecondsToFormattedDate((long) value);
            }
        });

        if (card.type == BATTERY_LEVEL) {
            holder.chart.getAxisLeft().setAxisMaximum(1f);
        }

        holder.chart.setExtraBottomOffset(5f);
        holder.chart.getAxisLeft().setDrawGridLines(false);
        holder.chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                switch (card.type) {
                    case BATTERY_LEVEL:
                        return StringHelper.formatPercentageNumber(value);
                    case BATTERY_TEMPERATURE:
                        return StringHelper.formatNumber(value) + " ºC";
                    case BATTERY_VOLTAGE:
                        return StringHelper.formatNumber(value) + " V";
                    default:
                        return String.valueOf(value);
                }
            }
        });
        holder.chart.getAxisRight().setDrawGridLines(false);
        holder.chart.getAxisRight().setDrawLabels(false);
        holder.chart.getXAxis().setDrawGridLines(false);
        holder.chart.getXAxis().setLabelCount(3);
        holder.chart.getXAxis().setGranularity(1f);

        holder.chart.getLegend().setEnabled(false);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setNoDataText(mContext.getString(R.string.chart_loading_data));

        IMarker marker = new ChartMarkerView(
                holder.itemView.getContext(), R.layout.item_marker, card.type
        );

        holder.chart.setMarker(marker);

        holder.chart.animateY(600, Easing.EaseInOutElastic);
    }
}