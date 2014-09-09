package mss.fragments.sample;

import mss.httpService.R;
import mss.httpService.exceptions.NoInternetConnectionException;
import mss.httpService.listeners.OnHttpEventListener;
import mss.httpService.listeners.OnRESTResultCallback;
import mss.httpService.model.HttpConnection;
import mss.httpService.model.IHttpConnection;
import mss.httpService.services.HttpManagerService;
import mss.httpService.services.IHttpManagerService;
import mss.httpService.services.RESTIntentService;
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

/**
 * 
 * @author Juan Aguilar Guisado
 * 
 */
public class HttpServiceFragment extends Fragment implements OnRESTResultCallback, OnClickListener,OnHttpEventListener{

	private String MIME = "text/html";
	private String ENCODING = "utf-8";
	private IHttpManagerService httpManagerService;
	private EditText filtersInputText;
	private ProgressBar loading;
	private WebView webView;
	// We don't allow several requests at the same time.
	private Boolean waitingForHttpRequest;

	public HttpServiceFragment() {
		waitingForHttpRequest = false;
		httpManagerService = new HttpManagerService(this.getActivity(),this) {

			@Override
			public boolean logout() {
				// Not necessary
				return false;
			}

			@Override
			public boolean isLogged() {
				// Not necessary
				return false;
			}

			
		};
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

			IHttpConnection connection = new HttpConnection(url, RESTIntentService.GET, null, this);
			//In this case, this.getActivity() is null in the fragment constructor call, so we set it right here.
			httpManagerService.setContext(this.getActivity());
			try {
				httpManagerService.sendRequest(connection);
			} catch (NoInternetConnectionException expected) {
				setCustomCSSHtmlToWebView(R.string.noInternet, "no_internet.png");
			}
		}
	}

	@Override
	public void onRESTResult(int code, String result) {
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
