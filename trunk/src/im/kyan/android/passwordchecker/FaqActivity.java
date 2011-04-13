/**
 * 
 */
package im.kyan.android.passwordchecker;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author kyan
 * 
 */
public class FaqActivity extends Activity {

	public static final String ACTION_SAFE = "passwordchecker.action.safe";
	public static final String ACTION_ACCURATE = "passwordchecker.action.accurate";
	public static final String ACTION_HOWTO = "passwordchecker.action.howto";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String action = getIntent().getAction();
		String url = "file:///android_asset/faq.html";

		if (ACTION_SAFE.equals(action)) {
			url += "#safe";
		} else if (ACTION_ACCURATE.equals(action)) {
			url += "#accurate";
		} else if (ACTION_HOWTO.equals(action)) {
			url += "#works";
		}

		WebView wv = new WebView(this);
		wv.loadUrl(url);
		setContentView(wv);
	}

}
