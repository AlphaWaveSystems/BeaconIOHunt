package com.alphaws.javadaytrivia.json;

import com.alphaws.javadaytrivia.tools.Commons;

import java.io.IOException;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class JSonClientBackUp {

	public HttpClient getNewHttpClient() {
		KeyStore trustStore;
		SSLSocketFactory sslFactory;
		try {

			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sslFactory = new MySSLSocketFactory(trustStore);
			sslFactory
					.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 30000);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sslFactory, 443));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			return new DefaultHttpClient(ccm, params);

		} catch (Exception e) {
			return new DefaultHttpClient();
		} finally {
			trustStore = null;
			sslFactory = null;
		}
	}

	public JSONObject getJSONFromURL(String url)
			throws ClientProtocolException, IOException, JSONException,
			Exception {

		HttpResponse r = makeGetRequest(url);
		JSONObject oJSONObject;
		int status = r.getStatusLine().getStatusCode();
		if (status == 200) {
			oJSONObject = new JSONObject(EntityUtils.toString(r.getEntity()));
			return oJSONObject;
		} else {
			return null;
		}
	}

	public HttpResponse makeGetRequest(String path) throws IOException,ClientProtocolException {

		HttpClient oHttpClient = getNewHttpClient();

		HttpGet httpget = new HttpGet(path);
		httpget.setHeader("Accept", "application/json");
		String userAgent = "Android" + "|" + android.os.Build.VERSION.RELEASE
				+ "|" + Commons.CARRIER + "|" + android.os.Build.MANUFACTURER
				+ "|" + android.os.Build.MODEL;
		httpget.setHeader("User-Agent", userAgent);

		return oHttpClient.execute(httpget);
	}
}
