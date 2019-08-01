package showfree.commoncore.db.orm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class MappingFieldConvert {
	
	private static Logger log = Logger.getLogger(MappingFieldConvert.class);

	private final static Map<Class, Convert> typeConvertLookup = new HashMap<Class, Convert>() {
		{
			put(String.class, new Convert<String>() {
				public String execute(ResultSet rs, String label) {
					try {
						return rs.getString(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
			
			put(boolean.class, new Convert<Boolean>() {
				public Boolean execute(ResultSet rs, String label) {
					try {
						return rs.getBoolean(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return false;
					}
				}
			});
			
			put(Boolean.class, new Convert<Boolean>() {
				public Boolean execute(ResultSet rs, String label) {
					try {
						return rs.getBoolean(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
			
			put(int.class, new Convert<Integer>() {
				public Integer execute(ResultSet rs, String label) {
					try {
						return rs.getInt(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return 0;
					}
				}
			});
			
			put(Integer.class, new Convert<Integer>() {
				public Integer execute(ResultSet rs, String label) {
					try {
						return rs.getInt(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
			
			put(long.class, new Convert<Long>() {
				public Long execute(ResultSet rs, String label) {
					try {
						return rs.getLong(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return 0l;
					}
				}
			});
			
			put(Long.class, new Convert<Long>() {
				public Long execute(ResultSet rs, String label) {
					try {
						return rs.getLong(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
			
			put(double.class, new Convert<Double>() {
				public Double execute(ResultSet rs, String label) {
					try {
						return rs.getDouble(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return 0d;
					}
				}
			});
			
			put(Double.class, new Convert<Double>() {
				public Double execute(ResultSet rs, String label) {
					try {
						return rs.getDouble(label);
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
			
			put(JSONObject.class, new Convert<JSONObject>() {
				public JSONObject execute(ResultSet rs, String label) {
					try {
						return JSON.parseObject(rs.getString(label));
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
			
			put(JSONArray.class, new Convert<JSONArray>() {
				public JSONArray execute(ResultSet rs, String label) {
					try {
						return JSON.parseArray(rs.getString(label));
					}catch(Exception ex) {
						log.error(ex.getMessage(), ex);
						return null;
					}
				}
			});
		}
	};
	
	public static Object getValue(ResultSet rs, String label, Class type) throws SQLException {
		Convert convert = typeConvertLookup.get(type);
		return convert != null ? convert.execute(rs, label) : null;
	}
	
	private MappingFieldConvert() {}
	
	private interface Convert<T> {
		public T execute(ResultSet rs, String label);
	}
}
