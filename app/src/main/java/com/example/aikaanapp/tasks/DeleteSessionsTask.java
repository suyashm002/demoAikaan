package com.example.aikaanapp.tasks;

import android.os.AsyncTask;

import com.example.aikaanapp.models.data.BatterySession;
import com.example.aikaanapp.util.DateUtils;

import io.realm.Realm;
import io.realm.RealmResults;

public class DeleteSessionsTask extends AsyncTask<Integer, Void, Boolean> {

    private static final String TAG = "DeleteSessionsTask";

    private boolean mResponse;

    @Override
    protected Boolean doInBackground(Integer... params) {
        mResponse = false;
        // Open the Realm
        Realm realm = Realm.getDefaultInstance();

        try {
            final int interval = params[0];
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<BatterySession> sessions =
                            realm.where(BatterySession.class).lessThan(
                                    "timestamp",
                                    DateUtils.getMilliSecondsInterval(interval)
                            ).findAll();
                    if (sessions != null && !sessions.isEmpty()) {
                        mResponse = sessions.deleteAllFromRealm();
                    }
                }
            });
        } finally {
            realm.close();
        }

        return mResponse;
    }
}