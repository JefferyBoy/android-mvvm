package xyz.mxlei.app.data.source.http;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.mxlei.app.data.source.http.service.ApiService;

/**
 * 网络数据源
 * @author mxlei
 * @date 2019/03/07
 */
public class HttpDataSource implements ApiService {
    private final ApiService apiService;
    private volatile static HttpDataSource INSTANCE = null;
    private static final String BASE_URL = "http://mxlei.xyz/";

    private HttpDataSource() {
        apiService = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiService.class);
    }

    public static HttpDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSource();
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
