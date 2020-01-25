package com.example.aikaanapp.fragments;

 import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aikaanapp.Config;
import com.example.aikaanapp.R;
import com.example.aikaanapp.TaskListActivity;
import com.example.aikaanapp.adapters.CustomExpandableListAdapter;
import com.example.aikaanapp.adapters.ExpandableListDataPump;
import com.example.aikaanapp.events.RefreshEvent;
import com.example.aikaanapp.models.Bluetooth;
import com.example.aikaanapp.models.Memory;
import com.example.aikaanapp.models.Network;
import com.example.aikaanapp.models.Phone;
import com.example.aikaanapp.models.Specifications;
import com.example.aikaanapp.models.Storage;
import com.example.aikaanapp.models.Wifi;
import com.example.aikaanapp.models.data.StorageDetails;
import com.example.aikaanapp.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 import androidx.fragment.app.Fragment;

 import static com.example.aikaanapp.util.LogUtils.makeLogTag;

public class DeviceFragment extends Fragment {
    private static final String TAG = makeLogTag(DeviceFragment.class);
    private Context mContext = null;

    private View mParentView = null;

    private Handler mHandler;

    private ProgressBar mMemoryBar;

    private TextView mMemoryUsed;

    private TextView mMemoryFree;

    private ProgressBar mStorageBar;

    private TextView mStorageUsed;

    private TextView mStorageFree;

    private ExpandableListView mExpandableListView;

    private CustomExpandableListAdapter mExpandableListAdapter;

    private List<String> mExpandableListTitle;

