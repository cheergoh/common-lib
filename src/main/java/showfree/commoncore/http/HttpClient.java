package showfree.commoncore.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class HttpClient {

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     * @throws Exception
     */
    public static HttpResponse get(String url, Map<String, String> headers) throws Exception {
        return get(url, headers, null);
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param sslsf
     * @return
     * @throws Exception
     */
    public static HttpResponse get(String url, Map<String, String> headers, SSLConnectionSocketFactory sslsf) throws Exception {
        CloseableHttpClient client = sslsf == null ? HttpClients.createDefault() : HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpGet get = new HttpGet(url);

            if (headers != null && headers.size() > 0) {
                Iterator<String> keys = headers.keySet().iterator();
                String key = null;
                String value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = headers.get(key);
                    get.setHeader(key, value == null ? "" : value);
                }
            }

            CloseableHttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResponse(statusCode, statusCode == 200 ? EntityUtils.toString(response.getEntity(), "UTF-8") : null);
        } finally {
            try {
                client.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * post字符串流数据请求
     *
     * @param url
     * @param headers
     * @param str
     * @return
     * @throws Exception
     */
    public static HttpResponse postString(String url, Map<String, String> headers, String str) throws Exception {
        return postString(url, headers, str, null);
    }

    /**
     * post字符串流数据请求
     *
     * @param url
     * @param headers
     * @param str
     * @param sslsf
     * @return
     * @throws Exception
     */
    public static HttpResponse postString(String url, Map<String, String> headers, String str, SSLConnectionSocketFactory sslsf) throws Exception {
        CloseableHttpClient client = sslsf == null ? HttpClients.createDefault() : HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost post = new HttpPost(url);

            if (headers != null && headers.size() > 0) {
                Iterator<String> keys = headers.keySet().iterator();
                String key = null;
                String value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = headers.get(key);
                    post.setHeader(key, value == null ? "" : value);
                }
            }

            StringEntity entity = new StringEntity(str == null ? "" : str, "UTF-8");
            entity.setContentEncoding("UTF-8");
            post.setEntity(entity);

            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResponse(statusCode, statusCode == 200 ? EntityUtils.toString(response.getEntity(), "UTF-8") : null);
        } finally {
            try {
                client.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * post表单请求
     *
     * @param url
     * @param headers
     * @param postData
     * @return
     * @throws Exception
     */
    public static HttpResponse postForm(String url, Map<String, String> headers, Map<String, Object> postData) throws Exception {
        return postForm(url, headers, postData, null);
    }

    /**
     * post表单请求
     *
     * @param url
     * @param headers
     * @param postData
     * @param sslsf
     * @return
     * @throws Exception
     */
    public static HttpResponse postForm(String url, Map<String, String> headers, Map<String, Object> postData, SSLConnectionSocketFactory sslsf) throws Exception {
        CloseableHttpClient client = sslsf == null ? HttpClients.createDefault() : HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost post = new HttpPost(url);

            if (headers != null && headers.size() > 0) {
                Iterator<String> keys = headers.keySet().iterator();
                String key = null;
                String value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = headers.get(key);
                    post.setHeader(key, value == null ? "" : value);
                }
            }

            post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            if (postData != null && postData.size() > 0) {
                List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
                Iterator<String> keys = postData.keySet().iterator();
                String key = null;
                Object value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = postData.get(key);
                    pairList.add(new BasicNameValuePair(key, value == null ? "" : String.valueOf(value)));
                }
                post.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
            }

            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResponse(statusCode, statusCode == 200 ? EntityUtils.toString(response.getEntity(), "UTF-8") : null);
        } finally {
            try {
                client.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * post XML请求
     *
     * @param url
     * @param headers
     * @param xml
     * @return
     * @throws Exception
     */
    public static HttpResponse postXml(String url, Map<String, String> headers, String xml) throws Exception {
        return postForm(url, headers, xml, null);
    }

    /**
     * post XML请求
     *
     * @param url
     * @param headers
     * @param xml
     * @param sslsf
     * @return
     * @throws Exception
     */
    public static HttpResponse postForm(String url, Map<String, String> headers, String xml, SSLConnectionSocketFactory sslsf) throws Exception {
        CloseableHttpClient client = sslsf == null ? HttpClients.createDefault() : HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost post = new HttpPost(url);

            if (headers != null && headers.size() > 0) {
                Iterator<String> keys = headers.keySet().iterator();
                String key = null;
                String value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = headers.get(key);
                    post.setHeader(key, value == null ? "" : value);
                }
            }

            post.setHeader("Content-Type", "application/xml; charset=UTF-8");

            StringEntity entity = new StringEntity(xml == null ? "" : xml, "UTF-8");
            entity.setContentEncoding("UTF-8");
            post.setEntity(entity);

            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResponse(statusCode, statusCode == 200 ? EntityUtils.toString(response.getEntity(), "UTF-8") : null);
        } finally {
            try {
                client.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * post JSON数据请求
     *
     * @param url
     * @param headers
     * @param postData
     * @return
     * @throws Exception
     */
    public static HttpResponse postJson(String url, Map<String, String> headers, JSON postData) throws Exception {
        return postJson(url, headers, postData, null);
    }

    /**
     * post JSON数据请求
     *
     * @param url
     * @param headers
     * @param postData
     * @param sslsf
     * @return
     * @throws Exception
     */
    public static HttpResponse postJson(String url, Map<String, String> headers, JSON postData, SSLConnectionSocketFactory sslsf) throws Exception {
        CloseableHttpClient client = sslsf == null ? HttpClients.createDefault() : HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost post = new HttpPost(url);

            if (headers != null && headers.size() > 0) {
                Iterator<String> keys = headers.keySet().iterator();
                String key = null;
                String value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = headers.get(key);
                    post.setHeader(key, value == null ? "" : value);
                }
            }

            post.setHeader("Content-Type", "application/json;charset=UTF-8");

            StringEntity entity = new StringEntity(postData == null ? "" : postData.toJSONString(), "UTF-8");
            entity.setContentEncoding("UTF-8");
            post.setEntity(entity);

            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResponse(statusCode, statusCode == 200 ? EntityUtils.toString(response.getEntity(), "UTF-8") : null);
        } finally {
            try {
                client.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * post文件请求
     *
     * @param url
     * @param headers
     * @param postData
     * @param fileField
     * @param file
     * @return
     * @throws Exception
     */
    public static HttpResponse postFile(String url, Map<String, String> headers, Map<String, Object> postData, String fileField, File file) throws Exception {
        return postFile(url, headers, postData, fileField, file, null);
    }

    /**
     * post文件请求
     *
     * @param url
     * @param headers
     * @param postData
     * @param fileField
     * @param file
     * @param sslsf
     * @return
     * @throws Exception
     */
    public static HttpResponse postFile(String url, Map<String, String> headers, Map<String, Object> postData, String fileField, File file, SSLConnectionSocketFactory sslsf) throws Exception {
        CloseableHttpClient client = sslsf == null ? HttpClients.createDefault() : HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "multipart/form-data; charset=UTF-8");

            if (headers != null && headers.size() > 0) {
                Iterator<String> keys = headers.keySet().iterator();
                String key = null;
                String value = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    value = headers.get(key);
                    post.setHeader(key, value == null ? "" : value);
                }
            }

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setCharset(Charset.forName("UTF-8"));
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if (postData != null && postData.size() > 0) {
                Iterator<String> keys = postData.keySet().iterator();
                String key = null;
                Object value = null;
                ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
                while (keys.hasNext()) {
                    key = keys.next();
                    value = postData.get(value);
                    entityBuilder.addPart(key, new StringBody(value == null ? "" : String.valueOf(value), contentType));
                }
            }

            if (file != null) {
                entityBuilder.addPart(fileField, new FileBody(file));
            }

            post.setEntity(entityBuilder.build());

            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            return new HttpResponse(statusCode, statusCode == 200 ? EntityUtils.toString(response.getEntity(), "UTF-8") : null);
        } finally {
            try {
                client.close();
            } catch (Exception ex) {
            }
        }
    }

    private HttpClient() {
    }
}
