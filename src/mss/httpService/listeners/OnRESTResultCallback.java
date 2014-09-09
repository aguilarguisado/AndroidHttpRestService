package mss.httpService.listeners;

/**
 * Listener for executing the HttpRequest response back.
 * 
 * @author Juan Aguilar Guisado
 * @since 1.0
 * 
 */
public interface OnRESTResultCallback {
	/**
	 * It's called by the intent service when the device gets the response of
	 * the HttpRequest
	 * 
	 * @param code
	 * @param result
	 */
	public void onRESTResult(int code, String result);
}
