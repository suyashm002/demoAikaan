package com.example.aikaanapp.managers.storage;

import android.content.Intent;

import com.example.aikaanapp.models.data.BatterySession;
import com.example.aikaanapp.models.data.BatteryUsage;
import com.example.aikaanapp.models.data.Sample;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmMigrationNeededException;

public class AikaanDb {

    private Realm mRealm;

    public AikaanDb() {
        try {

            mRealm = Realm.getDefaultInstance();
        } catch (RealmMigrationNeededException e) {
            // handle migration exception io.realm.exceptions.RealmMigrationNeededException
            e.printStackTrace();
        }
    }

    public void getDefaultInstance() {
        if (mRealm.isClosed()) {
            mRealm = Realm.getDefaultInstance();
        }
    }

    public void close() {
        mRealm.close();
    }

    public boolean isClosed() {
        return mRealm.isClosed();
    }

    public long count(Class className) {
        long size = -1;
        if (className.equals(Sample.class)) {
            size = mRealm.where(Sample.class).count();
        } else if (className.equals(BatteryUsage.class)) {
            size = mRealm.where(BatteryUsage.class).count();
        } else if (className.equals(BatterySession.class)) {
            size = mRealm.where(BatterySession.class).count();
        }
        return size;
    }

    public Sample lastSample() {
        if (mRealm.where(Sample.class).count() > 0) {
            return mRealm.where(Sample.class).findAll().last();
        }
        return null;
    }

    /**
     * Store the sample into the database
     *
     * @param sample the sample to be saved
     */
    public void saveSample(Sample sample) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(sample);
        mRealm.commitTransaction();
    }

    /**
     * Store the usage details into the database
     *
     * @param usage the usage details to be saved
     */
    public void saveUsage(BatteryUsage usage) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(usage);
        mRealm.commitTransaction();
    }

    /**
     * Store a new battery session into the database
     *
     * @param session the session to be saved
     */
    public void saveSession(BatterySession session) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(session);
        mRealm.commitTransaction();
    }

    public Iterator<Integer> allSamplesIds() {
        ArrayList<Integer> list = new ArrayList<>();
        RealmResults<Sample> samples = mRealm.where(Sample.class).findAll();
        if (!samples.isEmpty()) {
            for (Sample sample : samples) {
                list.add(sample.id);
            }
        }
        return list.iterator();
    }

    public RealmResults<BatteryUsage> betweenUsages(long from, long to) {
        return mRealm
                .where(BatteryUsage.class)
                .equalTo("triggeredBy", Intent.ACTION_BATTERY_CHANGED)
                .between("timestamp", from, to)
                .findAllSorted("timestamp");
    }



    public RealmResults<BatteryUsage> getUsages() {
        return mRealm
                .where(BatteryUsage.class)
                .findAllSorted("timestamp",Sort.DESCENDING);
    }




}