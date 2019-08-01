package showfree.commoncore.db.support.mysql.dbcp2;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.log4j.Logger;
import showfree.commoncore.db.DBConfig;
import showfree.commoncore.db.DBPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class MysqlDBCP2Pool implements DBPool {

    private Logger log = Logger.getLogger(MysqlDBCP2Pool.class);

    private DBConfig config; //数据库配置
    private DataSource dataSource; //数据源实例

    public MysqlDBCP2Pool(DBConfig config) throws Exception {
        if (config != null) {
            this.config = config;

            Properties prop = new Properties();
            prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
            prop.setProperty("url", "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getSchema() + "?connectTimeout=3000&socketTimeout=60000&useUnicode=true&characterEncoding=utf8");
            prop.setProperty("username", config.getUsername());
            prop.setProperty("password", config.getPassword());
            prop.setProperty("initialSize", String.valueOf(config.getInitialSize()));
            prop.setProperty("maxTotal", String.valueOf(config.getMaxSize()));
            prop.setProperty("maxWaitMillis", String.valueOf(config.getMaxWait()));
            prop.setProperty("characterEncoding", "UTF-8");
            prop.setProperty("connectionProperties", "useUnicode=true;characterEncoding=utf8;useSSL=false;");
            prop.setProperty("defaultAutoCommit", "true");

            dataSource = BasicDataSourceFactory.createDataSource(prop);
        }
    }

    @Override
    public DBConfig getConfig() {
        return config;
    }

    @Override
    public boolean isClosed() {
        return dataSource == null || ((BasicDataSource) dataSource).isClosed();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource != null ? dataSource.getConnection() : null;
    }

    @Override
    public void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}
