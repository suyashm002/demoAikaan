package com.example.aikaanapp.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Build;

import com.example.aikaanapp.models.data.SensorDetails;

import static com.example.aikaanapp.util.LogUtils.makeLogTag;

/**
 * Sensor attributes getters. See https://developer.android.com/reference/android/hardware/Sensor.html
 */
public class Sensors {
    private static final String TAG = makeLogTag(Sensors.class);
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    private static Map<String, SensorDetails> sensorsMap = new HashMap<>();;
    private static SensorManager manager;

    /**
     * Obtains the current Fifo Max Event Count.
     *
     * @param context Application's context
     * @return Returns the battery voltage
     */
    public static Collection<SensorDetails> getSensorDetailsList(final Context context) {
        verifySensorsChanged(context);
        return sensorsMap.values();
    }

    private static void verifySensorsChanged(final Context context) {
        if (manager == null) {
            manager = (SensorManager)
                    context.getSystemService(Context.SENSOR_SERVICE);
        }
        assert manager != null;
        List<Sensor> values = manager.getSensorList(Sensor.TYPE_ALL);
        if (values.size() != sensorsMap.size()) {
            sensorsMap.clear();
            for (Sensor sensor: values) {
                extractSensorDetails(sensor);
            }
        }
    }

    private static SensorDetails extractSensorDetails(Sensor sensor) {
        SensorDetails details = null;
        if ((details = sensorsMap.get(sensor.getName())) == null) {
            details = new SensorDetails();
            sensorsMap.put(sensor.getName(), details);
        }
        details.codeType = sensor.getType();
        details.fifoMaxEventCount = sensor.getFifoMaxEventCount();
        details.fifoReservedEventCount = sensor.getFifoReservedEventCount();
        getAttributesNewVersion(sensor, details);
        details.isWakeUpSensor = sensor.isWakeUpSensor();
        details.maxDelay = sensor.getMaxDelay();
        details.maximumRange = sensor.getMaximumRange();
        details.minDelay = sensor.getMinDelay();
        details.name = sensor.getName();
        details.power = sensor.getPower();
        details.reportingMode = sensor.getReportingMode();
        details.resolution = sensor.getResolution();
        details.stringType = sensor.getStringType();
        details.vendor = sensor.getVendor();
        details.version = sensor.getVersion();
        return details;
    }

    /**
     * Update events of the sensors
     * @param event SensorEvent
     */
    public static void onSensorChanged(SensorEvent event) {
        SensorDetails details = extractSensorDetails(event.sensor);
        details.frequencyOfUse++;
        if (details.iniTimestamp == 0) {
            details.iniTimestamp = event.timestamp;
        }
        details.endTimestamp = event.timestamp;
    }

    public static void clearSensorsMap() {
        sensorsMap = new HashMap<>();
    }

    @TargetApi(23)
    private static void getAttributesNewVersion(Sensor sensor, SensorDetails details) {
        if (SDK_VERSION > 23) {
            details.id = sensor.getId();
            details.isAdditionalInfoSupported = sensor.isAdditionalInfoSupported();
            details.isDynamicSensor = sensor.isDynamicSensor();
        }
        if (SDK_VERSION > 25) {
            details.highestDirectReportRateLevel = sensor.getHighestDirectReportRateLevel();
        }
    }
}