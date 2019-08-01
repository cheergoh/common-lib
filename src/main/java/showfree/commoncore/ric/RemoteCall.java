package showfree.commoncore.ric;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import showfree.commoncore.http.HttpClient;
import showfree.commoncore.http.HttpResponse;
import showfree.commoncore.tools.StringTemplate;
import showfree.commoncore.tools.StringTool;

import java.io.File;
import java.util.*;

public final class RemoteCall {

    private String id;
    private String host;

    private JSONObject defines;
    private JSONObject headers;

    private List<RemoteCallAPI> apiList = new ArrayList<RemoteCallAPI>();
    private Map<String, RemoteCallAPI> apiLookup = new HashMap<String, RemoteCallAPI>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public JSONObject getDefines() {
        return defines;
    }

    public void setDefines(JSONObject defines) {
        this.defines = defines;
    }

    public JSONObject getHeaders() {
        return headers;
    }

    public void setHeaders(JSONObject headers) {
        this.headers = headers;
    }

    public void add(RemoteCallAPI api) {
        if (api == null || StringTool.isEmpty(api.getId())) {
            return;
        }

        apiList.add(api);
        apiLookup.put(api.getId(), api);
    }

    public HttpResponse call(String id) throws Exception {
        return call(id, null, null, null, null, null);
    }

    public HttpResponse call(String id, Object data) throws Exception {
        return call(id, null, null, data, null, null);
    }

    public HttpResponse call(String id, Map<String, String> uriMapping, Object data) throws Exception {
        return call(id, uriMapping, null, data, null, null);
    }

    public HttpResponse call(String id, Map<String, String> uriMapping, Map<String, String> headers, Object data) throws Exception {
        return call(id, uriMapping, headers, data, null, null);
    }

    public HttpResponse call(String id, Map<String, String> uriMapping, Map<String, String> headers, Object data, String fileField, File file) throws Exception {
        if (StringTool.isEmpty(id)) {
            return null;
        }

        RemoteCallAPI api = apiLookup.get(id);
        if (api == null || StringTool.isEmpty(api.getMethod())) {
            return null;
        }

        StringTemplate uri = new StringTemplate(api.getUri());

        if (defines != null && !defines.isEmpty()) {
            for (String name : uri.getVariables()) {
                uri.setVariable(name, defines.getString(name));
            }
        }

        if (uriMapping != null && !uriMapping.isEmpty()) {
            for (String name : uri.getVariables()) {
                uri.setVariable(name, uriMapping.get(name));
            }
        }

        String url = host + uri;

        if (this.headers != null && !this.headers.isEmpty()) {
            if (headers == null) {
                headers = new HashMap<String, String>();
                Iterator<String> keys = this.headers.keySet().iterator();
                String key = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    headers.put(key, this.headers.getString(key));
                }
            }
        }

        switch (api.getMethod().toLowerCase()) {
            case "get":
                return HttpClient.get(url, headers);
            case "form":
                if (data != null && data instanceof JSONObject) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject obj = (JSONObject) data;
                    Iterator<String> keys = obj.keySet().iterator();
                    String key = null;
                    while (keys.hasNext()) {
                        key = keys.next();
                        map.put(key, obj.get(key));
                    }
                    data = map;
                }
                return HttpClient.postForm(url, headers, (Map) data);
            case "xml":
                return HttpClient.postXml(url, headers, (String) data);
            case "json":
                if (data != null) {
                    if (data instanceof Map) {
                        JSONObject obj = new JSONObject();
                        Map map = (Map) data;
                        Iterator<String> keys = map.keySet().iterator();
                        String key = null;
                        while (keys.hasNext()) {
                            key = keys.next();
                            obj.put(key, map.get(key));
                        }
                        data = obj;
                    } else if (data instanceof List) {
                        JSONArray arr = new JSONArray();
                        List list = (List) data;
                        for (Object obj : list) {
                            arr.add(obj);
                        }
                        data = arr;
                    }
                }
                return HttpClient.postJson(url, headers, (JSON) data);
            case "file":
                if (data != null && data instanceof JSONObject) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject obj = (JSONObject) data;
                    Iterator<String> keys = obj.keySet().iterator();
                    String key = null;
                    while (keys.hasNext()) {
                        key = keys.next();
                        map.put(key, obj.get(key));
                    }
                    data = map;
                }
                return HttpClient.postFile(url, headers, (Map) data, fileField, file);
        }

        return null;
    }
}
