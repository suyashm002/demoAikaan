package com.example.aikaanapp.models.ui;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * ChartCard.
 */
public class ChartCard {

    private static final String TAG = "ChartCard";

    public int type;
    public String label;
    public int color;
    public List<Entry> entries;
    public double[] extras;

    public ChartCard(int type, String label, int color) {
        this.type = type;
        this.label = label;
        this.color = color;
        this.entries = new ArrayList<>();
        this.extras = null;
    }
}