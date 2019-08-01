package showfree.commoncore.db;

public interface DBTransactionHandler {

	public boolean execute(DBTransactionSession session) throws Exception;
}
