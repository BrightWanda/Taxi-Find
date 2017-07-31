package com.taxifind.kts.taxifind;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wandab on 2017/07/07.
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("Distances")
    public Call<List<Distance>> getDistances(
            @Field("id") int id,
            @Field("origin") String origin,
            @Field("destination") String destination,
            @Field("mycity") String mycity,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude);
    //Call<List<Distance>> getDistance();
}
