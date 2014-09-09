package mss.httpService.services;

import mss.httpService.exceptions.NoInternetConnectionException;
import mss.httpService.model.IHttpConnection;
import android.content.Context;
import android.os.ResultReceiver;

/**
 * 
 * @author Juan Aguilar Guisado
 * @since 1.0
 */
public interface IHttpManagerService {

	/**
	 * 
	 * @return Handler which will process the result and call the object waiting
	 *         for it.
	 */
	public ResultReceiver getResultReceiver();

	/**
	 * Make an HttpRequest
	 * 
	 * @param connection
	 *            Wrapper with request settings
	 * @throws NoInternetConnectionException
	 *             Exception thrown in case network is not available
	 */
	public void sendRequest(IHttpConnection connection) throws NoInternetConnectionException;

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
	 * Sets the context which will execute the requests in case we need to
	 * change it after building the httpService instance
	 * 
	 * @param context
	 */
	public void setContext(Context context);

	/**
	 * Check network connection for this device
	 * 
	 * @param context
	 * @return
	 */
	public Boolean hasDeviceConnectionToInternet(Context context);

}
