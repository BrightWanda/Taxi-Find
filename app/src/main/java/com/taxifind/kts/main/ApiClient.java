package com.taxifind.kts.main;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wandab on 2017/07/07.
 */
public class ApiClient {

    public static final String BASE_URL = "http://taxifind.co.za/odata/";
    //public static final String BASE_URL = "http://172.16.0.11:60219/odata/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                        addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }

}
