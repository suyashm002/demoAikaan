package com.example.aikaanapp.tasks;

import android.os.AsyncTask;

import com.example.aikaanapp.models.data.Sample;

import androidx.annotation.NonNull;
import io.realm.Realm;

public class DeleteSampleTask extends AsyncTask<Integer, Void, Void> {
    @Override
    protected Void doInBackground(Integer... params) {
        // Open the Realm
        Realm realm = Realm.getDefaultInstance();
        try {
            final int id = params[0];
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    Sample sample = realm.where(Sample.class).equalTo("id", id).findFirst();
                    if (sample != null) sample.deleteFromRealm();
                }
            });
        } finally {
            realm.close();
        }
        return null;
    }
}