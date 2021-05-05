package com.example.dreamgarden.Remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitICloudClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
    if (retrofit == null )
        retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-recmenu-35ad4.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    return retrofit;
    }
}
