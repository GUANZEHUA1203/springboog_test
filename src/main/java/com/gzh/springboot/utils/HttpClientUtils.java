package com.gzh.springboot.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月18日
 * @Desc HTTPCLIENT 请求工具包
 */
public class HttpClientUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	private final static String URL_ENCODING = "UTF-8";

	private static final String HTTP = "http";
	
	private static final String HTTPS = "https";
	
	private static SSLConnectionSocketFactory sslsf = null;
	
	private static PoolingHttpClientConnectionManager cm = null;
	
	private static RequestConfig requestConfig;
	
	private static SSLContextBuilder builder = null;
	
	private static String[] tlsArray = new String[] { "TLSv1", "TLSv1.2" };
	
	private static String[] sslArray = new String[] { "SSLv2Hello", "SSLv3" };

	private static final int CONNECT_TIME_OUT = 20000;
	
	private static final int SOCKET_TIME_OUT = 20000;
	
	private static final int CONNECTION_REQUEST_TIMEOUT = 20000;

	static {
		try {
			builder = new SSLContextBuilder();
			// 全部信任
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(), tlsArray, null, NoopHostnameVerifier.INSTANCE);

			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);

			RequestConfig.Builder configBuilder = RequestConfig.custom();
			//设置连接超时
			configBuilder.setConnectTimeout(CONNECT_TIME_OUT);
			//设置读取超时
			configBuilder.setSocketTimeout(SOCKET_TIME_OUT);
			//设置从连接池获取连接实例的超时
			configBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);

			requestConfig = configBuilder.build();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpUrl
	 * @param header
	 * @param params
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String httpUrl, Map<String, String> header, Map<String, Object> params,
			HttpEntity entity) throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		HttpPost httpPost=null;
		try {
			httpClient = getHttpClient();
			httpPost = new HttpPost(httpUrl);
			httpPost.setConfig(requestConfig);
			// 设置头信息
			if (null != header && header.size() != 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Set<Entry<String, Object>> set = params.entrySet();
				Iterator<Entry<String, Object>> iterator = set.iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = entry.getKey();
					String value = entry.getValue() + "";
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			// 设置实体 优先级高
			if (entity != null) {
				httpPost.setEntity(entity);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
			logger.info(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String httpUrl, Map<String, Object> params) throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpUrl);
			httpPost.setConfig(requestConfig);
			// 设置请求参数
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Set<Entry<String, Object>> set = params.entrySet();
				Iterator<Entry<String, Object>> iterator = set.iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = entry.getKey();
					String value = entry.getValue() + "";
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
			logger.info(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年5月6日
	 * @Desc 文件上传
	 *
	 * @param httpUrl
	 * @param file
	 * @param params
	 * @return
	 */
	public static String httpUploadFile(String httpUrl,MultipartFile file, Map<String, Object> params)
			throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		try {
			String fileName = file.getOriginalFilename();
			HttpPost httpPost = new HttpPost(httpUrl);
			httpPost.setConfig(requestConfig);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
			builder.addTextBody("filename", fileName);
			if (params.size() != 0) {
				for (String key : params.keySet()) {
					builder.addTextBody(key, String.valueOf(params.get(key)));
				}
			}
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
			}
			logger.info(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpUrl
	 * @param header
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String httpUrl, Map<String, String> header, Map<String, Object> params)
			throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();

			HttpPost httpPost = new HttpPost(httpUrl);
			httpPost.setConfig(requestConfig);
			// 设置头信息
			if (null != header && header.size() != 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Set<Entry<String, Object>> set = params.entrySet();
				Iterator<Entry<String, Object>> iterator = set.iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = entry.getKey();
					String value = entry.getValue() + "";
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
			logger.info(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月20日
	 * @Desc
	 *
	 * @param httpUrl 请求地址
	 * @param params  请求头参数
	 * @return
	 * @throws Exception
	 */
	public static String httpGet(String httpUrl, Map<String, Object> params) throws Exception {
		StringBuffer param = new StringBuffer();
		int i = 0;
		HttpGet httpGet=null;
		for (String key : params.keySet()) {
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		httpUrl += param;
		String result = null;
		CloseableHttpClient httpClient = getHttpClient();
		try {
			httpGet = new HttpGet(httpUrl);
			httpGet.setConfig(requestConfig);
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(response);
			}
			logger.info(result);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	public static CloseableHttpClient getHttpClient() throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
				.setConnectionManagerShared(true).build();

		return httpClient;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String readHttpResponse(HttpResponse httpResponse) throws ParseException, IOException {
		StringBuilder builder = new StringBuilder();
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		
		logger.info("请求响应异常:"+EntityUtils.toString(entity));
		// 响应状态
		builder.append("status:" + httpResponse.getStatusLine());
		builder.append("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			builder.append("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			builder.append("response length:" + responseString.length());
			builder.append("response content:" + responseString.replace("\r\n", ""));
		}
		return builder.toString();
	}
	public static String doPostEntity(String url, String stringEntity) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(new BasicHeader("Content-type", "application/json;charset=utf-8"));
		CloseableHttpResponse response = null;
		String ret = null;
		try {
			httpPost.setEntity(new StringEntity(stringEntity, URL_ENCODING));
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			ret = EntityUtils.toString(entity, URL_ENCODING);
			EntityUtils.consume(entity);
		} catch (UnsupportedEncodingException e) {
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (null != response)
					response.close();
			} catch (IOException e) {
			}
		}
		return ret;
	}
	
	public static String test(String httpUrl, Map<String, Object> params) throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpUrl);
			httpPost.setConfig(requestConfig);
			// 设置请求参数
			if (params != null && params.size() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Set<Entry<String, Object>> set = params.entrySet();
				Iterator<Entry<String, Object>> iterator = set.iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = entry.getKey();
					String value = entry.getValue() + "";
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
			logger.info(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
//		String httpUrl="http://dr.ice.test.colourlife.com/v1.0/own/device/list?uuid=353be70f-e28c-4393-9352-7417fa05e1b0";
//		 httpUrl="http://dr.ice.test.colourlife.com/v1.0/own/device/open";
		 String httpUrl="https://47.104.220.61/v2.0/device/list";
		try {
			Map<String,Object> params=new HashMap<String, Object>();
			params.put("villageId", "bcfe0f35-37b0-49cf-a73d-ca96914a46a5");
//			params.put("openType", 2);
//			params.put("uuid", "fec00ec2-c323-4e5a-8c75-1a0cacc8f628");
//			params.put("uuid", "353be70f-e28c-4393-9352-7417fa05e1b0");
//			params.put("openSp", 1);
			String str=HttpClientUtils.httpPost(httpUrl,params);
//			String str=HttpColourLifeServer.getInstince().httpAuth(); 
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}