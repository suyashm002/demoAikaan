package com.example.aikaanapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aikaanapp.Config;
import com.example.aikaanapp.MainActivity;
import com.example.aikaanapp.R;
import com.example.aikaanapp.adapters.BatteryRVAdapter;
import com.example.aikaanapp.events.BatteryLevelEvent;
import com.example.aikaanapp.events.BatteryTimeEvent;
import com.example.aikaanapp.events.PowerSourceEvent;
import com.example.aikaanapp.events.StatusEvent;
import com.example.aikaanapp.managers.sampling.DataEstimator;
import com.example.aikaanapp.managers.sampling.Inspector;
import com.example.aikaanapp.models.Battery;
import com.example.aikaanapp.models.Data;
import com.example.aikaanapp.models.GenericEventPojo;
import com.example.aikaanapp.models.ui.BatteryCard;
import com.example.aikaanapp.network.CommunicationManager;
import com.example.aikaanapp.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.aikaanapp.util.LogUtils.makeLogTag;

/**
 * Home Fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = makeLogTag("HomeFragment");

    private Context mContext;

    private MainActivity mActivity;

    private TextView mBatteryPercentage;

    private TextView mBatteryCurrentNow;

    private TextView mBatteryCurrentMin;

    private TextView mBatteryCurrentMax;

    private ImageView mPowerDischarging;

    private ImageView mPowerAc;

    private ImageView mPowerUsb;

    private ImageView mPowerWireless;

    private TextView mStatus;

    private ProgressBar mBatteryCircleBar;

    private RecyclerView mRecyclerView;

    private BatteryRVAdapter mAdapter;

    private ArrayList<BatteryCard> mBatteryCards;

    private Thread mLocalThread;

    private Handler mHandler;

    private double mMin;

    private double mMax;

    private String mActivePower;
    CommunicationManager manager;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = view.getContext();
        mActivity = (MainActivity) getActivity();
         manager = new CommunicationManager(
                getActivity(),
                false);
        mRecyclerView = view.findViewById(R.id.rv);
        mAdapter = null;

        LinearLayoutManager layout = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setHasFixedSize(true);

        mBatteryPercentage = view.findViewById(R.id.batteryCurrentValue);
        mBatteryCircleBar = view.findViewById(R.id.batteryProgressbar);
        mStatus = view.findViewById(R.id.status);

        mBatteryCurrentNow = view.findViewById(R.id.batteryCurrentNow);
        mBatteryCurrentMin = view.findViewById(R.id.batteryCurrentMin);
        mBatteryCurrentMax = view.findViewById(R.id.batteryCurrentMax);

        mPowerDischarging = view.findViewById(R.id.imgPowerDischarging);
        mPowerAc = view.findViewById(R.id.imgPowerAc);
        mPowerUsb = view.findViewById(R.id.imgPowerUsb);
        mPowerWireless = view.findViewById(R.id.imgPowerWireless);
        mActivePower = "";

        mMin = Integer.MAX_VALUE;
        mMax = 0;
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, Config.REFRESH_CURRENT_INTERVAL);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        try {
            createFileOnDevice(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity.getEstimator() != null) {
            String level = Integer.toString(mActivity.getEstimator().getLevel());
            mBatteryPercentage.setText(level);
            mBatteryCircleBar.setProgress(mActivity.getEstimator().getLevel());
            loadData(mActivity.getEstimator());
            loadPluggedState("home");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBatteryLevelUI(BatteryLevelEvent event) {
        String text = "battery test" + event.level;
        mBatteryPercentage.setText(text);
        mBatteryCircleBar.setProgress(event.level);
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        // Reload battery cards data from estimator
        loadData(mActivity.getEstimator());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBatteryRemainingTime(BatteryTimeEvent event) {
        // TODO: change the UI with the value from this event
        // For now, it's just logging
        String logText;
        if (event.charging) {
            logText = "" + event.remainingHours + " h " +
                    event.remainingMinutes + " m until complete charge";
        } else {
            logText = "Remaining Time: " + event.remainingHours + " h " +
                    event.remainingMinutes + " m";
        }
        LogUtils.logI("BATTERY_LOG", logText);
         Toast.makeText(mContext, logText, Toast.LENGTH_SHORT).show();


    }

    private void makeAPICallForBatteryData() {
        GenericEventPojo genericEventPojo = new GenericEventPojo();
        Data data = new Data();
        data.setTest(Battery.getBatteryCurrentNowInAmperes(mContext) + "Voltage");
        genericEventPojo.setSeverity(1);
        genericEventPojo.setMessage("Battery Usage");
        genericEventPojo.setData(data);

        Log.d("BatteryData",genericEventPojo.toString());
        manager.sendBatteryData(genericEventPojo);
        writeToFile(Battery.getBatteryCurrentNowInAmperes(mContext) + "Voltage");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateStatus(StatusEvent event) {
        mStatus.setText(event.status);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePowerSource(PowerSourceEvent event) {
        loadPluggedState(event.status);
        if (mActivePower.equals("unplugged")) {
            resetBatteryCurrent();
        }
    }

    /**
     * Creates an array to feed data to the recyclerView
     *
     * @param estimator Provider of mobile status
     */
    private void loadData(final DataEstimator estimator) {
        mLocalThread = new Thread(new Runnable() {
            public void run() {
                mBatteryCards = new ArrayList<>();
                String value;
                int color = Color.GREEN;

                // Temperature
                float temperature = estimator.getTemperature();
                value = temperature + " ºC";
                if (temperature > 45) {
                    color = Color.RED;
                } else if (temperature <= 45 && temperature > 35) {
                    color = Color.YELLOW;
                }
                mBatteryCards.add(
                        new BatteryCard(
                                R.drawable.ic_launcher_background,
                                getString(R.string.battery_summary_temperature),
                                value,
                                color
                        )
                );

                // Voltage
                value = estimator.getVoltage() + " V";
                mBatteryCards.add(
                        new BatteryCard(
                                R.drawable.ic_launcher_background,
                                getString(R.string.battery_summary_voltage),
                                value
                        )
                );
               // makeAPICallForBatteryData();

                // Health
                value = estimator.getHealthStatus(mContext);
                color = value.equals(mContext.getString(R.string.battery_health_good)) ?
                        Color.GREEN : Color.RED;
                mBatteryCards.add(
                        new BatteryCard(
                                R.drawable.ic_launcher_background,
                                getString(R.string.battery_summary_health),
                                value,
                                color
                        )
                );

                // Technology
                if (estimator.getTechnology() == null) {
                    color = Color.GRAY;
                    value = getString(R.string.not_available);
                } else {
                    color = estimator.getTechnology().equals("Li-ion") ? Color.GRAY : Color.GREEN;
                    value = estimator.getTechnology();
                }
                mBatteryCards.add(
                        new BatteryCard(
                                R.drawable.ic_launcher_background,
                                getString(R.string.battery_summary_technology),
                                value,
                                color
                        )
                );
            }
        });

        mLocalThread.start();
        setAdapter();
    }

    private void setAdapter() {
        try {
            mLocalThread.join();
            if (mAdapter == null) {
                mAdapter = new BatteryRVAdapter(mBatteryCards);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.swap(mBatteryCards);
            }
            mRecyclerView.invalidate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadPluggedState(String status) {
        mMin = Integer.MAX_VALUE;
        mMax = Integer.MIN_VALUE;
        String batteryCharger = "unplugged";

        if (status.equals("home")) {
            if (mActivity.getEstimator() == null) return;

            switch (mActivity.getEstimator().getPlugged()) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    batteryCharger = "ac";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    batteryCharger = "usb";
                    break;
            }
        } else {
            batteryCharger = status;
        }

        switch (mActivePower) {
            case "unplugged":
                mPowerDischarging.setImageResource(R.drawable.ic_launcher_background);
                break;
            case "ac":
                mPowerAc.setImageResource(R.drawable.ic_launcher_background);
                break;
            case "usb":
                mPowerUsb.setImageResource(R.drawable.ic_launcher_background);
                break;
            case "wireless":
                mPowerWireless.setImageResource(R.drawable.ic_launcher_background);
                break;
        }

        switch (batteryCharger) {
            case "unplugged":
                mPowerDischarging.setImageResource(R.drawable.ic_launcher_background);
                break;
            case "ac":
                mPowerAc.setImageResource(R.drawable.ic_launcher_background);
                break;
            case "usb":
                mPowerUsb.setImageResource(R.drawable.ic_launcher_background);
                break;
            case "wireless":
                mPowerWireless.setImageResource(R.drawable.ic_launcher_background);
                break;
            default:
                break;
        }

        mActivePower = batteryCharger;
    }

    private void resetBatteryCurrent() {
        mMin = Integer.MAX_VALUE;
        mMax = 0;
        String value = "min: --";
        mBatteryCurrentMin.setText(value);
        value = "max: --";
        mBatteryCurrentMax.setText(value);
        value = getString(R.string.battery_measure);
        mBatteryCurrentNow.setText(value);
    }

    public static BufferedWriter out;
    private void createFileOnDevice(Boolean append) throws IOException {
        /*
         * Function to initially create the log file and it also writes the time of creation to file.
         */
        File Root = Environment.getExternalStorageDirectory();
        if(Root.canWrite()){
            File  LogFile = new File(Root, "LogAikaanFile.txt");
            FileWriter LogWriter = new FileWriter(LogFile, append);
            out = new BufferedWriter(LogWriter);
           // Date date = new Date();
            out.write("Logged at" + (Battery.getBatteryCurrentNowInAmperes(mContext) + "Voltage"+ "\n"));
            out.close();

        }
    }

    public void writeToFile(String message) {
        if (out != null) {
            try {
                out.write(message + "\n");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            double now = Battery.getBatteryCurrentNowInAmperes(mContext);
            double level = Inspector.getCurrentBatteryLevel();
            String value;

            // If is charging and full battery stop mRunnable
            if (!mActivePower.equals("unplugged") && level == 1.0) {
                value = "min: --";
                mBatteryCurrentMin.setText(value);
                value = "max: --";
                mBatteryCurrentMax.setText(value);
                // value = mContext.getString(R.string.battery_full);
                value = String.format(Locale.getDefault(), "%.3f", now) + " A";
                mBatteryCurrentNow.setText(value);
                mHandler.postDelayed(this, Config.REFRESH_CURRENT_INTERVAL);
                return;
            }

                makeAPICallForBatteryData();



            if (Math.abs(now) < Math.abs(mMin)) {
                mMin = now;
                value = "min: " + String.format(Locale.getDefault(), "%.3f", mMin) + " A";
                mBatteryCurrentMin.setText(value);
            }

            if (Math.abs(now) > Math.abs(mMax)) {
                mMax = now;
                value = "max: " + String.format(Locale.getDefault(), "%.3f", mMax) + " A";
                mBatteryCurrentMax.setText(value);
            }

            value = String.format(Locale.getDefault(), "%.3f", now) + " A";
            mBatteryCurrentNow.setText(value);

            mHandler.postDelayed(this, Config.REFRESH_CURRENT_INTERVAL);

        }
    };
}