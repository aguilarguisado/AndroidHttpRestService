package app.juaagugui.httpService.services;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Pair;

import java.util.List;

import app.juaagugui.httpService.exceptions.NoInternetConnectionException;
import app.juaagugui.httpService.listeners.OnHttpEventListener;
import app.juaagugui.httpService.listeners.OnRESTResultCallback;
import app.juaagugui.httpService.model.HttpConnection;

/**
 * @author Juan Aguilar Guisado
 * @since 1.0
 */

public abstract class RestManagerImpl implements RestManager {

    private Context context;

    private OnHttpEventListener eventListener;

    private boolean runUIThread;

    public RestManagerImpl(Context context, OnHttpEventListener eventListener) {
        this(context, eventListener, false);
    }

    public RestManagerImpl(Context context, OnHttpEventListener eventListener, boolean runUIThread) {
        this.context = context;
        this.eventListener = eventListener;
        this.runUIThread = runUIThread;
    }

    @Override
    public void sendRequest(HttpConnection connection) throws NoInternetConnectionException {
        sendRequestWithReturn(-1, connection);
    }

    @Override
    public void sendRequestWithReturn(int returnCode, HttpConnection connection) throws NoInternetConnectionException {

        if (eventListener != null) {
            eventListener.onRequestInit();
        }

        if (connection == null) {
            if (eventListener != null) {
                eventListener.onRequestFinish();
            }
            throw new IllegalArgumentException("Connection cannot be null when you want to send an httpRequest!");
        }

        if (context == null) {
            if (eventListener != null) {
                eventListener.onRequestFinish();
            }
            throw new IllegalArgumentException("Context cannot be null when you want to send an httpRequest!");
        }

        if (!hasDeviceConnectionToInternet(context)) {
            if (eventListener != null) {
                eventListener.onRequestFinish();
            }
            throw new NoInternetConnectionException();
        }

        //Getting connection attributes

        Intent intent = new Intent(context, RESTIntentService.class);
        intent.putExtra(RESTIntentService.EXTRA_HTTP_VERB, connection.getHttpVerb());
        intent.setData(Uri.parse(connection.getUrl()));

        intent = setIntentBundle(intent, connection.getFiles(), RESTIntentService.FILES);
        intent = setIntentBundle(intent, connection.getHeaders(), RESTIntentService.HEADERS);
        intent = setIntentBundle(intent, connection.getDefaultUriQueryParams(), RESTIntentService.DEFAULT_QUERY_FILTERS);

        Bundle params = new Bundle();
        if (connection.getBodyData() != null) {
            params.putString("json", connection.getBodyData().toString());
        }


        intent.putExtra(RESTIntentService.EXTRA_PARAMS, params);
        intent.putExtra(RESTIntentService.EXTRA_RESULT_RECEIVER, createResultReceiver(connection.getCallback()));
        intent.putExtra(RESTIntentService.RETURN_CODE, returnCode);

        context.startService(intent);
    }


    private ResultReceiver createResultReceiver(final OnRESTResultCallback callback) {

        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null while building a RestManager request");
        }

        return new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(final int resultCode, Bundle resultData) {
                if (eventListener != null) {
                    eventListener.onRequestFinish();
                }
                int returnCode = -1;
                String restResultData = null;
                if (resultData != null) {
                    returnCode = resultData.getInt(RESTIntentService.RETURN_CODE);
                    if (resultData.containsKey(RESTIntentService.REST_RESULT)) {
                        restResultData = resultData.getString(RESTIntentService.REST_RESULT);
                    }
                }

                if (runUIThread) {
                    final int finalReturnCode = returnCode;
                    final String finalRestResultData = restResultData;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onRESTResult(finalReturnCode, resultCode, finalRestResultData);
                        }
                    });
                } else {
                    callback.onRESTResult(returnCode, resultCode, restResultData);
                }
            }
        };
    }


    @Override
    public Boolean hasDeviceConnectionToInternet(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    public abstract boolean isLogged();

    @Override
    public abstract boolean logout();

    private Intent setIntentBundle(Intent intent, List<Pair<String, String>> pairs, String keyForExtra) {
        if (pairs != null) {
            intent.putExtra(keyForExtra, buildBundleFromListPair(pairs));
        }

        return intent;
    }

    private Bundle buildBundleFromListPair(List<Pair<String, String>> pairs) {
        Bundle bundleRet = new Bundle();
        for (Pair<String, String> p : pairs) {
            bundleRet.putString(p.first, p.second);
        }
        return bundleRet;
    }
}
