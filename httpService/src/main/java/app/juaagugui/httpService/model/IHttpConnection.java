package app.juaagugui.httpService.model;

import java.util.List;

import app.juaagugui.httpService.listeners.OnRESTResultCallback;

import org.json.JSONObject;

import android.util.Pair;

public interface IHttpConnection {

	public String getUrl();

	public void setUrl(String url);

	public int getHttpVerb();

	public void setHttpVerb(int httpVerb);

	public JSONObject getBodyData();

	public void setBodyData(JSONObject bodyData);

	public Boolean getLoginRequired();

	public void setLoginRequired(Boolean loginRequired);

	public List<Pair<String, String>> getHeaders();

	public void setHeaders(List<Pair<String, String>> headers);

	/**
	 * Some servers require several params attached to the URL, even if the verb
	 * of the httpRequest is not GET
	 * 
	 * @return Default params to be attached to the URL following GET way
	 */
	public List<Pair<String, String>> getDefaultUriQueryParams();

	/**
	 * Some servers require several params attached to the URL, even if the verb
	 * of the httpRequest is not GET
	 * 
	 * @param defaultUriQueryParams
	 */
	public void setDefaultUriQueryParams(List<Pair<String, String>> defaultUriQueryParams);

	/**
	 * Gets the listener which will process the result of the httpRequest
	 * 
	 * @return callback
	 */

	public OnRESTResultCallback getCallback();

	/**
	 * Sets the listener which will process the result of the httpRequest
	 * 
	 * @param callback
	 */
	public void setCallback(OnRESTResultCallback callback);

}