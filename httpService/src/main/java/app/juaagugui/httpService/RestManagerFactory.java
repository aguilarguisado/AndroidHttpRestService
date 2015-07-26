package app.juaagugui.httpService;

import android.content.Context;

import app.juaagugui.httpService.listeners.OnHttpEventListener;
import app.juaagugui.httpService.services.RestManager;

public class RestManagerFactory {

    public static RestManager createDefaultRestManager(Context context) {
        return new RestManager(context, new OnHttpEventListener() {
            @Override
            public void onRequestInit() {
                //Nothing to do here
            }

            @Override
            public void onRequestFinish() {
                //Nothing to do here
            }
        }) {
            @Override
            public boolean isLogged() {
                return false;
            }

            @Override
            public boolean logout() {
                return false;
            }
        };
    }

    public static RestManager createRestManagerWithHttpEventListener(Context context, OnHttpEventListener httpEventListener) {
        return new RestManager(context, httpEventListener) {
            @Override
            public boolean isLogged() {
                return false;
            }

            @Override
            public boolean logout() {
                return false;
            }
        };
    }


}
