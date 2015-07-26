package app.juaagugui.httpService.exceptions;

/**
 * Exception thrown when there is not connection network (Network Mobile, Wifi,
 * etc.)
 * 
 * @author Juan Aguilar Guisado
 * @since 1.0
 * 
 */
public class NoInternetConnectionException extends Exception {

	private static final long serialVersionUID = 970452226189062435L;

	public NoInternetConnectionException() {
	}

	public NoInternetConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public NoInternetConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NoInternetConnectionException(Throwable throwable) {
		super(throwable);
	}
}
