package com.example.dreamgarden.Remote;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICloudFunctions {
    @GET("getCustomToken")
    Observable<ResponseBody> getCustomToken(@Query("firebase-adminsdk-j419l@recmenu-35ad4.iam.gserviceaccount.com") String accessToken);

}
