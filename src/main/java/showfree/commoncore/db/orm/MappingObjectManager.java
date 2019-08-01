package showfree.commoncore.db.orm;

import java.util.HashMap;
import java.util.Map;

public final class MappingObjectManager {

	private final static Map<Class, MappingObject> lookup = new HashMap<Class, MappingObject>();
	
	public static MappingObject getMappingObject(Class cls) {
		if(cls == null) {
			return null;
		}
		
		MappingObject mo = lookup.get(cls);
		if(mo == null) {
			mo = new MappingObject(cls);
		}
		
		return mo;
	}
	
	private MappingObjectManager() {}
}
