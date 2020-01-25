package com.example.aikaanapp.layouts;

import android.content.Context;
import android.util.AttributeSet;

import com.example.aikaanapp.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class MainTabLayout extends TabLayout {

    public MainTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MainTabLayout(Context context) {
        super(context);
    }

    public MainTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Creates the tabs for the layout.
     */
    public void createTabs() {
        addTab(R.drawable.ic_launcher_background, R.string.title_fragment_home);
        addTab(R.drawable.ic_launcher_background, R.string.title_fragment_device);
        addTab(R.drawable.ic_launcher_background, R.string.title_fragment_stats);
        // addTab(R.drawable.ic_history_white_24dp, R.string.title_fragment_history);
        // addTab(R.drawable.ic_information_white_24dp, R.string.title_fragment_about);
    }

    /**
     * Adds a new tab to the layout provided the icon and string description resources.
     *
     * @param iconId Icon Id resource
     * @param contentDescriptionId Content Description Id resource
     */
    private void addTab(@DrawableRes int iconId, @StringRes int contentDescriptionId) {
        addTab(newTab()
                .setIcon(iconId)
                .setContentDescription(contentDescriptionId));
    }
}