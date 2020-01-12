package com.example.aikaanapp;

public final class Config {
    public static final boolean DEBUG = true;

    public static final String SERVER_STATUS_URL = "";
    public static final String SERVER_URL_DEFAULT = "none";
    public static final String SERVER_URL_DEVELOPMENT = "";

    public static final int DATABASE_VERSION = 1;

    public static final String IMPORTANCE_NOT_RUNNING = "Not Running";
    public static final String IMPORTANCE_UNINSTALLED = "uninstalled";
    public static final String IMPORTANCE_DISABLED = "disabled";
    public static final String IMPORTANCE_INSTALLED = "installed";
    public static final String IMPORTANCE_REPLACED = "replaced";
    public static final int IMPORTANCE_APP = 9999;

    public static final String BATTERY_SOURCE_DEFAULT =
            "/sys/class/power_supply/battery/current_now";
    public static final String BATTERY_SOURCE_ALTERNATIVE =
            "/sys/devices/platform/battery/power_supply/battery/BatteryAverageCurrent";

    public static final String BATTERY_STATS_SOURCE_DEFAULT =
            "/sys/class/power_supply/battery/uevent";
    // Battery Full Capacity in uAh
    public static final String BATTERY_CHARGE_FULL = "POWER_SUPPLY_CHARGE_FULL";
    public static final String BATTERY_CHARGE_FULL_DESIGN = "POWER_SUPPLY_CHARGE_FULL_DESIGN";
    // Battery Full Capacity in uWh
    public static final String BATTERY_ENERGY_FULL = "POWER_SUPPLY_ENERGY_FULL";
    public static final String BATTERY_ENERGY_FULL_DESIGN = "POWER_SUPPLY_CHARGE_FULL_DESIGN";
    // The circuit voltage at the moment
    public static final String BATTERY_VOLTAGE_NOW = "POWER_SUPPLY_VOLTAGE_NOW";
    // The Current at the moment in uA
    public static final String BATTERY_CURRENT_NOW = "POWER_SUPPLY_CURRENT_NOW";
    // The device remaining capacity in uAh
    public static final String BATTERY_ENERGY_NOW = "POWER_SUPPLY_ENERGY_NOW";
    // The battery capacity percentage (1-100)
    public static final String BATTERY_CAPACITY = "POWER_SUPPLY_CAPACITY";

    public static final int BATTERY_CAPACITY_SAMPLES_SIZE = 10;

    // assume that, in one hours, it charges 500 mA
    public static final int DEFAULT_USB_CHARGE_RATE = 500;
    // assume that, in one hours, it charges 1500 mA
    public static final int DEFAULT_AC_CHARGE_RATE = 1500;
    // assume that, in one hours, it charges 500 mA
    public static final int DEFAULT_WIRELESS_CHARGE_RATE = 500;
    // assume a continuous discharge of 200 mA per hour
    public static final int DEFAULT_DISCHARGE_RATE = 200;

    public static final String DATA_HISTORY_DEFAULT = "4";

    public static final int UPLOAD_MAX_TRIES = 3;
    public static final String UPLOAD_DEFAULT_RATE = "20";

    public static final int SAMPLES_MAX_STORAGE_NUM = 500;

    public static final int STARTER_MESSAGE_ID = 0;

    public static final int STARTUP_CURRENT_INTERVAL = 2000;
    public static final int REFRESH_CURRENT_INTERVAL = 5000;
    public static final int REFRESH_MEMORY_INTERVAL = 10000;
    public static final int REFRESH_STATUS_BAR_INTERVAL = REFRESH_CURRENT_INTERVAL * 6;
    public static final int REFRESH_STATUS_ERROR = REFRESH_CURRENT_INTERVAL * 2;

    public static final String NOTIFICATION_DEFAULT_TEMPERATURE_RATE = "5";
    public static final String NOTIFICATION_DEFAULT_TEMPERATURE_WARNING = "35";
    public static final String NOTIFICATION_DEFAULT_TEMPERATURE_HIGH = "45";
    public static final double BATTERY_LOW_LEVEL = 0.2;

    public static final int PERMISSION_READ_PHONE_STATE = 1;
    public static final int PERMISSION_ACCESS_COARSE_LOCATION = 2;
    public static final int PERMISSION_ACCESS_FINE_LOCATION = 3;

    public static final String NOTIFICATION_DEFAULT_PRIORITY = "0";
    public static final int NOTIFICATION_BATTERY_STATUS = 1001;
    public static final int NOTIFICATION_BATTERY_FULL = 1002;
    public static final int NOTIFICATION_BATTERY_LOW = 1003;
    public static final int NOTIFICATION_TEMPERATURE_WARNING = 1004;
    public static final int NOTIFICATION_TEMPERATURE_HIGH = 1005;
    public static final int NOTIFICATION_MESSAGE_NEW = 1006;

    public static final int PENDING_REMOVAL_TIMEOUT = 1500; // 1.5s
    public static final int KILL_APP_TIMEOUT = 15000; // 15s
    public static final int SORT_BY_MEMORY = 1;
    public static final int SORT_BY_NAME = 2;

