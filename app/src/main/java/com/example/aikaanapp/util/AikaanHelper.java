package com.example.aikaanapp.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class AikaanHelper {
    /**
     * Return a Drawable that contains an app icon for the named app. If not
     * found, return the Drawable for the GreenHub icon.
     *
     * @param appName
     *            the application name
     * @return the Drawable for the application's icon
     */
    public static Drawable iconForApp(final Context context, String appName) {
        try {
            return context.getPackageManager().getApplicationIcon(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return ContextCompat.getDrawable(context, android.R.drawable.sym_def_app_icon);
        }
    }

    /**
     * Return a human readable application label for the named app. If not
     * found, return appName.
     *
     * @param appName the application name
     * @return the human readable application label
     */
    public static String labelForApp(final Context context, String appName) {
        if (appName == null) return "Unknown";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(appName, 0);
            if (info != null) {
                return context.getPackageManager().getApplicationLabel(info).toString();
            } else {
                return appName;
            }
        } catch (PackageManager.NameNotFoundException exception) {
            return appName;
        }
    }
}
