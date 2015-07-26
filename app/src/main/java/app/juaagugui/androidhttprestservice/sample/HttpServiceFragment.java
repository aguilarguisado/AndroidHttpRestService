package app.juaagugui.androidhttprestservice.sample;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import app.juaagugui.androidhttprestservice.R;
import app.juaagugui.httpService.RestManagerFactory;
import app.juaagugui.httpService.exceptions.NoInternetConnectionException;
import app.juaagugui.httpService.listeners.OnHttpEventListener;
import app.juaagugui.httpService.listeners.OnRESTResultCallback;
import app.juaagugui.httpService.model.HttpConnection;
import app.juaagugui.httpService.services.RESTIntentService;
import app.juaagugui.httpService.services.RestManager;

/**
 * @author Juan Aguilar Guisado
 */
public class HttpServiceFragment extends Fragment implements OnRESTResultCallback, OnClickListener, OnHttpEventListener {

    private String MIME = "text/html";
    private String ENCODING = "utf-8";
    private RestManager restManager;
    private EditText filtersInputText;
    private ProgressBar loading;
    private WebView webView;
    // We don't allow several requests at the same time.
    private Boolean waitingForHttpRequest;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        waitingForHttpRequest = false;
        restManager = RestManagerFactory.createRestManagerWithHttpEventListener(getActivity(), this);

        View rootView = inflater.inflate(R.layout.fragment_http_service, container, false);

        RelativeLayout searchContainer = (RelativeLayout) rootView.findViewById(R.id.searchContainer);
        filtersInputText = (EditText) searchContainer.findViewById(R.id.searchEditTextView);
        Button searchButton = (Button) searchContainer.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new RequestCallback());

        setCustomCSSHtmlToWebView(R.string.trySearching, "question.png");

        loading = (ProgressBar) rootView.findViewById(R.id.loading);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (!waitingForHttpRequest) {
            String query = filtersInputText.getText().toString();
            String url = buildGoogleURL(query);

            HttpConnection connection = new HttpConnection(url, RESTIntentService.GET, null, this);
            try {
                restManager.sendRequest(connection);
            } catch (NoInternetConnectionException expected) {
                setCustomCSSHtmlToWebView(R.string.noInternet, "no_internet.png");
            }
        }
    }

    @Override
    public void onRESTResult(int returnCode, int code, String result) {
        if (result == null) {
            setCustomCSSHtmlToWebView(R.string.errorMakingRequest, "error.png");
        } else {
            setHtmlToWebView(result);
        }
    }


    @Override
    public void onRequestInit() {
        waitingForHttpRequest = true;
        loading.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void onRequestFinish() {
        waitingForHttpRequest = false;
        loading.setVisibility(ProgressBar.GONE);
    }


    private String buildGoogleURL(String query) {
        String stringFormattedForQuery = query.replaceAll(" ", "+");
        return "https://www.google.com/search?q=" + stringFormattedForQuery;
    }

    private class RequestCallback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            setCustomCSSHtmlToWebView(R.string.navigationForbidden, "forbidden.png");
            return (false);
        }

    }

    private void setHtmlToWebView(String htmlContent) {
        webView.loadDataWithBaseURL(null, htmlContent, MIME, ENCODING, null);
    }

    private void setCustomCSSHtmlToWebView(Integer resourceId, String imageName) {
        String htmlContent = buildHtmlContent(resourceId, imageName);
        webView.loadDataWithBaseURL("file:///android_asset/", htmlContent, "text/html", "utf-8", null);
    }

    private String buildHtmlContent(Integer resourceId, String imageName) {
        String text = getString(resourceId);
        return buildFormattedHTMFromText(text, imageName);
    }

    private String buildFormattedHTMFromText(String text, String imageName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
        if (imageName != null && !imageName.isEmpty()) {
            sb.append("<img id=\"main_image\" src=" + imageName + " alt=\"\"></br>");
        }
        sb.append(text);
        sb.append("</body></HTML>");
        return sb.toString();
    }
}
