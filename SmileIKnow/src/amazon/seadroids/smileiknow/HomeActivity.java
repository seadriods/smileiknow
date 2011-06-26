package amazon.seadroids.smileiknow;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import android.app.AlertDialog;

public class HomeActivity extends Activity{
	
	private ImageButton button1;
	private ImageButton button2;
	private ImageButton button3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        /*WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getString(R.string.homepage_url));*/

       
        //add actions to buttons
        this.button1 = (ImageButton) findViewById(R.id.buy_button1);
        this.button1.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		showDialog("Thanks! Your order has been logged. Jeff Bezos personally thanks you for shopping with us today.");
        	}	
        });
        
        this.button2 = (ImageButton) findViewById(R.id.buy_button2);
        this.button2.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		showDialog("Thanks! Your order has been logged. Jeff Bezos personally thanks you for shopping with us today.");
        	}	
        });
        
        this.button3 = (ImageButton) findViewById(R.id.buy_button3);
        this.button3.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		showDialog("Thanks! Your order has been logged. Jeff Bezos personally thanks you for shopping with us today.");
        	}	
        });
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.take_picture:
	    	Intent myIntent = new Intent(this, CameraActivity.class);
	        startActivityForResult(myIntent, 0);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showDialog(String msg) {
        AlertDialog alertDlg = new AlertDialog.Builder(this).create();
        alertDlg.setMessage(msg);
        alertDlg.setButton("OK", new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {

                    //do somthing
              }
        });

        alertDlg.show();
  }

	
	

}