    public static final String[] PERMISSIONS_ARRAY = {
            "android.permission.ACCESS_CHECKIN_PROPERTIES",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
            "android.permission.ACCESS_MOCK_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_SURFACE_FLINGER",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.ACCOUNT_MANAGER",
            "android.permission.AUTHENTICATE_ACCOUNTS",
            "android.permission.BATTERY_STATS",
            "android.permission.BIND_APPWIDGET",
            "android.permission.BIND_DEVICE_ADMIN",
            "android.permission.BIND_INPUT_METHOD",
            "android.permission.BIND_WALLPAPER",
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.BRICK",
            "android.permission.BROADCAST_PACKAGE_REMOVED",
            "android.permission.BROADCAST_SMS",
            "android.permission.BROADCAST_STICKY",
            "android.permission.BROADCAST_WAP_PUSH",
            "android.permission.CALL_PHONE",
            "android.permission.CALL_PRIVILEGED",
            "android.permission.CAMERA",
            "android.permission.CHANGE_COMPONENT_ENABLED_STATE",
            "android.permission.CHANGE_CONFIGURATION",
            "android.permission.CHANGE_NETWORK_STATE",
            "android.permission.CHANGE_WIFI_MULTICAST_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.CLEAR_APP_CACHE",
            "android.permission.CLEAR_APP_USER_DATA",
            "android.permission.CONTROL_LOCATION_UPDATES",
            "android.permission.DELETE_CACHE_FILES",
            "android.permission.DELETE_PACKAGES",
            "android.permission.DEVICE_POWER",
            "android.permission.DIAGNOSTIC",
            "android.permission.DISABLE_KEYGUARD",
            "android.permission.DUMP",
            "android.permission.EXPAND_STATUS_BAR",
            "android.permission.FACTORY_TEST",
            "android.permission.FLASHLIGHT",
            "android.permission.FORCE_BACK",
            "android.permission.GET_ACCOUNTS",
            "android.permission.GET_PACKAGE_SIZE",
            "android.permission.GET_TASKS",
            "android.permission.GLOBAL_SEARCH",
            "android.permission.HARDWARE_TEST",
            "android.permission.INJECT_EVENTS",
            "android.permission.INSTALL_LOCATION_PROVIDER",
            "android.permission.INSTALL_PACKAGES",
            "android.permission.INTERNAL_SYSTEM_WINDOW",
            "android.permission.INTERNET",
            "android.permission.KILL_BACKGROUND_PROCESSES",
            "android.permission.MANAGE_ACCOUNTS",
            "android.permission.MANAGE_APP_TOKENS",
            "android.permission.MASTER_CLEAR",
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.MODIFY_PHONE_STATE",
            "android.permission.MOUNT_FORMAT_FILESYSTEMS",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
            "android.permission.PERSISTENT_ACTIVITY",
            "android.permission.PROCESS_OUTGOING_CALLS",
            "android.permission.READ_CALENDAR",
            "android.permission.READ_CONTACTS",
            "android.permission.READ_FRAME_BUFFER",
            "com.android.browser.permission.READ_HISTORY_BOOKMARKS",
            "android.permission.READ_INPUT_STATE",
            "android.permission.READ_LOGS",
            "android.permission.READ_OWNER_DATA",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_SMS",
            "android.permission.READ_SYNC_SETTINGS",
            "android.permission.READ_SYNC_STATS",
            "android.permission.REBOOT",
            "android.permission.RECEIVE_BOOT_COMPLETED",
            "android.permission.RECEIVE_MMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.RECEIVE_WAP_PUSH",
            "android.permission.RECORD_AUDIO",
            "android.permission.REORDER_TASKS",
            "android.permission.RESTART_PACKAGES",
            "android.permission.SEND_SMS",
            "android.permission.SET_ACTIVITY_WATCHER",
            "android.permission.SET_ALWAYS_FINISH",
            "android.permission.SET_ANIMATION_SCALE",
            "android.permission.SET_DEBUG_APP",
            "android.permission.SET_ORIENTATION",
            "android.permission.SET_PREFERRED_APPLICATIONS",
            "android.permission.SET_PROCESS_LIMIT",
            "android.permission.SET_TIME",
            "android.permission.SET_TIME_ZONE",
            "android.permission.SET_WALLPAPER",
            "android.permission.SET_WALLPAPER_HINTS",
            "android.permission.SIGNAL_PERSISTENT_PROCESSES",
            "android.permission.STATUS_BAR",
            "android.permission.SUBSCRIBED_FEEDS_READ",
            "android.permission.SUBSCRIBED_FEEDS_WRITE",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.UPDATE_DEVICE_STATS",
            "android.permission.USE_CREDENTIALS",
            "android.permission.VIBRATE",
            "android.permission.WAKE_LOCK",
            "android.permission.WRITE_APN_SETTINGS",
            "android.permission.WRITE_CALENDAR",
            "android.permission.WRITE_CONTACTS",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.WRITE_GSERVICES",
            "com.android.browser.permission.WRITE_HISTORY_BOOKMARKS",
            "android.permission.WRITE_OWNER_DATA",
            "android.permission.WRITE_SECURE_SETTINGS",
            "android.permission.WRITE_SETTINGS",
            "android.permission.WRITE_SMS",
            "android.permission.WRITE_SYNC_SETTINGS"
    };
}