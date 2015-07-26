package app.juaagugui.androidhttprestservice.activities;

import app.juaagugui.androidhttprestservice.R;
import app.juaagugui.androidhttprestservice.sample.HttpServiceFragment;
import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Juan Aguilar Guisado
 * @version 1.0
 * @since 1.0
 * 
 */
public class LauncherMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_launcher_main);
		getFragmentManager().beginTransaction()
				.add(R.id.container, new HttpServiceFragment()).commit();
	}
}