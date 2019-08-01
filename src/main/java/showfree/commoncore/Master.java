package showfree.commoncore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import showfree.commoncore.db.DBConfig;
import showfree.commoncore.db.DBPool;
import showfree.commoncore.db.DBSession;
import showfree.commoncore.db.DBTransaction;
import showfree.commoncore.db.support.DBTypeFactory;
import showfree.commoncore.db.support.DBTypeSupport;
import showfree.commoncore.http.HttpResponse;
import showfree.commoncore.tools.StringTemplate;
import showfree.commoncore.tools.StringTool;
import showfree.commoncore.ric.RemoteCall;
import showfree.commoncore.ric.RemoteCallAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.Security;
import java.util.*;

public final class Master {

    private static Logger log = Logger.getLogger(Master.class); //日志

    private static boolean hasStartup = false; //是否已经启动

    private static String userPath; //用户启动路径
    private static String packageRootPath; //类包根路径

    private static Map<Class, Object> singleInstanceLookup = new HashMap<>(); //单实例查找表

    private static Map<String, JSON> configLookup = new HashMap<>(); //配置对象查找表
    private static JSONObject defines = new JSONObject(); //全局参数定义
    private static Map<String, DBPool> dbPoolLookup = new HashMap<>(); //数据库连接池查找表

    private static Map<String, RemoteCall> remoteCallLookup = new HashMap<>(); //远程接口调用查找表

    public static Object getSingleInstance(Class cls) {
        return cls != null ? singleInstanceLookup.get(cls) : null;
    }

    public static Object getSingleInstnaceOrNew(Class cls) throws Exception {
        Object instance = singleInstanceLookup.get(cls);
        return instance == null ? cls.newInstance() : instance;
    }

    public static void setSingleInstance(Class cls, Object instance) {
        if (cls != null && instance != null) {
            singleInstanceLookup.put(cls, instance);
        }
    }

    public static JSON getConfig(String name) {
        return configLookup.get(name);
    }

    public static List<JSON> getConfigsStartsWith(String prefix) {
        List<JSON> list = new ArrayList<JSON>();

        Iterator<String> names = configLookup.keySet().iterator();
        String name = null;
        while (names.hasNext()) {
            name = names.next();
            if (name.startsWith(prefix)) {
                list.add(configLookup.get(name));
            }
        }

        return list;
    }

    public static String getStringDefine(String name) {
        return defines.getString(name);
    }

