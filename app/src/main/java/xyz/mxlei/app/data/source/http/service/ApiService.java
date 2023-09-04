package xyz.mxlei.app.data.source.http.service;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface ApiService {

    @POST("/login")
    Observable<Boolean> login(@Field("username") String name, @Field("password") String password);
}
