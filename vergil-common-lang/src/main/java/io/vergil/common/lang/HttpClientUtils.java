package io.vergil.common.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

/**
 * 调用时需要自己引入apache httpclient相关依赖
 * 
 * @author zhaowei
 * @date 2016年3月2日下午1:34:22
 */
@SuppressWarnings("deprecation")
public class HttpClientUtils {
	protected static final String DEFAULT_CHARSET = "UTF-8";
	protected static HttpClient httpClient;

	/**
	 * 默认的从manager取到连接的时间
	 */
	protected static final int DEFAULT_REQUEST_TIMEOUT = 2000;

	/**
	 * 默认的请求连接时间
	 */
	public static final int DEFAULT_CONN_TIMEOUT = 2000;

	/**
	 * 默认的socket数据包间隔时间
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 60000;

	/**
	 * 连接池同时最大执行数量
	 */
	protected static final int POOL_MAX = 200;
	/**
	 * 连接池每个连接的最大路由次数
	 */
	protected static final int POOL_MAX_PERROUTE = 10;

	static {
		httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
				.setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONN_TIMEOUT).build();
		// https验证
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
		HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(POOL_MAX);
		cm.setDefaultMaxPerRoute(POOL_MAX_PERROUTE);
		httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
	}

	/**
	 * delete方法
	 * 
	 * @author zhaowei
	 * @date 2015年9月26日下午4:18:20
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String delete(String url) throws IOException {
		HttpEntity entity = delete2(url).getEntity();
		InputStream is = entity.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * delete 方法
	 * 
	 * @author zhaowei
	 * @date 2015年9月26日下午4:16:17
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse delete2(String url) throws IOException {
		HttpClient client = getHttpClient();
		HttpDelete delete = new HttpDelete(url.trim());
		HttpResponse response = client.execute(delete);
		return response;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月30日下午1:56:54
	 * @param connTimeout
	 *            连接超时时间
	 * @param socketTimeout
	 *            响应超时时间
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String delete(int connTimeout, int socketTimeout, String url) throws IOException {
		HttpClient client = getHttpClient();
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout)
				.build();
		HttpDelete delete = new HttpDelete(url.trim());
		delete.setConfig(config);
		HttpResponse response = client.execute(delete);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * get请求，返回string
	 * 
	 * @author zhaowei
	 * @date 2015年9月7日下午5:14:35
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		HttpEntity entity = get2(url).getEntity();
		InputStream is = entity.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * get请求,返回HttpResponse
	 * 
	 * @author zhaowei
	 * @date 2015年9月7日下午5:08:31
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse get2(String url) throws IOException {
		HttpClient client = getHttpClient();
		HttpGet get = new HttpGet(url.trim());
		HttpResponse response = client.execute(get);
		return response;
	}

	/**
	 * get方法
	 * 
	 * @author zhaowei
	 * @date 2015年9月30日下午1:50:48
	 * @param connTimeout
	 *            连接超时时间
	 * @param socketTimeout
	 *            响应超时时间
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(int connTimeout, int socketTimeout, String url) throws IOException {
		HttpClient client = getHttpClient();
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout)
				.build();
		HttpGet get = new HttpGet(url.trim());
		get.setConfig(config);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * 获取httpclient
	 * 
	 * @author zhaowei
	 * @date 2015年9月7日下午5:18:23
	 * @return
	 */
	public static HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月29日上午10:51:14
	 * @param url
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, HttpEntity entity) throws IOException {
		HttpEntity entity_ = post2(url, entity).getEntity();
		InputStream is = entity_.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月29日上午10:47:01
	 * @param url
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse post2(String url, HttpEntity entity) throws IOException {
		HttpPost post = new HttpPost(url.trim());
		if (entity != null) {
			post.setEntity(entity);
		}
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(post);
		return response;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月30日下午2:01:29
	 * @param connTimeout
	 *            连接超时时间
	 * @param socketTimeout
	 *            响应超时时间
	 * @param entity
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String post(int connTimeout, int socketTimeout, String url, HttpEntity entity) throws IOException {
		HttpPost post = new HttpPost(url.trim());
		if (entity != null) {
			post.setEntity(entity);
		}
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout)
				.build();
		post.setConfig(config);
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(post);
		HttpEntity entity_ = response.getEntity();
		InputStream is = entity_.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月30日下午2:03:51
	 * @param connTimeout
	 *            连接超时时间
	 * @param socketTimeout
	 *            响应超时时间
	 * @param url
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	public static String put(int connTimeout, int socketTimeout, String url, HttpEntity entity) throws IOException {
		HttpPut put = new HttpPut(url.trim());
		if (entity != null) {
			put.setEntity(entity);
		}
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout)
				.build();
		put.setConfig(config);
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(put);
		HttpEntity entity_ = response.getEntity();
		InputStream is = entity_.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月29日上午10:57:37
	 * @param url
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	public static String put(String url, HttpEntity entity) throws IOException {
		HttpEntity entity_ = put2(url, entity).getEntity();
		InputStream is = entity_.getContent();
		String str = IOUtils.toString(is, DEFAULT_CHARSET);
		IOUtils.closeQuietly(is);
		return str;
	}

	/**
	 * @author zhaowei
	 * @date 2015年9月29日上午10:56:08
	 * @param url
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	public static HttpResponse put2(String url, HttpEntity entity) throws IOException {
		HttpPut put = new HttpPut(url.trim());
		if (entity != null) {
			put.setEntity(entity);
		}
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(put);
		return response;
	}

	/**
	 * 获得MultipartEntity,Object参数可以是String,byte[],Inputstream,File
	 * 
	 * @author zhaowei
	 * @date 2015年10月9日下午2:57:51
	 * @param param
	 * @return
	 */
	public static HttpEntity buildMultipartEntity(Map<String, Object> param) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		if (param != null && !param.isEmpty()) {
			for (Entry<String, Object> entry : param.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					builder.addTextBody(key, (String) value);
				} else if (value instanceof byte[]) {
					builder.addBinaryBody(key, (byte[]) value);
				} else if (value instanceof InputStream) {
					builder.addBinaryBody(key, (InputStream) value);
				} else if (value instanceof File) {
					builder.addBinaryBody(key, (File) value);
				}
			}
			return builder.build();
		}
		return null;
	}
}
