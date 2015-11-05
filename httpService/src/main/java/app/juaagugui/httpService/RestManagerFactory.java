package app.juaagugui.httpService;

import android.content.Context;

import app.juaagugui.httpService.listeners.OnHttpEventListener;
import app.juaagugui.httpService.services.RestManagerImpl;

public class RestManagerFactory {

    public static RestManagerImpl createDefaultRestManager(Context context) {
        return new RestManagerImpl(context, new OnHttpEventListener() {
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

    public static RestManagerImpl createRestManagerWithHttpEventListener(Context context, OnHttpEventListener httpEventListener) {
        return new RestManagerImpl(context, httpEventListener) {
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
