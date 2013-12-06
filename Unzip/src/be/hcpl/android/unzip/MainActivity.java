package be.hcpl.android.unzip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.testunzip.R;

/**
 * TODO <br/>
 * perform in background <br/>
 * user feedback on progress and failures <br/>
 * user selectable file<br/>
 * improve doc
 * 
 * @author hcpl
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button) findViewById(R.id.buttonExtract)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// since we pass null here this will fall back to the defaults
				extractZip(null, null);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * helper to perform extraction of zip
	 */
	private void extractZip(final String zipFilePath, String targetFolderPath) {

		InputStream inStream = null;
		ZipInputStream zipStream = null;

		try {
			// fallback on the delivered zip file if no zipFile was given as an
			// argument
			inStream = zipFilePath == null ? getAssets().open(zipFilePath) : new FileInputStream(new File(zipFilePath));

			// fallback on the application folder if no targetFolder was given
			// as an argument
			targetFolderPath = targetFolderPath == null ? getApplicationContext().getFilesDir().getPath()
					: targetFolderPath;

			// create an input stream for the zipfile
			zipStream = new ZipInputStream(new BufferedInputStream(inStream));
			// the zip entry instance is used to traverse all items in the zip
			// file
			ZipEntry entry;
			// work with a buffer to improve speed
			int size;
			byte[] buffer = new byte[2048];

			// loop all zip entries here
			while ((entry = zipStream.getNextEntry()) != null) {

				FileOutputStream outStream = null;
				BufferedOutputStream bufferOut = null;

				// let's have a try/catch in the loop so we don't break all code
				// if one file fails
				try {

					// prepare file name to be created
					File of = new File(targetFolderPath + "/" + entry.getName());

					// create dirs where needed
					if (entry.isDirectory()) {
						of.mkdirs();
					}
					// unpack files
					else {

						outStream = new FileOutputStream(of);
						bufferOut = new BufferedOutputStream(outStream, buffer.length);

						while ((size = zipStream.read(buffer, 0, buffer.length)) != -1) {
							bufferOut.write(buffer, 0, size);
						}

					}

					// per file error handling
				} catch (Exception e) {
					Log.e("unzip", "Failed on a single file", e);
				} finally {
					// clean up goes here
					if (bufferOut != null) {
						bufferOut.flush();
						bufferOut.close();
					}

				}

			}

			// the overall error handling, you should be more specific in
			// production and warn user about issues
		} catch (Exception e) {
			Log.e("unzip", "Failed on a single file", e);
		} finally {
			try {
				// clean up
				if (zipStream != null)
					zipStream.close();
				if (inStream != null)
					inStream.close();
			} catch (Exception e) {
				Log.e("unzip", "Error on closing streams", e);
			}
		}

	}

}
