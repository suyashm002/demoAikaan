package com.example.aikaanapp.models.ui;

import android.content.pm.PackageInfo;

import java.util.TreeSet;

/**
 * Task.
 */
public class Task {

    private int mUid;

    private String mName;

    private double mMemory;

    private String mLabel;

    private PackageInfo mPackageInfo;

    private TreeSet<Integer> mProcesses;

    private boolean mIsChecked;

    private boolean mIsAutoStart;

    private boolean mHasBackgroundService;

    public Task(int uid, String name) {
        mUid = uid;
        mName = name;
        mProcesses = new TreeSet<>();
        mIsChecked = true;
    }

    // ---------------------------------------------------------------------------------------------

    public int getUid() {
        return mUid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getMemory() {
        return mMemory;
    }

    public void setMemory(double memory) {
        this.mMemory = Math.round(memory * 100.0) / 100.0;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.mPackageInfo = packageInfo;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    public boolean isAutoStart() {
        return mIsAutoStart;
    }

    public void setIsAutoStart(boolean isAutoStart) {
        this.mIsAutoStart = isAutoStart;
    }

    public boolean hasBackgroundService() {
        return mHasBackgroundService;
    }

    public void setHasBackgroundService(boolean hasBackgroundService) {
        this.mHasBackgroundService = hasBackgroundService;
    }

    public TreeSet<Integer> getProcesses() {
        return mProcesses;
    }
}