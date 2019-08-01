package showfree.commoncore.db;

public interface ResultRowFilter<T> {

	public T execute(T row);
}
