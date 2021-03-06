package com.example.aikaanapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.aikaanapp.tasks.DeleteSessionsTask;
import com.example.aikaanapp.tasks.DeleteUsagesTask;
import com.example.aikaanapp.util.SettingsUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import static com.example.aikaanapp.util.LogUtils.logI;
import static com.example.aikaanapp.util.LogUtils.makeLogTag;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = makeLogTag(SettingsActivity.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        public SettingsFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final String versionName = BuildConfig.DEBUG ?
                    BuildConfig.VERSION_NAME + " (Debug)" :
                    BuildConfig.VERSION_NAME;

            findPreference(SettingsUtils.PREF_APP_VERSION).setSummary(versionName);

            bindPreferenceSummaryToValue(findPreference(SettingsUtils.PREF_DATA_HISTORY));
            bindPreferenceSummaryToValue(findPreference(SettingsUtils.PREF_UPLOAD_RATE));
            bindPreferenceSummaryToValue(findPreference(SettingsUtils.PREF_TEMPERATURE_RATE));
            bindPreferenceSummaryToValue(findPreference(SettingsUtils.PREF_TEMPERATURE_WARNING));
            bindPreferenceSummaryToValue(findPreference(SettingsUtils.PREF_TEMPERATURE_HIGH));
            bindPreferenceSummaryToValue(findPreference(SettingsUtils.PREF_NOTIFICATIONS_PRIORITY));

            SettingsUtils.registerOnSharedPreferenceChangeListener(getActivity(), this);
        }

        @Override
        public void onDestroy() {
            SettingsUtils.unregisterOnSharedPreferenceChangeListener(getActivity(), this);
            super.onDestroy();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            final Context context = getActivity().getApplicationContext();
            final AikaanApplication app = (AikaanApplication) getActivity().getApplication();
            final Preference preference = findPreference(key);

            switch (key) {
                case SettingsUtils.PREF_SAMPLING_SCREEN:
                    // Restart GreenHub Service with new settings
                    logI(TAG, "Restarting GreenHub Service because of preference changes");
                    app.stopAikaanService();
                    app.startAikaanService();

                    break;
                case SettingsUtils.PREF_DATA_HISTORY:
                    bindPreferenceSummaryToValue(preference);
                    // Delete old data history
                    final int interval = SettingsUtils.fetchDataHistoryInterval(context);
                    new DeleteUsagesTask().execute(interval);
                    new DeleteSessionsTask().execute(interval);
                    break;
                case SettingsUtils.PREF_UPLOAD_RATE:
                    bindPreferenceSummaryToValue(preference);
                    break;
                /*
                case SettingsUtils.PREF_POWER_INDICATOR:
                    if (SettingsUtils.isPowerIndicatorShown(context)) {
                        Notifier.startStatusBar(context);
                        app.startStatusBarUpdater();
                    } else {
                        Notifier.closeStatusBar();
                        app.stopStatusBarUpdater();
                    }
                    Answers.getInstance().logCustom(new CustomEvent("Preference Change")
                            .putCustomAttribute(
                                    "Power Indicator",
                                    String.valueOf(SettingsUtils.isPowerIndicatorShown(context))
                            ));
                    break;
                */
                case SettingsUtils.PREF_TEMPERATURE_WARNING:
                    bindPreferenceSummaryToValue(preference);
                    break;
                case SettingsUtils.PREF_TEMPERATURE_HIGH:
                    bindPreferenceSummaryToValue(preference);
                    break;
                case SettingsUtils.PREF_TEMPERATURE_RATE:
                    bindPreferenceSummaryToValue(preference);
                    break;
                case SettingsUtils.PREF_NOTIFICATIONS_PRIORITY:
                    bindPreferenceSummaryToValue(preference);
                    break;
                case SettingsUtils.PREF_REMAINING_TIME:

                    break;
            }
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            String stringValue = PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), "");

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else if (preference instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference) preference;
                stringValue = stringValue.replaceFirst("^0+(?!$)", "");

                editTextPreference.setText(stringValue);
                preference.setSummary(stringValue.replaceFirst("^0+(?!$)", ""));
            }
        }
    }
}