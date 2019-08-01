package showfree.commoncore.db.orm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import showfree.commoncore.tools.StringTool;
import showfree.commoncore.db.ResultRowFilter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果集转换工具
 * @author 
 *
 */
public class ResultSetConvert {
	
	/**
	 * 将结果集转换成JSON对象数组，每个结果列必须指定Label
	 * @param rs
	 * @param rowLimit null或<=0则转换所有结果，>0则转换指定数量结果
	 * @param rowFilter
	 * @return
	 * @throws SQLException
	 */
	public static JSONArray toJSONArray(ResultSet rs, Integer rowLimit, ResultRowFilter<JSONObject> rowFilter) throws SQLException {
		return toJSONArray(rs, rowLimit, rowFilter);
	}

	/**
	 * 将结果集转换成JSON对象数组，每个结果列必须指定Label
	 * @param rs
	 * @param rowLimit null或<=0则转换所有结果，>0则转换指定数量结果
	 * @param rowFilter
	 * @param toHumpName
	 * @return
	 * @throws SQLException
	 */
	public static JSONArray toJSONArray(ResultSet rs, Integer rowLimit, ResultRowFilter<JSONObject> rowFilter, boolean toHumpName) throws SQLException {
		JSONArray rows = new JSONArray();
		
		if(rs != null) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				JSONObject row = new JSONObject();
				String label = null;
				for(int i = 1; i <= columnCount; i++) {
					label = toHumpName ? StringTool.toHumpName(rsmd.getColumnLabel(i)) : rsmd.getColumnLabel(i);
					switch(rsmd.getColumnType(i)) {
						case Types.NULL:
							row.put(label, null);
							break;
						case Types.BIT:
							row.put(label, rs.getBoolean(i));
							break;
						case Types.SMALLINT:
							row.put(label, rs.getInt(i));
							break;
						case Types.INTEGER:
						case Types.BIGINT:
							if(rsmd.isSigned(i)) {
								row.put(label, rs.getInt(i));
							}else {
								row.put(label, rs.getLong(i));
							}
							break;
						case Types.FLOAT:
							row.put(label, rs.getFloat(i));
							break;
						case Types.DOUBLE:
						case Types.DECIMAL:
							row.put(label, rs.getDouble(i));
							break;
						default:
							row.put(label, rs.getString(i));
							break;
					}
				}
				
				rows.add(rowFilter == null ? row : rowFilter.execute(row));
				
				if(rowLimit != null && rowLimit >= 0 && rows.size() >= rowLimit) {
					break;
				}
			}
		}
		
		return rows;
	}
	
	/**
	 * 将结果集转换成Java对象数组，每个结果列必须指定Label
	 * @param rs
	 * @param cls
	 * @param rowLimit null或<=0则转换所有结果，>0则转换指定数量结果
	 * @param rowFilter
	 * @return
	 * @throws Exception
	 */
	public static List toObject(ResultSet rs, Class cls, Integer rowLimit, ResultRowFilter<Object> rowFilter) throws Exception {
		List rows = new ArrayList();
		
		if(rs != null && cls != null) {
			MappingObject mo = MappingObjectManager.getMappingObject(cls);
			if(mo != null ) {
				Object obj = null;
				while(rs.next()) {
					obj = mo.getObject(rs);
					if(obj != null) {
						rows.add(rowFilter == null ? obj : rowFilter.execute(obj));
					}
					
					if(rowLimit != null && rowLimit > 0 && rows.size() >= rowLimit) {
						break;
					}
				}
			}
		}
		
		return rows;
	}
	
	private ResultSetConvert() {}
}
