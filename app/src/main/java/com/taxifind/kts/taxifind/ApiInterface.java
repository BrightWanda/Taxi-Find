package com.taxifind.kts.taxifind;

import com.taxifind.kts.POJOs.Countries;
import com.taxifind.kts.POJOs.Country;
import com.taxifind.kts.POJOs.Distance;
import com.taxifind.kts.POJOs.Municipalities;
import com.taxifind.kts.POJOs.Province;
import com.taxifind.kts.POJOs.Provinces;
import com.taxifind.kts.POJOs.UserInput;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by wandab on 2017/07/07.
 */
public interface ApiInterface {

    @GET("Countries({id})/Provinces")
    Call<Provinces> getProvinces(@Path("id") int id);
    @GET("Provinces({id})/Municipalities")
    Call<Municipalities> getMunicipalities(@Path("id") int id);
    @GET("Countries")
    Call<Countries> getCountries();
    @FormUrlEncoded
    @POST("Distances")
    Call<ArrayList<Distance>> getDistances(
            @Field("id") int id,
            @Field("origin") String origin,
            @Field("destination") String destination,
            @Field("mycity") String mycity,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude);
    @FormUrlEncoded
    @POST("User_Input")
    Call<UserInput> getUserInput(
            @Field("user_input_id") int user_input_id,
            @Field("Origin") String Origin,
            @Field("Destination") String Destination,
            @Field("Price") String Price,
            @Field("Origin_Loc_Lat") Double Origin_Loc_Lat,
            @Field("Origin_Loc_Long") Double Origin_Loc_Long,
            @Field("Destination_Loc_Lat") Double Destination_Loc_Lat,
            @Field("Destination_Loc_Long") Double Destination_Loc_Long,
            @Field("Valid") Double Valid);
}
