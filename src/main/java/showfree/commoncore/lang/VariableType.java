package showfree.commoncore.lang;

/**
 * 可变类型对象基类
 * @author 
 *
 * @param <T>
 */
public class VariableType<T> {

	public T value;
	
	public VariableType() {}
	
	public VariableType(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
}
