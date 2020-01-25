package com.example.aikaanapp.models.ui;

import android.graphics.Color;

import androidx.annotation.DrawableRes;

public class BatteryCard {
    public int icon;
    public String label;
    public String value;
    public int indicator;

    public BatteryCard(@DrawableRes int icon, String title, String value) {
        this.icon = icon;
        this.label = title;
        this.value = value;
        this.indicator = Color.GREEN;
    }

    public BatteryCard(@DrawableRes int icon, String title, String value, int indicator) {
        this.icon = icon;
        this.label = title;
        this.value = value;
        this.indicator = indicator;
    }
}