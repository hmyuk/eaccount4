package com.isu.erp.eaccount.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ERetrofit {

    private String SERVER_URL = "http://175.119.100.44:2020/spAdapter/rfc2/";

    private static final ERetrofit instance = new ERetrofit();
    public static ERetrofit getInstance(){
        return instance;
    }
    private ERetrofit(){

    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RetrofitService service = retrofit.create(RetrofitService.class);

    public RetrofitService getService(){
        return service;
    }
}
