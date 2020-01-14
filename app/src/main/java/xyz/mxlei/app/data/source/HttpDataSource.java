package xyz.mxlei.app.data.source;


import io.reactivex.Observable;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    //模拟登录
    Observable<Object> login();

}
