package app.juaagugui.httpService.listeners;

import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

/**
 * @author Juan Aguilar Guisado
 * @since 1.0
 * 
 */
public final class LoadingDialogHandler extends Handler {
	// Constants for Hiding/Showing Loading dialog
	public static final int HIDE_LOADING_DIALOG = 0;
	public static final int SHOW_LOADING_DIALOG = 1;

	private ProgressBar mLoadingDialogContainer;

	public LoadingDialogHandler(ProgressBar container) {
		mLoadingDialogContainer = container;
	}

	public void handleMessage(Message msg) {
		if (msg.what == SHOW_LOADING_DIALOG) {
			mLoadingDialogContainer.setVisibility(ProgressBar.VISIBLE);

		} else if (msg.what == HIDE_LOADING_DIALOG) {
			mLoadingDialogContainer.setVisibility(ProgressBar.GONE);
		}
	}
}
