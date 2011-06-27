package amazon.seadroids.smileiknow;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class CameraActivity extends Activity {

	byte[] imageBytes;
	ProgressDialog progressDlg;

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1337;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		useCamera();

	}

	private void useCamera() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent,
				CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
				imageBytes = baos.toByteArray();

				/*
				FileOutputStream out;
				try {
					out = new FileOutputStream("/sdcard/test.jpg");
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				

				ImageView preview = (ImageView) findViewById(R.id.photoResultView);
				preview.setImageBitmap(bmp);

				progressDlg = ProgressDialog.show(this, null,
						"Processing, please wait");

				new UploadInBackground().execute();

			} else if (resultCode == RESULT_CANCELED) {
				showDialog(null);
			} else {
				Intent myIntent = new Intent(CameraActivity.this,
						HomeActivity.class);
				startActivityForResult(myIntent, 0);
			}
		}
	}

	private void showDialog(String msg) {
		AlertDialog alertDlg = new AlertDialog.Builder(this).create();
		alertDlg.setMessage(msg);
		alertDlg.setButton("Take another picture",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						useCamera();
					}
				});

		alertDlg.setButton2("Continue shopping",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent myIntent = new Intent(CameraActivity.this,
								HomeActivity.class);
						startActivityForResult(myIntent, 0);
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
