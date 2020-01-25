package com.example.aikaanapp.fragments;
 import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.aikaanapp.MainActivity;
import com.example.aikaanapp.R;
import com.example.aikaanapp.adapters.ChartRVAdapter;
import com.example.aikaanapp.events.RefreshChartEvent;
import com.example.aikaanapp.models.data.BatteryUsage;
import com.example.aikaanapp.models.ui.ChartCard;
import com.example.aikaanapp.util.DateUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import androidx.annotation.NonNull;
 import androidx.fragment.app.Fragment;
 import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.RealmResults;

import static com.example.aikaanapp.util.LogUtils.makeLogTag;

/**
 * StatisticsFragment.
 */
public class StatisticsFragment extends Fragment {

    private static final String TAG = makeLogTag(StatisticsFragment.class);

    private MainActivity mActivity;

    private RecyclerView mRecyclerView;

    private ChartRVAdapter mAdapter;

    private ArrayList<ChartCard> mChartCards;

    private int mSelectedInterval;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        mActivity = (MainActivity) getActivity();

        mRecyclerView = view.findViewById(R.id.rv);
        mAdapter = null;

        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());

        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setHasFixedSize(true);

        final BottomNavigationView bottomNavigationView =
                view.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_24h:
                                mSelectedInterval = DateUtils.INTERVAL_24H;
                                loadData(DateUtils.INTERVAL_24H);
                                return true;
                            case R.id.action_3days:
                                mSelectedInterval = DateUtils.INTERVAL_3DAYS;
                                loadData(DateUtils.INTERVAL_3DAYS);
                                return true;
                            case R.id.action_5days:
                                mSelectedInterval = DateUtils.INTERVAL_5DAYS;
                                loadData(DateUtils.INTERVAL_5DAYS);
                                return true;
                        }
                        return false;
                    }
                });

        mSelectedInterval = DateUtils.INTERVAL_24H;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(mSelectedInterval);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshChartsData(RefreshChartEvent event) {
        loadData(mSelectedInterval);
    }

    /**
     * Queries the data from mDatabase.
     *
     * @param interval Time interval for fetching the data.
     */
    private void loadData(final int interval) {
        long now = System.currentTimeMillis();
        RealmResults<BatteryUsage> results;

        mChartCards = new ArrayList<>();

        // Make sure mDatabase instance is opened
        if (mActivity.mDatabase.isClosed()) {
            mActivity.mDatabase.getDefaultInstance();
        }

        // Query results according to selected time interval
        if (interval == DateUtils.INTERVAL_24H) {
            results = mActivity.mDatabase.betweenUsages(
                    DateUtils.getMilliSecondsInterval(DateUtils.INTERVAL_24H),
                    now
            );
        } else if (interval == DateUtils.INTERVAL_3DAYS) {
            results = mActivity.mDatabase.betweenUsages(
                    DateUtils.getMilliSecondsInterval(DateUtils.INTERVAL_3DAYS),
                    now
            );
        } else if (interval == DateUtils.INTERVAL_5DAYS) {
            results = mActivity.mDatabase.betweenUsages(
                    DateUtils.getMilliSecondsInterval(DateUtils.INTERVAL_5DAYS),
                    now
            );
        } else {
            results = mActivity.mDatabase.betweenUsages(
                    DateUtils.getMilliSecondsInterval(DateUtils.INTERVAL_24H),
                    now
            );
        }

        fillData(results);

        setAdapter(mSelectedInterval);
    }

    /**
     * Fills in the data queried from the mDatabase.
     *
     * @param results Collection of results fetched from the mDatabase.
     */
    private void fillData(@NonNull RealmResults<BatteryUsage> results) {
        ChartCard card;
        double min = 0;
        double avg = 0;
        double max = 0;

        // Battery Level
        card = new ChartCard(
                ChartRVAdapter.BATTERY_LEVEL,
                getString(R.string.chart_battery_level),
                ColorTemplate.rgb("#E84813")
        );

        for (BatteryUsage usage : results) {
            card.entries.add(new Entry((float) usage.timestamp, usage.level));
        }

        mChartCards.add(card);

        // Battery Temperature
        if (!results.isEmpty()) {
            min = Double.MAX_VALUE;
            avg = 0;
            max = Double.MIN_VALUE;
        }

        card = new ChartCard(
                ChartRVAdapter.BATTERY_TEMPERATURE,
                getString(R.string.chart_battery_temperature),
                ColorTemplate.rgb("#E81332")
        );

        for (BatteryUsage usage : results) {
            card.entries.add(new Entry((float) usage.timestamp, (float) usage.details.temperature));
            if (usage.details.temperature < min) {
                min = usage.details.temperature;
            }
            if (usage.details.temperature > max) {
                max = usage.details.temperature;
            }
            avg += usage.details.temperature;
        }

        avg /= results.size();
        card.extras = new double[] {min, avg, max};
        mChartCards.add(card);

        // Battery Voltage
        if (!results.isEmpty()) {
            min = Double.MAX_VALUE;
            avg = 0;
            max = Double.MIN_VALUE;
        }

        card = new ChartCard(
                ChartRVAdapter.BATTERY_VOLTAGE,
                getString(R.string.chart_battery_voltage),
                ColorTemplate.rgb("#FF15AC")
        );

        for (BatteryUsage usage : results) {
            card.entries.add(new Entry((float) usage.timestamp, (float) usage.details.voltage));
            if (usage.details.voltage < min) {
                min = usage.details.voltage;
            }
            if (usage.details.voltage > max) {
                max = usage.details.voltage;
            }
            avg += usage.details.voltage;
        }

        avg /= results.size();
        card.extras = new double[] {min, avg, max};
        mChartCards.add(card);
    }

    /**
     * Sets the adapter of the recycler view,
     * filtering the time interval of the charts.
     *
     * @param interval Time interval to filter charts results.
     */
    private void setAdapter(final int interval) {
        if (mAdapter == null) {
            // We need the application context to access String resources within the Adapter
            Context context = getActivity().getApplicationContext();

            mAdapter = new ChartRVAdapter(mChartCards, interval, context);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setInterval(interval);
            mAdapter.swap(mChartCards);
        }
        mRecyclerView.invalidate();
    }
}