package xyz.mxlei.app.data.source.file;

/**
 * 本地文件源
 *
 * @author mxlei
 * @date 2020/7/12
 */
public class FileDataSource {

    private static volatile FileDataSource instance;

    private FileDataSource() {
    }

    public static FileDataSource getInstance() {
        if (instance == null) {
            synchronized (FileDataSource.class) {
                if (instance == null) {
                    instance = new FileDataSource();
                }
            }
        }
        return instance;
    }


}
