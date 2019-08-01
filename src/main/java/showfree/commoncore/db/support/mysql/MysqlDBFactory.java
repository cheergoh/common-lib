package showfree.commoncore.db.support.mysql;

import showfree.commoncore.tools.StringTool;
import showfree.commoncore.db.DBConfig;
import showfree.commoncore.db.DBPool;
import showfree.commoncore.db.support.DBTypeFactory;
import showfree.commoncore.db.support.mysql.dbcp2.MysqlDBCP2Factory;

/**
 * mysql数据库工厂
 *
 * @author
 */
public final class MysqlDBFactory implements DBTypeFactory {

    @Override
    public DBPool getPool(DBConfig config) throws Exception {
        if (StringTool.isEmpty(config.getPool())) {
            return null;
        }

        DBPool pool = MysqlDBCP2Factory.createPool(config);
        if (pool == null) {
            throw new Exception("获取mysql连接池失败，请检查配置");
        }
        System.out.println("pool:" + pool.getConfig());
        return pool;
    }
}
