package com.example.aikaanapp.network.services;

import com.example.aikaanapp.models.GenericEventPojo;
import com.example.aikaanapp.models.data.Device;
import com.example.aikaanapp.models.data.Upload;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AikaanAPIService {
  /*  @GET("api/mobile/messages")
    Call<List<JsonObject>> getMessages(@Query("uuid") String uuid, @Query("message") int message);
*/
    @POST("api/mobile/register")
    Call<Integer> createDevice(@Body Device device);

    @POST("api/mobile/upload")
    Call<Integer> createSample(@Body Upload upload);

    @POST("api/aiagent/v1/event")
  Call<Integer> sendBatteryEvent(@Body GenericEventPojo genericEventPojo);

}