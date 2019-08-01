package showfree.commoncore.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接池接口
 *
 * @author
 */
public interface DBPool {

    /**
     * 获取数据库配置
     *
     * @return
     */
    public DBConfig getConfig();

    /**
     * 判断当前连接池是否已关闭
     *
     * @return
     */
    public boolean isClosed();

    /**
     * 从连接池内获取一个连接实例
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException;

    /**
     * 关闭指定数据库连接（重新放回连接池内，等待下次使用）
     *
     * @param closeable
     */
    public void close(AutoCloseable closeable);
}
