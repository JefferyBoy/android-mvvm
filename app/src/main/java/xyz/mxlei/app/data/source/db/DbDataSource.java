package xyz.mxlei.app.data.source.db;

/**
 * 本地数据库源
 *
 * @author mxlei
 * @date 2020/7/12
 */
public class DbDataSource {

    private static volatile DbDataSource instance;

    private DbDataSource() {
    }

    public static DbDataSource getInstance() {
        if (instance == null) {
            synchronized (DbDataSource.class) {
                if (instance == null) {
                    instance = new DbDataSource();
                }
            }
        }
        return instance;
    }


}
