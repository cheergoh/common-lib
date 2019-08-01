package showfree.commoncore.db.support.mysql.dbcp2;

import showfree.commoncore.db.DBConfig;
import showfree.commoncore.db.DBPool;

public final class MysqlDBCP2Factory {

    public static DBPool createPool(Object... args) throws Exception {
        if (args == null || args.length == 0) {
            return null;
        }

        if (args[0] == null || !(args[0] instanceof DBConfig)) {
            return null;
        }

        return new MysqlDBCP2Pool((DBConfig) args[0]);
    }
}
