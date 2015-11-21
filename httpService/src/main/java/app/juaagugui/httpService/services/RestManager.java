package app.juaagugui.httpService.services;

import app.juaagugui.httpService.exceptions.NoInternetConnectionException;
import app.juaagugui.httpService.model.HttpConnection;
import android.content.Context;
import android.os.ResultReceiver;

/**
 * 
 * @author Juan Aguilar Guisado
 * @since 1.0
 */
public interface RestManager {


	/**
	 * Make an HttpRequest
	 * 
	 * @param connection
	 *            Wrapper with request settings
	 * @throws NoInternetConnectionException
	 *             Exception thrown in case network is not available
	 */
	public void sendRequest(HttpConnection connection) throws NoInternetConnectionException;

	/**
	 * Make an HttpRequest with a return code
	 * 
	 * @param returnCode 
	 * 			Code to be returned with the callback
	 * @param connection
	 *            Wrapper with request settings
	 * @throws NoInternetConnectionException
	 *             Exception thrown in case network is not available
	 * @since 1.0.1
	 */
	public void sendRequestWithReturn(int returnCode, HttpConnection connection) throws NoInternetConnectionException;
	
	/**
	 * 
	 * @return if the user is currently logged in server side
	 */
	public boolean isLogged();

	/**
	 * Execute the logout action of client side.
	 * 
	 * @return the success of the logout action
	 */
	public boolean logout();


	/**
	 * Check network connection for this device
	 * 
	 * @param context
	 * @return
	 */
	public Boolean hasDeviceConnectionToInternet(Context context);

}
