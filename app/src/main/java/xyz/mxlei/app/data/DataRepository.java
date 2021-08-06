package xyz.mxlei.app.data;

import xyz.mxlei.app.app.AppApplication;
import xyz.mxlei.app.data.source.db.DbDataSource;
import xyz.mxlei.app.data.source.file.FileDataSource;
import xyz.mxlei.app.data.source.http.HttpDataSource;
import xyz.mxlei.app.data.source.sp.SpDataSource;

/**
 * 全局数据仓库，统一管理多个数据源
 *
 * @author mxlei
 * @date 2019/03/07
 */
public class DataRepository {

    private DataRepository() {
    }

    public static HttpDataSource http() {
        return HttpDataSource.getInstance(AppApplication.getInstance());
    }

    public static SpDataSource sp() {
        return SpDataSource.getInstance();
    }

    public static DbDataSource db() {
        return DbDataSource.getInstance();
    }

    public static FileDataSource file() {
        return FileDataSource.getInstance();
    }
}