    private Map<String, List<String>> mExpandableListDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device, container, false);

        mParentView = view;
        mContext = view.getContext();
        mHandler = new Handler();

        loadComponents(view);
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

    public static DeviceFragment newInstance() {
        return new DeviceFragment();
    }

    // Private Helper Methods ----------------------------------------------------------------------

    /**
     * Helper method to load all UI views and set all values for layout view elements.
     *
     * @param view View to update
     */
    private void loadComponents(final View view) {
        StringBuilder model = new StringBuilder();
        TextView textView;
        String value;

        // Create model device string
        model.append(Specifications.getBrand());
        model.append(" ");
        model.append(Specifications.getModel());

        // Device
        textView = view.findViewById(R.id.androidVersion);
        textView.setText(Specifications.getOsVersion());
        textView = view.findViewById(R.id.androidModel);
        textView.setText(model);
        textView = view.findViewById(R.id.androidImei);
        value = Phone.getDeviceId(mContext);
        textView.setText(value == null ? getString(R.string.not_available) : value);
        textView = view.findViewById(R.id.androidRoot);
        textView.setText(Specifications.isRooted() ?
                getString(R.string.yes) : getString(R.string.no));

        // Network
        updateWifiData(view, Wifi.isEnabled(mContext));
        updateBluetoothData(view, Bluetooth.isEnabled());
        updateMobileData(view, Network.isMobileDataEnabled(mContext));

        // Memory
        mMemoryBar = view.findViewById(R.id.memoryBar);
        mMemoryUsed = view.findViewById(R.id.memoryUsed);
        mMemoryFree = view.findViewById(R.id.memoryFree);
        Button btViewMore = view.findViewById(R.id.buttonViewMore);
        btViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(), TaskListActivity.class));
            }
        });

        // Storage
        mStorageBar = view.findViewById(R.id.storageBar);
        mStorageUsed = view.findViewById(R.id.storageUsed);
        mStorageFree = view.findViewById(R.id.storageFree);

        // Sensors
        updateSensorsData(view, mContext);
        mHandler.post(mRunnable);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(RefreshEvent event) {
        if (mParentView == null) return;

        switch (event.field) {
            case "wifi":
                updateWifiData(mParentView, event.value);
                break;
            case "bluetooth":
                updateBluetoothData(mParentView, event.value);
                break;
            case "mobile":
                updateMobileData(mParentView, event.value);
                break;
            default:
                break;
        }
    }

    private void updateWifiData(final View view, boolean value) {
        TextView textView;

        textView = view.findViewById(R.id.wifi);
        textView.setText(value ? getString(R.string.yes) : getString(R.string.no));

        // Display/Hide Additional Wifi fields
        textView = view.findViewById(R.id.ipAddressLabel);
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        // IP Address
        textView = view.findViewById(R.id.ipAddress);
        if (value) {
            textView.setText(Wifi.getIpAddress(mContext));
        }
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        textView = view.findViewById(R.id.macAddressLabel);
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        // MAC Address
        textView = view.findViewById(R.id.macAddress);
        if (value) {
            textView.setText(Wifi.getMacAddress(mContext));
        }
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        textView = view.findViewById(R.id.ssidLabel);
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        // SSID Network
        textView = view.findViewById(R.id.ssid);
        if (value) {
            textView.setText(Wifi.getInfo(mContext).getSSID());
        }
        textView.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    private void updateSensorsData(final View view, final Context context) {
        mExpandableListDetail = ExpandableListDataPump.getData(context);

        LogUtils.logI(TAG, "SENSORS SIZE = " + mExpandableListDetail.size());

        mExpandableListView = view.findViewById(R.id.expandableListView);

        mExpandableListTitle = new ArrayList<>(mExpandableListDetail.keySet());
        mExpandableListAdapter = new CustomExpandableListAdapter(context,
                mExpandableListTitle, mExpandableListDetail);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                HashMap<String, List<String>> listTemp = ExpandableListDataPump.
                        getData(context);
                ExpandableListAdapter listAdapter = parent.getExpandableListAdapter();
                String group = (String) listAdapter.getGroup(groupPosition);
                List<String> list = listTemp.get(group);
                mExpandableListDetail.put(group, list);
                setListViewHeight(parent, groupPosition);
                mExpandableListAdapter.notifyDataSetChanged();
                return false;
            }
        });

        setListViewHeight(mExpandableListView, -1);
    }

    private Fragment getFragment() {
        return this;
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        //First method called
        if (group == -1) {
            View groupItem = listAdapter.getGroupView(0, false,
                    null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight = groupItem.getMeasuredHeight() /
                    (listAdapter.getChildrenCount(0) + 1)
                    * (listAdapter.getGroupCount() - 1);
        } else {
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View groupItem = listAdapter.getGroupView(i, false,
                        null, listView);
                groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                totalHeight += groupItem.getMeasuredHeight();

                if (((listView.isGroupExpanded(i)) && (i != group))
                        || ((!listView.isGroupExpanded(i)) && (i == group))) {
                    for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                        View listItem = listAdapter.getChildView(i, j, false,
                                null, listView);
                        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                        totalHeight += listItem.getMeasuredHeight();

                    }
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * listAdapter.getGroupCount() * 2);
        if (height < 70)
            height = 220;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void updateBluetoothData(final View view, boolean value) {
        TextView textView;

        textView = view.findViewById(R.id.bluetooth);
        textView.setText(value ? getString(R.string.yes) : getString(R.string.no));

        // Bluetooth Address
        textView = view.findViewById(R.id.bluetoothAddress);
        if (value) {
            textView.setText(Bluetooth.getAddress(view.getContext()));
        }
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        textView = view.findViewById(R.id.bluetoothAddressLabel);
        textView.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    private void updateMobileData(final View view, boolean value) {
        TextView textView;

        textView = view.findViewById(R.id.mobileData);
        textView.setText(value ? getString(R.string.yes) : getString(R.string.no));

        // Bluetooth Address
        textView = view.findViewById(R.id.networkType);
        if (value) {
            textView.setText(Network.getMobileNetworkType(mContext));
        }
        textView.setVisibility(value ? View.VISIBLE : View.GONE);

        textView = view.findViewById(R.id.networkTypeLabel);
        textView.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // 0 total, 1 free, 2 used
            long[] memory = Memory.getMemoryInfo(mContext);
            int totalMemory = (int) (memory[0] / 1000000);
            int freeMemory = (int) (memory[1] / 1000000);
            int usedMemory = (int) (memory[2] / 1000000);
            StorageDetails storageDetails = Storage.getStorageDetails();
            String value;

            mMemoryBar.setMax(totalMemory);
            mMemoryBar.setProgress(usedMemory);

            value = usedMemory + " MB";
            mMemoryUsed.setText(value);

            value = freeMemory + " MB";
            mMemoryFree.setText(value);

            mStorageBar.setMax(storageDetails.total);
            mStorageBar.setProgress(storageDetails.total - storageDetails.free);

            value = (storageDetails.total - storageDetails.free) + " MB";
            mStorageUsed.setText(value);

            value = storageDetails.free + " MB";
            mStorageFree.setText(value);

            mHandler.postDelayed(this, Config.REFRESH_MEMORY_INTERVAL);
        }
    };
}