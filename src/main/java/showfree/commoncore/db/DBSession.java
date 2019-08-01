package showfree.commoncore.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import showfree.commoncore.db.orm.ResultSetConvert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public final class DBSession {

    private DBPool pool;

    public DBSession(DBPool pool) {
        this.pool = pool;
    }

    public DBPool getPool() {
        return pool;
    }

    public JSONArray query(String sql, ResultRowFilter<JSONObject> rowFilter) throws Exception {
        return query(sql, rowFilter, false);
    }

    public JSONArray query(String sql, ResultRowFilter<JSONObject> rowFilter, boolean toHumpName) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            return ResultSetConvert.toJSONArray(conn.createStatement().executeQuery(sql), null, rowFilter, toHumpName);
        } finally {
            pool.close(conn);
        }
    }

    public List query(String sql, Class cls, ResultRowFilter<Object> rowFilter) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            return ResultSetConvert.toObject(conn.createStatement().executeQuery(sql), cls, null, rowFilter);
        } finally {
            pool.close(conn);
        }
    }

    public JSONObject queryFirst(String sql) throws Exception {
        return queryFirst(sql, false);
    }

    public JSONObject queryFirst(String sql, boolean toHumpName) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            JSONArray rows = ResultSetConvert.toJSONArray(conn.createStatement().executeQuery(sql), 1, null, toHumpName);
            return rows.size() > 0 ? rows.getJSONObject(0) : null;
        } finally {
            pool.close(conn);
        }
    }

    public Object queryFirst(String sql, Class cls) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            List rows = ResultSetConvert.toObject(conn.createStatement().executeQuery(sql), cls, 1, null);
            return rows.size() > 0 ? rows.get(0) : null;
        } finally {
            pool.close(conn);
        }
    }

    public void execute(String sql) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            conn.createStatement().execute(sql);
        } finally {
            pool.close(conn);
        }
    }

    public int insertIntAutoGeneratedKey(String sql) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } finally {
            pool.close(conn);
        }
    }

    public long insertLongAutoGeneratedKey(String sql) throws Exception {
        Connection conn = null;
        try {
            conn = pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } finally {
            pool.close(conn);
        }
    }
}
