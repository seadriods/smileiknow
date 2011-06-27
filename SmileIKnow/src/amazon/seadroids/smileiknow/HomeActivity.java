package amazon.seadroids.smileiknow;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import android.app.AlertDialog;

public class HomeActivity extends Activity {

	private ImageButton button1;
	private ImageButton button2;
	private ImageButton button3;

	WebView webViewCollage;
	
	ProgressDialog progressDlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		webViewCollage = (WebView) findViewById(R.id.webViewCollageHome);
		// webViewCollage.loadUrl(CollageHandler.getCollage());

		// add actions to buttons
		this.button1 = (ImageButton) findViewById(R.id.buy_button1);
		this.button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				progressDlg = ProgressDialog.show(HomeActivity.this, null, "Processing, please wait");
				new UploadInBackground().execute("1");
			}
		});

		this.button2 = (ImageButton) findViewById(R.id.buy_button2);
		this.button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				progressDlg = ProgressDialog.show(HomeActivity.this, null, "Processing, please wait");
				new UploadInBackground().execute("2");
			}
		});

		this.button3 = (ImageButton) findViewById(R.id.buy_button3);
		this.button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				progressDlg = ProgressDialog.show(HomeActivity.this, null, "Processing, please wait");
				new UploadInBackground().execute("3");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// webViewCollage.loadUrl(CollageHandler.getCollage());
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

				// do somthing
			}
		});

		alertDlg.show();
	}

	private class UploadInBackground extends AsyncTask<String, Integer, Long> {
		@Override
		protected Long doInBackground(String... params) {
			boolean result = ServiceHandler.processOrder(params[0]);
			return result ? new Long(1) : null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Long result) {
			progressDlg.dismiss();

			if (result != null)
				showDialog("Your order was processed successfully");
			else
				showDialog("Oops, an error occured, please try again later");

		}
	}

}
