package showfree.commoncore.db.orm;

import showfree.commoncore.tools.StringTool;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MappingObject {

	private Class cls;
	private List<String> fieldLabelList = new ArrayList<String>();
	private Map<String, List<Field>> fieldLookup = new HashMap<String, List<Field>>();
	
	public MappingObject(Class cls) {
		if(cls != null) {
			this.cls = cls;
			Field[] fields = cls.getFields();
			if(fields.length > 0) {
				for(Field field : fields) {
					MappingField mf = field.getDeclaredAnnotation(MappingField.class);
					if(mf != null) {
						String label = mf.value();
						if(!StringTool.isEmpty(label)) {
							if(!fieldLabelList.contains(label)) {
								fieldLabelList.add(label);
							}
							
							List<Field> list = fieldLookup.get(label);
							if(list == null) {
								list = new ArrayList<Field>();
								fieldLookup.put(label, list);
							}
							list.add(field);
							field.setAccessible(true);
						}
					}
				}
			}
		}
	}
	
	public Object getObject(ResultSet rs) throws IllegalAccessException, InstantiationException, SQLException {
		if(cls == null) {
			return null;
		}
		
		Object obj = cls.newInstance();
		
		if(!fieldLabelList.isEmpty()) {
			List<Field> list = null;
			for(String label : fieldLabelList) {
				list = fieldLookup.get(label);
				if(!list.isEmpty()) {
					for(Field field : list) {
						field.set(obj, MappingFieldConvert.getValue(rs, label, field.getType()));
					}
				}
			}
		}
		
		return obj;
	}
}
