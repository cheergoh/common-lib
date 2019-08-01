package showfree.commoncore.db.support;

import showfree.commoncore.db.support.mysql.MysqlDBFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型支持工具
 *
 * @author
 */
public final class DBTypeSupport {

    private final static Map<String, DBTypeFactory> factoryLookup = new HashMap<String, DBTypeFactory>() {
        {
            put("mysql", new MysqlDBFactory());
        }
    };

    public static DBTypeFactory getFactory(String type) {
        return factoryLookup.get(type);
    }

    private DBTypeSupport() {
    }
}
