package mss.httpService.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * 
 * @author Juan Aguilar Guisado
 * @since 1.0
 * 
 */
public class RESTIntentService extends IntentService {

	private static final String TAG = RESTIntentService.class.getName();

	public static final int GET = 0x1;
	public static final int POST = 0x2;
	public static final int PUT = 0x3;
	public static final int DELETE = 0x4;

	public static final String EXTRA_HTTP_VERB = "EXTRA_HTTP_VERB";
	public static final String EXTRA_PARAMS = "EXTRA_PARAMS";
	public static final String DEFAULT_QUERY_FILTERS = "DEFAULT_QUERY_FILTERS";
	public static final String EXTRA_RESULT_RECEIVER = "EXTRA_RESULT_RECEIVER";
	public static final String HEADERS = "HEADERS";
	public static final String REST_RESULT = "REST_RESULT";

	public RESTIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// When an intent is received by this Service, this method is called on
		// a new thread.

		Uri intentUriAction = intent.getData();
		Bundle extras = intent.getExtras();
		if (extras == null || intentUriAction == null || !extras.containsKey(EXTRA_RESULT_RECEIVER)) {
			Log.e(TAG, "You did not pass extras or data with the Intent.");
			return;
		}

		// We default to GET if no verb was specified.
		int verb = extras.getInt(EXTRA_HTTP_VERB, GET);
		Bundle params = extras.getParcelable(EXTRA_PARAMS);
		Bundle defaultQueryFilters = extras.getParcelable(DEFAULT_QUERY_FILTERS);
		Bundle headers = extras.getParcelable(HEADERS);

		ResultReceiver receiver = extras.getParcelable(EXTRA_RESULT_RECEIVER);

		try {
			HttpRequestBase request = null;

			switch (verb) {
			case GET: {
				request = buildGetHttpRequest();
				break;
			}

			case DELETE: {
				request = buildDeleteHttpRequest();
				break;
			}

			case POST: {
				request = buildPostHttpRequest(params);
				break;
			}
			case PUT: {
				request = buildPutHttpRequest(params);
				break;
			}
			}

			request = setHeaders(request, headers);

			if (params != null && defaultQueryFilters != null) {
				request = attachUriWithQuery(request, intentUriAction, params, defaultQueryFilters);
			} else {
				request.setURI(new URI(intentUriAction.toString()));
			}

			if (request != null) {
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);

				HttpEntity responseEntity = response.getEntity();
				StatusLine responseStatus = response.getStatusLine();
				int statusCode = responseStatus != null ? responseStatus.getStatusCode() : 0;

				if (responseEntity != null) {
					Bundle resultData = new Bundle();
					resultData.putString(REST_RESULT, EntityUtils.toString(responseEntity));
					receiver.send(statusCode, resultData);
				} else {
					receiver.send(statusCode, null);
				}
			}
		} catch (URISyntaxException e) {
			Log.e(TAG, "Error parsing the URI", e);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "A UrlEncodedFormEntity was created with an unsupported encoding.", e);
			receiver.send(0, null);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "There was a problem when sending the request.", e);
			receiver.send(0, null);
		} catch (IOException e) {
			Log.e(TAG, "There was a problem when sending the request.", e);
			receiver.send(0, null);
		}
	}

	private HttpRequestBase buildGetHttpRequest() {
		return new HttpGet();
	}

	private HttpRequestBase buildDeleteHttpRequest() {
		return new HttpDelete();
	}

	private HttpRequestBase buildPostHttpRequest(Bundle params) throws UnsupportedEncodingException {
		HttpRequestBase request = new HttpPost();
		HttpPost postRequest = (HttpPost) request;
		if (params != null) {
			StringEntity formEntity = new StringEntity(params.getString("json"));
			postRequest.setEntity(formEntity);
		}
		return request;
	}

	private HttpRequestBase buildPutHttpRequest(Bundle params) throws UnsupportedEncodingException {
		HttpRequestBase request = new HttpPut();
		HttpPut putRequest = (HttpPut) request;
		if (params != null) {
			StringEntity formEntity = new StringEntity(params.getString("json"));
			putRequest.setEntity(formEntity);
		}
		return request;
	}

	private HttpRequestBase attachUriWithQuery(HttpRequestBase request, Uri uri, Bundle params, Bundle defaultQueryParams) throws URISyntaxException {
		if (params != null && defaultQueryParams != null) {
			Uri.Builder uriBuilder = uri.buildUpon();

			uriBuilder = attachQueryParams(uriBuilder, defaultQueryParams);
			uriBuilder = attachQueryParams(uriBuilder, params);

			uri = uriBuilder.build();
			request.setURI(new URI(uri.toString()));
		}
		return request;
	}

	private Uri.Builder attachQueryParams(Uri.Builder builder, Bundle params) {
		if (params != null) {
			for (BasicNameValuePair param : buildParamListFromBundle(params)) {
				builder.appendQueryParameter(param.getName(), param.getValue());
			}
		}

		return builder;
	}

	private List<BasicNameValuePair> buildParamListFromBundle(Bundle params) {
		ArrayList<BasicNameValuePair> formList = new ArrayList<BasicNameValuePair>(params.size());

		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value != null) {
				formList.add(new BasicNameValuePair(key, value.toString()));
			}
		}

		return formList;
	}

	private HttpRequestBase setHeaders(HttpRequestBase request, Bundle headers) {
		request.setHeader("Content-Type", "application/json");
		if (headers != null) {
			for (BasicNameValuePair param : buildParamListFromBundle(headers)) {
				request.setHeader(param.getName(), param.getValue());
			}
		}
		return request;
	}

}