package app.juaagugui.httpService.model;

import android.util.Pair;

import org.json.JSONObject;

import java.util.List;

import app.juaagugui.httpService.listeners.OnRESTResultCallback;
import app.juaagugui.httpService.services.RESTIntentService.HTTP_VERB;

/**
 * @author Juan Aguilar Guisado
 * @since 1.0
 */

public class HttpConnectionImpl implements HttpConnection {

    private String url;
    private HTTP_VERB httpVerb;
    private JSONObject bodyData;
    private Boolean loginRequired;
    private List<Pair<String, String>> headers;
    private List<Pair<String, String>> defaultUriQueryParams;
    private List<Pair<String, String>> files;
    private OnRESTResultCallback callback;


    public HttpConnectionImpl(OnRESTResultCallback callback) {
        this.callback = callback;
    }

    public HttpConnectionImpl(String url, HTTP_VERB httpVerb, JSONObject bodyData, Boolean loginRequired, OnRESTResultCallback callback) {
        this(url, httpVerb, bodyData, loginRequired, null, null, null, callback);
    }

    public HttpConnectionImpl(String url, HTTP_VERB httpVerb, JSONObject bodyData, OnRESTResultCallback callback) {
        this(url, httpVerb, bodyData, false, null, null, null, callback);
    }

    public HttpConnectionImpl(String url, HTTP_VERB httpVerb, JSONObject bodyData, Boolean loginRequired, List<Pair<String, String>> headers, List<Pair<String, String>> defaultUriQueryParams, List<Pair<String, String>> files,
                              OnRESTResultCallback callback) {

        if (url == null || loginRequired == null) {
            throw new IllegalArgumentException("Wrong parameters building a HttpConnection object: url and loginRequired should be != null");
        }

        this.url = url;
        this.httpVerb = httpVerb;
        this.bodyData = bodyData;
        this.loginRequired = loginRequired;
        this.headers = headers;
        this.defaultUriQueryParams = defaultUriQueryParams;
        this.callback = callback;
        this.files = files;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        this.url = url;
    }

    @Override
    public HTTP_VERB getHttpVerb() {
        return httpVerb;
    }

    @Override
    public void setHttpVerb(HTTP_VERB httpVerb) {
        this.httpVerb = httpVerb;
    }

    @Override
    public JSONObject getBodyData() {
        return bodyData;
    }

    @Override
    public void setBodyData(JSONObject bodyData) {
        this.bodyData = bodyData;
    }

    @Override
    public Boolean getLoginRequired() {
        return loginRequired;
    }

    @Override
    public void setLoginRequired(Boolean loginRequired) {
        if (loginRequired == null) {
            throw new IllegalArgumentException("Login Required cannot be null");
        }
        this.loginRequired = loginRequired;
    }

    @Override
    public List<Pair<String, String>> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(List<Pair<String, String>> headers) {
        this.headers = headers;
    }

    @Override
    public List<Pair<String, String>> getDefaultUriQueryParams() {
        return defaultUriQueryParams;
    }

    @Override
    public void setDefaultUriQueryParams(List<Pair<String, String>> defaultUriQueryParams) {
        this.defaultUriQueryParams = defaultUriQueryParams;
    }

    @Override
    public OnRESTResultCallback getCallback() {
        return callback;
    }

    @Override
    public void setCallback(OnRESTResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public List<Pair<String, String>> getFiles() {
        return files;
    }

    @Override
    public void setFiles(List<Pair<String, String>> files) {
        this.files = files;
    }
}
