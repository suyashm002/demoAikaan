package com.example.aikaanapp.models;

import com.example.aikaanapp.Config;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Permissions.
 */
public class Permissions {

    private static final String TAG = "Permissions";

    private static final ArrayList<String> PERMISSION_LIST = new ArrayList<>();

    public static byte[] getPermissionBytes(String[] perms) {
        if (perms == null) {
            return null;
        }
        if (PERMISSION_LIST.size() == 0) {
            populatePermList();
        }

        byte[] bytes = new byte[PERMISSION_LIST.size() / 8 + 1];

        for (String p : perms) {
            int idx = PERMISSION_LIST.indexOf(p);
            if (idx > 0) {
                int i = idx / 8;
                idx = (int) Math.pow(2, idx - i * 8);
                bytes[i] = (byte) (bytes[i] | idx);
            }
        }
        return bytes;
    }

    private static void populatePermList() {
        Collections.addAll(PERMISSION_LIST, Config.PERMISSIONS_ARRAY);
    }
}