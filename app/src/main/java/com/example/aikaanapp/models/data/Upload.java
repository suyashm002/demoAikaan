package com.example.aikaanapp.models.data;

import com.google.gson.JsonObject;

/**
 * Upload.
 */
public class Upload {

    public final JsonObject sample;

    public Upload(final JsonObject sample) {
        this.sample = sample;
    }
}