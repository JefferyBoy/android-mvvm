package xyz.mxlei.app.data.source.http;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.mxlei.app.data.source.http.service.ApiService;
import xyz.mxlei.mvvmx.http.cookie.CookieJarImpl;
import xyz.mxlei.mvvmx.http.cookie.store.PersistentCookieStore;

/**
 * 网络数据源
 *
 * @author mxlei
 * @date 2019/03/07
 */
public class HttpDataSource implements ApiService {
    private final ApiService apiService;
    private final OkHttpClient okhttpClient;
    private volatile static HttpDataSource INSTANCE = null;
    private static final String BASE_URL = "http://mxlei.xyz/";

    private HttpDataSource(Context context) {
        okhttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(context.getApplicationContext())))
                .build();
        apiService = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient)
                .baseUrl(BASE_URL)
                .build()
                .create(ApiService.class);
    }

    public static HttpDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HttpDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<Boolean> login(String name, String password) {
//        return apiService.login(name, password);
        //模拟登录 延迟1秒
        return Observable.just(true).delay(1, TimeUnit.SECONDS);
    }
}
