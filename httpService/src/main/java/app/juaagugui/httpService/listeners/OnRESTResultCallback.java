package app.juaagugui.httpService.listeners;

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
	 * @param returnCode
	 * @param result
	 * 
	 * @since 1.0.1
	 */
	public void onRESTResult(int returnCode, int code, String result);
}
