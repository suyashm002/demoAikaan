package com.example.aikaanapp.models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.aikaanapp.Config;
import com.example.aikaanapp.models.data.ProcessInfo;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package properties model.
 */
public class Package {

    private static WeakReference<Map<String, PackageInfo>> packages = null;

    /**
     * Helper to ensure the WeakReferenced `packages` is populated.
     *
     * @param context           the Context
     * @param collectSignatures if needs to collect signatures or not
     * @return The content of `packages` or null in case of failure.
     */
    private static Map<String, PackageInfo> getPackages(Context context,
                                                        boolean collectSignatures) {
        List<PackageInfo> packageList = null;
        Map<String, PackageInfo> map;

        try {
            if (packages != null && packages.get() != null && packages.get().size() != 0) {
                if (packages == null) return null;
                map = packages.get();

                return (map == null || map.size() == 0) ? null : map;
            }

            map = new HashMap<>();
            PackageManager manager = context.getPackageManager();

            if (manager == null) return null;

            try {
                if (collectSignatures) {
                    packageList = manager.getInstalledPackages(
                            PackageManager.GET_SIGNATURES | PackageManager.GET_PERMISSIONS
                    );
                } else {
                    packageList = manager.getInstalledPackages(0);
                }
            } catch (Throwable th) {
                // Forget about it...
            }
            if (packageList == null) return null;

            for (PackageInfo info : packageList) {
                if (info == null ||
                        info.applicationInfo == null ||
                        info.applicationInfo.processName == null) {
                    continue;
                }
                map.put(info.applicationInfo.processName, info);
            }

            packages = new WeakReference<>(map);

            return (map.size() == 0) ? null : map;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get info for a single package from the WeakReferenced packagelist.
     *
     * @param context     The Context
     * @param processName The package to get info for.
     * @return info for a single package from the WeakReferenced packagelist.
     */
    public static PackageInfo getPackageInfo(Context context, String processName) {
        Map<String, PackageInfo> map = getPackages(context, true);

        return (map == null || !map.containsKey(processName)) ? null : map.get(processName);
    }

    /**
     * Returns a list of installed packages on the device. Will be called for
     * the first Aikaan sample on a phone, to get signatures for the malware
     * detection project. Later on, single package information is got by
     * receiving the package installed intent.
     *
     * @param context      The Context
     * @param filterSystem if true, exclude system packages.
     * @return a list of installed packages on the device.
     */
    public static Map<String, ProcessInfo> getInstalledPackages(Context context,
                                                                boolean filterSystem) {
        Map<String, PackageInfo> packageMap = getPackages(context, true);
        PackageManager pm = context.getPackageManager();

        if (pm == null) return null;

        Map<String, ProcessInfo> result = new HashMap<>();

        for (Map.Entry<String, PackageInfo> entry : packageMap.entrySet()) {
            try {
                String pkg = entry.getKey();
                PackageInfo pak = entry.getValue();
                if (pak != null) {
                    int vc = pak.versionCode;
                    ApplicationInfo appInfo = pak.applicationInfo;
                    String label = pm.getApplicationLabel(appInfo).toString();
                    // we need application UID to be able to use Android's
                    // TrafficStat API
                    // in order to get the traffic info of a particular app:
                    int appUid = appInfo.uid;
                    // get the amount of transmitted and received bytes by an
                    // app
                    // TODO: disabled for debugging
                    // TrafficRecord trafficRecord = getAppTraffic(appUid);

                    int flags = pak.applicationInfo.flags;
                    // Check if it is a system app
                    boolean isSystemApp = (flags & ApplicationInfo.FLAG_SYSTEM) > 0;
                    isSystemApp =
                            isSystemApp || (flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0;
                    if (filterSystem & isSystemApp)
                        continue;
                    if (pak.signatures.length > 0) {
                        ProcessInfo pi = new ProcessInfo();
                        pi.name = pkg;
                        pi.applicationLabel = label;
                        pi.versionCode = vc;
                        pi.processId = -1;
                        pi.isSystemApp = isSystemApp;
                        pi.appSignatures.addAll(Signatures.getSignatureList(pak));
                        pi.importance = Config.IMPORTANCE_NOT_RUNNING;
                        pi.installationPkg = pm.getInstallerPackageName(pkg);
                        pi.versionName = pak.versionName;
                        // pi.setTrafficRecord(trafficRecord);
                        result.put(pkg, pi);
                    }
                }
            } catch (Throwable th) {
                // Forget about it...
            }
        }
        return result;
    }

    /**
     * Returns info about an installed package. Will be called when receiving
     * the PACKAGE_ADDED or PACKAGE_REPLACED intent.
     *
     * @param context the Context.
     * @return a list of installed packages on the device.
     */
    public static ProcessInfo getInstalledPackage(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        if (pm == null)
            return null;
        PackageInfo pak;
        try {
            pak = pm.getPackageInfo(
                    pkg,
                    PackageManager.GET_SIGNATURES | PackageManager.GET_PERMISSIONS
            );
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        if (pak == null)
            return null;

        ProcessInfo pi = new ProcessInfo();
        int vc = pak.versionCode;
        ApplicationInfo info = pak.applicationInfo;
        String label = pm.getApplicationLabel(info).toString();
        int flags = pak.applicationInfo.flags;
        // Check if it is a system app
        boolean isSystemApp = (flags & ApplicationInfo.FLAG_SYSTEM) > 0;
        isSystemApp = isSystemApp || (flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0;

        if (pak.signatures.length > 0) {
            pi.name = pkg;
            pi.applicationLabel = label;
            pi.versionCode = vc;
            pi.processId = -1;
            pi.isSystemApp = isSystemApp;
            pi.appSignatures.addAll(Signatures.getSignatureList(pak));
            pi.importance = Config.IMPORTANCE_NOT_RUNNING;
            pi.installationPkg = pm.getInstallerPackageName(pkg);
            pi.versionName = pak.versionName;
        }
        return pi;
    }
}