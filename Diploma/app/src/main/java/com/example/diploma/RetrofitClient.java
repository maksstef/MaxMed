package com.example.diploma;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


import okhttp3.logging.HttpLoggingInterceptor;


public class RetrofitClient {
//    private static Retrofit instance;
//
//    public static Retrofit getInstance() {
//        if(instance == null)
//            instance =  new Retrofit.Builder()
//                    .baseUrl("http://10.0.2.2:5000/")
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
//        return instance;
//    }

    static Retrofit instance;
    static HttpLoggingInterceptor interceptor;

    public static Retrofit getInstance() {
        if(instance == null){
            interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            instance = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/")
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}
