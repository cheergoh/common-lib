package showfree.commoncore.db;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据库配置基类
 * @author 
 *
 */
public final class DBConfig {

	private String id; //唯一标识
	private String type; //数据库类型
	private String pool; //连接池类型
	private String host = "localhost"; //地址
	private Integer port = 0; //端口号
	private String schema; //库名称
	private String username; //数据库帐号
	private String password; //数据库密码
	private Integer initialSize = 5; //初始化连接数
	private Integer maxSize = 10; //最大连接数
	private Integer maxWait = 30000; //连接最大等待时间，单位是毫秒
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPool() {
		return pool;
	}
	
	public void setPool(String pool) {
		this.pool = pool;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getInitialSize() {
		return initialSize;
	}
	
	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}
	
	public Integer getMaxSize() {
		return maxSize;
	}
	
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
	
	public Integer getMaxWait() {
		return maxWait;
	}
	
	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}
	
	public static DBConfig parse(JSONObject obj) {
		if(obj == null || obj.isEmpty()) {
			return null;
		}
		
		String id = obj.getString("id");
		String type = obj.getString("type");
		String pool = obj.getString("pool");
		String host = obj.getString("host");
		Integer port = obj.getInteger("port");
		String schema = obj.getString("schema");
		String username = obj.getString("username");
		String password = obj.getString("password");
		Integer initialSize = obj.getInteger("initialSize");
		Integer maxSize = obj.getInteger("maxSize");
		Integer maxWait = obj.getInteger("maxWait");
		
		DBConfig config = new DBConfig();
		config.setId(id);
		config.setType(type);
		config.setPool(pool);
		config.setHost(host);
		config.setPort(port == null ? 3306 : port);
		config.setSchema(schema);
		config.setUsername(username);
		config.setPassword(password);
		config.setInitialSize(initialSize == null ? 5 : initialSize);
		config.setMaxSize(maxSize == null ? 10 : maxSize);
		config.setMaxWait(maxWait == null ? 30000 : maxWait);
		
		return config;
	}
}
