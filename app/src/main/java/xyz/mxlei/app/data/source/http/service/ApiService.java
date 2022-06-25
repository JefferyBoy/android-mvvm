package xyz.mxlei.app.data.source.http.service;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/login")
    Observable<Boolean> login(@Field("username") String name, @Field("password") String password);
}
