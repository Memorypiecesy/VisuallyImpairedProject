package com.example.visuallyimpairedproject.Client;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static volatile RetrofitClient mInstance;
    public static final String BASE_URL = "http://192.168.1.8:9001";
//    public static final String BASE_URL = "http://192.168.43.161:9001";
    public static final String BASE__IMAGE_URL = RetrofitClient.BASE_URL +"/";
    private Retrofit retrofit;

    private RetrofitClient(){

    }

    public static RetrofitClient getInstance(){
        if (mInstance==null){
            synchronized (RetrofitClient.class){
                if (mInstance==null){
                    mInstance= new RetrofitClient();
                }
            }
        }
        return mInstance;
    }

    public <T> T getService(Class<T> cls){
        return getRetrofit().create(cls);
    }

    private synchronized Retrofit getRetrofit(){
        if (retrofit ==null){
            retrofit =new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