    public static String getStringDefine(String name, String defaultValue) {
        String value = defines.getString(name);
        if (StringTool.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

    public static Boolean getBooleanDefine(String name) {
        return defines.getBoolean(name);
    }

    public static Boolean getBooleanDefine(String name, Boolean defaultValue) {
        Boolean value = defines.getBoolean(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static Integer getIntDefine(String name) {
        return defines.getInteger(name);
    }

    public static Integer getIntDefine(String name, Integer defaultValue) {
        Integer value = defines.getInteger(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static Double getDoubleDefine(String name) {
        return defines.getDouble(name);
    }

    public static Double getDoubleDefine(String name, Double defaultValue) {
        Double value = defines.getDouble(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static JSONObject getJSONObjectDefine(String name) {
        return defines.getJSONObject(name);
    }

    public static JSONArray getJSONArrayDefine(String name) {
        return defines.getJSONArray(name);
    }

    public static DBPool getDBPool(String id) {
        return dbPoolLookup.get(id);
    }

    public static DBSession getDBSession(String id) {
        DBPool pool = dbPoolLookup.get(id);
        return pool != null ? new DBSession(pool) : null;
    }

    public static DBTransaction getDBTransaction(String id) {
        DBPool pool = dbPoolLookup.get(id);
        return pool != null ? new DBTransaction(pool) : null;
    }

    public static RemoteCall getRemoteCall(String id) {
        return remoteCallLookup.get(id);
    }

    public static HttpResponse remoteCall(String id, String apiId) throws Exception {
        return remoteCall(id, apiId, null, null, null, null, null);
    }

    public static HttpResponse remoteCall(String id, String apiId, Object data) throws Exception {
        return remoteCall(id, apiId, null, null, data, null, null);
    }

    public static HttpResponse remoteCall(String id, String apiId, Map<String, String> uriMapping, Object data) throws Exception {
        return remoteCall(id, apiId, uriMapping, null, data, null, null);
    }

    public static HttpResponse remoteCall(String id, String apiId, Map<String, String> uriMapping, Map<String, String> headers, Object data) throws Exception {
        return remoteCall(id, apiId, uriMapping, headers, data, null, null);
    }

    public static HttpResponse remoteCall(String id, String apiId, Map<String, String> uriMapping, Map<String, String> headers, Object data, String fileField, File
            file) throws Exception {
        RemoteCall remote = remoteCallLookup.get(id);
        return remote != null ? remote.call(apiId, uriMapping, headers, data, fileField, file) : null;
    }

    /**
     * 加载json文件
     */
    private static void loadConfigs() {
        File[] files = new File(packageRootPath).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("fetch json config :" + file.getName());
                if (file.getName().lastIndexOf(".json") > 0) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        StringBuffer sb = new StringBuffer();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        configLookup.put(file.getName().substring(0, file.getName().length() - 5), (JSON) JSON.parse(sb.toString()));
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
        }

        try {
            JSONObject masterConfig = (JSONObject) configLookup.get("master");
            if (masterConfig != null && !masterConfig.isEmpty()) {
                String classPath = masterConfig.getString("classPath");
                if (!StringTool.isEmpty(classPath)) {
                    packageRootPath += classPath;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        try {
            defines = (JSONObject) configLookup.get("defines");
            if (defines == null) {
                defines = new JSONObject();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 加载数据库
     */
    private static void loadDatabases() {
        JSONArray list = (JSONArray) getConfig("db");
        if (list == null || list.isEmpty()) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            DBConfig config = DBConfig.parse(list.getJSONObject(i));
            if (config == null || StringTool.isEmpty(config.getId())) {
                continue;
            }

            DBTypeFactory factory = DBTypeSupport.getFactory(config.getType());
            if (factory == null) {
                continue;
            }

            try {
                DBPool pool = factory.getPool(config);
                if (pool != null) {
                    dbPoolLookup.put(config.getId(), pool);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 加载http配置
     */
    private static void loadRics() {
        List<JSON> list = Master.getConfigsStartsWith("ric-");
        list.add(Master.getConfig("ric"));
        for (int i = 0; i < list.size(); i++) {
            JSONArray arr = (JSONArray) list.get(i);
            if (arr == null || arr.isEmpty()) {
                continue;
            }

            for (int j = 0; j < arr.size(); j++) {
                JSONObject config = arr.getJSONObject(j);
                if (config == null || config.isEmpty()) {
                    continue;
                }

                String id = config.getString("id");
                String host = config.getString("host");
                JSONObject defines = config.getJSONObject("defines");
                JSONObject headers = config.getJSONObject("headers");
                JSONArray apiList = config.getJSONArray("api");

                if (StringTool.isEmpty(id)) {
                    continue;
                }
                StringTemplate t = new StringTemplate(host);
                for (String name : t.getVariables()) {
                    t.setVariable(name, Master.getStringDefine(name));
                }
                host = t.toString();
                if (headers != null && !headers.isEmpty()) {
                    Iterator<String> keys = headers.keySet().iterator();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Object value = headers.get(key);
                        if (value != null && value instanceof String) {
                            t = new StringTemplate((String) value);
                            for (String name : t.getVariables()) {
                                t.setVariable(name, Master.getStringDefine(name, ""));
                            }
                            headers.put(key, t.toString());
                        }
                    }
                }
                RemoteCall rc = new RemoteCall();
                rc.setId(id);
                rc.setHost(host);
                rc.setDefines(defines);
                rc.setHeaders(headers);
                if (apiList != null && !apiList.isEmpty()) {
                    for (int x = 0; x < apiList.size(); x++) {
                        JSONObject api = apiList.getJSONObject(x);
                        if (api == null || api.isEmpty()) {
                            continue;
                        }
                        id = api.getString("id");
                        String uri = api.getString("uri");
                        String method = api.getString("method");

                        if (StringTool.isEmpty(id) || StringTool.isEmpty(method)) {
                            continue;
                        }
                        method = method.toLowerCase();
                        if (!method.equals("get") && !method.equals("form") && !method.equals("json") && !method.equals("file")) {
                            continue;
                        }
                        RemoteCallAPI rca = new RemoteCallAPI();
                        rca.setId(id);
                        rca.setUri(uri);
                        rca.setMethod(method);
                        rc.add(rca);
                    }
                }
                remoteCallLookup.put(rc.getId(), rc);
            }
        }
    }

    public Master() {
    }

    public static void startup() {
        if (!hasStartup) {
            Security.addProvider(new BouncyCastleProvider());
            userPath = System.getProperty("user.dir") + "/";
            System.out.println("fetch userPath:" + userPath);
            packageRootPath = Master.class.getResource("/").getPath();
            System.out.println("fetch packageRootPath:" + packageRootPath);
            loadConfigs();
            loadDatabases();
            loadRics();
            hasStartup = true;

        }
    }
}
