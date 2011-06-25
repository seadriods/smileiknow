package amazon.seadroids.smileiknow;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HomeActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getString(R.string.homepage_url));
        

    }
	
	

}
