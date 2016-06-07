package com.cwang.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class HttpSender {
	private static Map<String, String> header = new HashMap<String, String>();
	private static Map<String, String> param = new HashMap<String, String>();
	
	//default header
	static{
		header.put("accept", "*/*");
		header.put("connection", "Keep-Alive");
		header.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		header.put("Accept-Charset", "utf-8");		
		
		param.put("username", "wang");
		param.put("password", "123456");
	}
	
	public static void main(String[] args){
		String result = "";
		//result = sendGet_URLConnection("http://localhost:5000", header, param);
		//System.out.println(result);
		
		//result = sendPost_URLConnection("http://localhost:5000", header, param);
		//System.out.println(result);
		
		
		//result = sendGet_HttpClient("http://localhost:5000", header, param); 
		//System.out.println(result);
		
		//result = sendPost_HttpClient("http://localhost:5000", header, param);
		//System.out.println(result);
	}

	public static void httpGet(){
		
	}
	
	public static void httpPost(){
		
	}
	
	/**
	 * 从URL中解析出协议、主机地址、资源路径
	 * @param url	不带参数的URL地址
	 * @return		返回Map，包含 {<"scheme" : 协议>, <"host" ：机 >, <"path" : 资源路径>}
	 */
	public static Map<String, String> decodeURL(String url){
		Map<String, String> result= new HashMap<String, String>();
		
		String[] urls =  url.split("://");
		String scheme = urls[0];
		String[] rest =  urls[1].split("/");
		String host = rest[0];
		String path = "";
		if(rest.length > 1){
			path = "/" + rest[1];
		}
		
		result.put("scheme", scheme);
		result.put("host", host);
		result.put("path", path);
		
		return result;
	}
	
	/**
	 * 基于HttpClient的Http GET请求
	 * @param strurl	基本URL
	 * @param header	Http头部属性
	 * @param param		Http请求参数
	 * @return			返回JSON字符串，包含htmlhead和htmlbody两部
	 * @return
	 * @throws IOException 
	 */
	public static String sendGet_HttpClient(String url, Map<String, String> header, Map<String, String> param){	
		CloseableHttpClient httpClient = null;
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		
		try{
			//构建请求参数列表
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();	
			Set<String> keys;
			if(param != null && param.size() > 0){
				keys = param.keySet();
				for(String key : keys){
					pairs.add(new BasicNameValuePair(key, param.get(key)));		
				}
			}
			
			//从URL中解析出协议、主机地址、资源路径
			Map<String, String> decodedURL = decodeURL(url);	
			//构建URI，设置协议类型、主机地址、资源路径以及请求参数
			URI uri = new URIBuilder().setScheme(decodedURL.get("scheme")).setHost(decodedURL.get("host")).setPath(decodedURL.get("path")).setParameters(pairs).build();
			
			//创建HttpGet
			httpGet = new HttpGet(uri);
			
			//创建HttpClient
			httpClient = HttpClients.createDefault();
			
			//设置request header														
			if(header != null && header.size() > 0){
				keys = header.keySet();
				for(String key : keys){
					httpGet.addHeader(key, header.get(key));
				}
			}
			
			//执行request，获取response
			response = httpClient.execute(httpGet);
			//获取response header
			Header[] responsehead = response.getAllHeaders();
			//获取response body
			HttpEntity responseBody = response.getEntity();
			
			//构建JSON串
			JSONObject headjson = new JSONObject();
			for(Header head : responsehead){
				headjson.put(head.getName(), head.getValue());
			}
			
			Map<String, String> headmap = new HashMap<String, String>();
			headmap.put("htmlhead", headjson.toString());
			
			Map<String, String> bodymap = new HashMap<String, String>();
			bodymap.put("htmlbody", EntityUtils.toString(responseBody,"UTF-8"));
			
			JSONArray jarray = new JSONArray();
			jarray.add(JSONObject.fromObject(headmap));
			jarray.add(JSONObject.fromObject(bodymap));
			
			return jarray.toString();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// 关闭连接,释放资源                        
            if(httpGet != null){
            	httpGet.releaseConnection();
            }            
		}
			
		return null;
	}
	
	/**
	 * 基于HttpClient的Http POST请求
	 * @param url		基本URL
	 * @param header	Http头部属性
	 * @param param		Http请求参数
	 * @return			返回JSON字符串，包含htmlhead和htmlbody两部分
	 */
	public static String sendPost_HttpClient(String url, Map<String, String> header, Map<String, String> param){	
		//创建HttpClient，设置重定向策略LaxRedirectStrategy: This strategy relaxes restrictions on automatic redirection of POST methods imposed by the HTTP specification.
		HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();		
	
		//创建HttpPost
		HttpPost httpPost = new HttpPost(url);
		
		HttpResponse response = null;
		try{
			//设置request header
			Set<String> keys;
			if(header != null && header.size() > 0){
				keys = header.keySet();
				for(String key : keys){
					httpPost.addHeader(key, header.get(key));
				}
			}
					
			//设置请求request body
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();		
			if(param != null && param.size() > 0){
				keys = param.keySet();
				for(String key : keys){
					pairs.add(new BasicNameValuePair(key, param.get(key)));
				}
			}
						
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(pairs, "UTF-8");  			
			httpPost.setEntity(uefEntity);
			
			//执行request,获取response
			response = httpClient.execute(httpPost);
			
			//获取response header
			Header[] responsehead = response.getAllHeaders();
			
			//获取response body
			HttpEntity responseBody = response.getEntity();
			
			//构建JSON串
			JSONObject headjson = new JSONObject();
			for(Header head : responsehead){
				headjson.put(head.getName(), head.getValue());
			}
			
			Map<String, String> headmap = new HashMap<String, String>();
			headmap.put("htmlhead", headjson.toString());
			
			Map<String, String> bodymap = new HashMap<String, String>();
			bodymap.put("htmlbody", EntityUtils.toString(responseBody,"UTF-8"));
			
			JSONArray jarray = new JSONArray();
			jarray.add(JSONObject.fromObject(headmap));
			jarray.add(JSONObject.fromObject(bodymap));
			
			return jarray.toString();			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// 关闭连接,释放资源    
			if(httpPost != null){
				httpPost.releaseConnection();
			}						         			
		}
		
		return null;
	}
	
	
	/**
	 * 基于URLConnection的Http GET请求
	 * @param strurl	基本URL
	 * @param header	Http头部属性
	 * @param param		Http请求参数
	 * @return			返回JSON字符串，包含htmlhead和htmlbody两部分
	 */
	public static String sendGet_URLConnection(String strurl, Map<String, String> header, Map<String, String> param) {				
		BufferedReader in = null;
		try {
			String fullUrl = null;
			
			//将参数附加到URL上
			if (param == null || param.isEmpty()) {
				fullUrl = strurl;
			} else {
				fullUrl = strurl + "?";
				Iterator iter = param.entrySet().iterator();
				String name;
				String value;
				while (iter.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) iter.next();
					name = entry.getKey();
					value = entry.getValue();

					fullUrl += name + "=" + value + "&";
				}
				fullUrl = fullUrl.substring(0, fullUrl.length() - 1);
			}

			URL url = new URL(fullUrl);
			URLConnection connection = url.openConnection();
			
			// 设置通用的请求属性
			if(header != null || header.size() > 0){
				Set<String> keys = header.keySet();
				for(String key : keys){
					connection.setRequestProperty(key, header.get(key));
				}
			}	
			
			// 建立实际的连接
			connection.connect();
						
			// 获取所有响应头字段
			Map<String, List<String>> head = connection.getHeaderFields();
			
			Set<String> keyset = head.keySet();
			
			Map<String, List<String>> temp = new HashMap<String, List<String>>();
			
			for(String key : keyset){
				if(key != null){
					temp.put(key, head.get(key));
				}else{
					temp.put("status", head.get(key));
				}					
			}
			
			JSONObject headjson = JSONObject.fromObject(temp);
			
			Map<String, String> headmap = new HashMap<String, String>();
			headmap.put("htmlhead", headjson.toString());
						
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;												
			String bodystr = "";
			while ((line = in.readLine()) != null) {
				bodystr += line;	// + System.getProperty("line.separator");				
			}
			Map<String, String> bodymap = new HashMap<String, String>();
			bodymap.put("htmlbody", bodystr);
			
			JSONArray jsarray = new JSONArray();									
			jsarray.add(JSONObject.fromObject(headmap));
			jsarray.add(JSONObject.fromObject(bodymap));
								
			return jsarray.toString();			
		} catch (Exception e) {
			System.out.println("发送 GET请求出现异常！");
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}		
		return null;
	}

	/**
	 * 基于URLConnection的Http POST请求
	 * @param url		基本URL
	 * @param header	Http头部属性
	 * @param param		Http请求参数
	 * @return			返回JSON字符串，包含htmlhead和htmlbody两部分
	 */
	public static String sendPost_URLConnection(String url, Map<String, String> header, Map<String, String> param) {		
		PrintWriter out = null;
		BufferedReader in = null;
		String paramStr = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			
			// 设置通用的请求属性
			if(header != null || header.size() > 0){
				Set<String> keys = header.keySet();
				for(String key : keys){
					conn.setRequestProperty(key, header.get(key));
				}
			}	
			
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			
			// 发送请求参数
			if (param == null || param.isEmpty()) {
				paramStr = "";
			} else {
				Iterator iter = param.entrySet().iterator();
				String name;
				String value;
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
					name = entry.getKey();
					value = entry.getValue();

					paramStr += name + "=" + value + "&";
				}
				paramStr = paramStr.substring(0, paramStr.length() - 1);
			}
			out.print(paramStr);
			
			// flush输出流的缓冲
			out.flush();
						
			// 获取所有响应头字段
			Map<String, List<String>> head = conn.getHeaderFields();
			
			Set<String> keyset = head.keySet();
			
			Map<String, List<String>> temp = new HashMap<String, List<String>>();
			
			for(String key : keyset){
				if(key != null){
					temp.put(key, head.get(key));
				}else{
					temp.put("status", head.get(key));
				}					
			}
			
			JSONObject headjson = JSONObject.fromObject(temp);
			
			Map<String, String> headmap = new HashMap<String, String>();
			headmap.put("htmlhead", headjson.toString());
			
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String bodystr = "";
			while ((line = in.readLine()) != null) {
				bodystr += line; //+ System.getProperty("line.separator");
			}			
			Map<String, String> bodymap = new HashMap<String, String>();
			bodymap.put("htmlbody", bodystr);
			
			JSONArray jsarray = new JSONArray();									
			jsarray.add(JSONObject.fromObject(headmap));
			jsarray.add(JSONObject.fromObject(bodymap));
			
			return jsarray.toString();
		} catch (Exception e) {
			System.out.println("发送 POST请求出现异常！");
			e.printStackTrace();
		}finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
}
