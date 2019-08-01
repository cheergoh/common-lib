package showfree.commoncore.db.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 字段映射注解
 * @author 
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingField {

	String value(); //映射字段名称
}
