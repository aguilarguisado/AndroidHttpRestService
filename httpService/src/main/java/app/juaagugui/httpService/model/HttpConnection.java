package app.juaagugui.httpService.model;

import android.util.Pair;

import org.json.JSONObject;

import java.util.List;

import app.juaagugui.httpService.listeners.OnRESTResultCallback;
import app.juaagugui.httpService.services.RESTIntentService.HTTP_VERB;

public interface HttpConnection {

    String getUrl();

    void setUrl(String url);

    HTTP_VERB getHttpVerb();

    void setHttpVerb(HTTP_VERB httpVerb);

    JSONObject getBodyData();

    void setBodyData(JSONObject bodyData);

    Boolean getLoginRequired();

    void setLoginRequired(Boolean loginRequired);

    List<Pair<String, String>> getHeaders();

    void setHeaders(List<Pair<String, String>> headers);

    /**
     * @since 2.1
     * <p/>
     * For sending images, you should to send (for each) a Pair<String, String> in order
     * to avoid overflow problems while starting service.
     */

    List<Pair<String, String>> getFiles();

    void setFiles(List<Pair<String, String>> headers);

    /**
     * Some servers require several params attached to the URL, even if the verb
     * of the httpRequest is not GET
     *
     * @return Default params to be attached to the URL following GET way
     */
    List<Pair<String, String>> getDefaultUriQueryParams();

    /**
     * Some servers require several params attached to the URL, even if the verb
     * of the httpRequest is not GET
     *
     * @param defaultUriQueryParams
     */
    void setDefaultUriQueryParams(List<Pair<String, String>> defaultUriQueryParams);

    /**
     * Gets the listener which will process the result of the httpRequest
     *
     * @return callback
     */

    OnRESTResultCallback getCallback();

    /**
     * Sets the listener which will process the result of the httpRequest
     *
     * @param callback
     */
    void setCallback(OnRESTResultCallback callback);

}