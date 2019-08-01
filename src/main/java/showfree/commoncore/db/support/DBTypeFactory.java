package showfree.commoncore.db.support;

import showfree.commoncore.db.DBConfig;
import showfree.commoncore.db.DBPool;

/**
 * 数据库类型工厂
 * @author 
 *
 */
public interface DBTypeFactory {

	public DBPool getPool(DBConfig config) throws Exception;
}
