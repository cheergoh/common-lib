package showfree.commoncore.db;

import org.apache.log4j.Logger;

import java.sql.Connection;

public final class DBTransaction {

    private static Logger log = Logger.getLogger(DBTransaction.class);

    private DBPool pool;

    public DBTransaction(DBPool pool) {
        this.pool = pool;
    }

    public boolean execute(DBTransactionHandler handler) {
        if (handler == null) {
            return false;
        }

        Connection conn = null;
        try {
            conn = pool.getConnection();
            conn.setAutoCommit(false);
            boolean success = handler.execute(new DBTransactionSession(conn));
            conn.commit();
            return success;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex2) {
                    log.error(ex2.getMessage(), ex2);
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
                pool.close(conn);
            }
        }
    }
}
