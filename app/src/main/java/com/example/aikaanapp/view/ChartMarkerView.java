package com.example.aikaanapp.view;

import android.content.Context;
import android.widget.TextView;

import com.example.aikaanapp.R;
import com.example.aikaanapp.adapters.ChartRVAdapter;
import com.example.aikaanapp.util.StringHelper;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class ChartMarkerView extends MarkerView {

    private TextView mContent;
    private MPPointF mOffset;
    private int mType;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        mContent = findViewById(R.id.tvContent);
    }

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ChartMarkerView(Context context, int layoutResource, int type) {
        super(context, layoutResource);

        mType = type;
        mContent = findViewById(R.id.tvContent);
    }


    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String value = "";

        switch (mType) {
            case ChartRVAdapter.BATTERY_LEVEL:
                value = StringHelper.formatPercentageNumber(e.getY());
                break;
            case ChartRVAdapter.BATTERY_TEMPERATURE:
                value = StringHelper.formatNumber(e.getY()) + " ºC";
                break;
            case ChartRVAdapter.BATTERY_VOLTAGE:
                value = StringHelper.formatNumber(e.getY()) + " V";
                break;
        }

        mContent.setText(value);

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() >> 1), -getHeight());
        }

        return mOffset;
    }
}