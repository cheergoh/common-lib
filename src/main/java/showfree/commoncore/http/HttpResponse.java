package showfree.commoncore.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import showfree.commoncore.tools.StringTool;

import java.io.StringReader;

/**
 * http请求响应对象
 * @author 
 *
 * @param <T>
 */
public final class HttpResponse<T> {
	
	private static Logger log = Logger.getLogger(HttpResponse.class); //日志

	private int statusCode; //http状态码
	private String data; //原始响应数据
	
	protected HttpResponse() {}
	
	protected HttpResponse(int statusCode) {
		this.statusCode = statusCode;
	}
	
	protected HttpResponse(int statusCode, String data) {
		this.statusCode = statusCode;
		this.data = data;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	protected void setStatusCode(int statucCode) {
		this.statusCode = statusCode;
	}
	
	public String getString() {
		return data;
	}
	
	public JSONObject getJSONObject() {
		try {
			return StringTool.isEmpty(data) ? null : JSON.parseObject(data);
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	public JSONArray getJSONArray() {
		try {
			return StringTool.isEmpty(data) ? null : JSON.parseArray(data);
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	public Document getDocument() {
		try {
			return new SAXReader().read(new StringReader(data));
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	public Element getRootElement() {
		try {
			return new SAXReader().read(new StringReader(data)).getRootElement();
		}catch(Exception ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	protected void setData(String data) {
		this.data = data;
	}
}
