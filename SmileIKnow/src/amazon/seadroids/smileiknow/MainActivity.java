package amazon.seadroids.smileiknow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	EditText editUsername;
	EditText editPassword;
	ImageButton btnSignIn;

	byte[] imageBytes;
	ProgressDialog progressDlg;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		editUsername = (EditText) findViewById(R.id.editUsername);
		editPassword = (EditText) findViewById(R.id.editPassword);
	}

	public static byte[] getBytesFromFile(InputStream is) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();

			return buffer.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	public void signIn(View v) {
		if ((editUsername.getText().length() == 0)
				|| (editPassword.getText().length() == 0)) {
			return;
		}

		EditText username = (EditText) findViewById(R.id.editUsername);
		// SharedData.userId = username.getText().toString();

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String action = intent.getAction();

		// if this is from the share menu
		if (Intent.ACTION_SEND.equals(action)) {
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				try {
					// Get resource path from intent callee
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

					// Query gallery for camera picture via
					// Android ContentResolver interface
					ContentResolver cr = getContentResolver();
					InputStream is = cr.openInputStream(uri);
					// Get binary bytes for encode
					imageBytes = getBytesFromFile(is);

					progressDlg = ProgressDialog.show(this, null,
							"Processing, please wait");
					new UploadInBackground().execute();

				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
				}

			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {
				this.finish();
				return;
			}
		} else {

			Intent myIntent = new Intent(v.getContext(), HomeActivity.class);
			startActivityForResult(myIntent, 0);
		}
	}

	private void showDialog(String msg) {
		AlertDialog alertDlg = new AlertDialog.Builder(this).create();
		alertDlg.setMessage(msg);
		alertDlg.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				MainActivity.this.finish();
			}
		});

		alertDlg.show();
	}

		private class UploadInBackground extends AsyncTask<String, Integer, Long> {
		@Override
		protected Long doInBackground(String... params) {
			boolean result = ServiceHandler.sendImage(imageBytes);
			return result ? new Long(1) : null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Long result) {
			progressDlg.dismiss();

			if (result != null)
				showDialog("Your picture has been submitted to Amazon.com");
			else
				showDialog("Oops, an error occured, please try again later");

		}
	}

}