package com.example.aikaanapp.util;

import android.util.Log;

/**
 * LogUtils.
 */
public class LogUtils {
    private static final String LOG_PREFIX = "batteryhub_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static boolean LOGGING_ENABLED = false;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void logD(final String tag, String message) {
        if (LOGGING_ENABLED && message != null && Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void logD(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED && message != null && Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause);
        }
    }

    public static void logI(final String tag, String message) {
        if (LOGGING_ENABLED && message != null) {
            Log.i(tag, message);
        }
    }

    public static void logI(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED && message != null) {
            Log.i(tag, message, cause);
        }
    }

    public static void logW(final String tag, String message) {
        if (LOGGING_ENABLED && message != null) {
            Log.w(tag, message);
        }
    }

    public static void logW(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED && message != null) {
            Log.w(tag, message, cause);
        }
    }

    public static void logE(final String tag, String message) {
        if (LOGGING_ENABLED && message != null) {
            Log.e(tag, message);
        }
    }

    public static void logE(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED && message != null) {
            Log.e(tag, message, cause);
        }
    }

    private LogUtils() {}
}