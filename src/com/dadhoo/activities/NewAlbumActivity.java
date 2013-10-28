package com.dadhoo.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.provider.DadhooDB;

public class NewAlbumActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private static final int MEDIA_TYPE_IMAGE = 1;
	
	private Uri pictureFileUri;
	
	private EditText mAlbumTitleText;
	private EditText mAlbumBabyName;
	private EditText mAlbumBirthLocation;
	private EditText mAlbumMotherName;
	private EditText mAlbumFatherName;
	
	private long album_id = -1;
	private boolean isUpdate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_album);
		
		mAlbumTitleText = (EditText) findViewById(R.id.album_title);
		mAlbumBabyName = (EditText) findViewById(R.id.album_baby_name);
		mAlbumBirthLocation = (EditText) findViewById(R.id.album_birth_location);
		mAlbumMotherName = (EditText) findViewById(R.id.album_mother_name);
		mAlbumFatherName = (EditText) findViewById(R.id.album_father_name);
		
		if (getIntent().getExtras() != null) {
			album_id = getIntent().getExtras().getLong("album_id");
			isUpdate = true;
		}
		
		if (isUpdate) {//TODO initialize here the fields.
			mAlbumTitleText.setText(getTitle());
		}
	        
		
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setTitle(R.string.new_album_actionbar_title);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_album, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_done:
				Uri pictureContentUri = null;
				if (pictureFileUri != null) {
					pictureContentUri = insertPicture();
				}
				Uri uriInserted = insertAlbum(pictureContentUri);
				if(uriInserted != null) {
					Intent intent = new Intent(this, MainActivity.class);
					intent.putExtra("albums_list", true);
					Toast.makeText(this, "Album created", Toast.LENGTH_LONG).show();
					startActivity(intent);
				}
				return true;
			case R.id.action_camera:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				pictureFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		return super.onOptionsItemSelected(item);
	}

	private Uri insertPicture() {
		ContentValues pictureContentValues = new ContentValues();
    	pictureContentValues.put(DadhooDB.Pictures._DATA, pictureFileUri.getPath());

    	return getContentResolver().insert(DadhooDB.Pictures.PICTURE_CONTENT_URI, pictureContentValues);
	}

	private Uri insertAlbum(Uri pictureContentUri) {
		ContentValues albumContentValues = new ContentValues();
		
		albumContentValues.put(DadhooDB.Albums.TITLE, mAlbumTitleText.getText().toString());
		albumContentValues.put(DadhooDB.Albums.BABYNAME, mAlbumBabyName.getText().toString());
		albumContentValues.put(DadhooDB.Albums.BIRTHLOCATION, mAlbumBirthLocation.getText().toString());
		albumContentValues.put(DadhooDB.Albums.FATHERNAME, mAlbumFatherName.getText().toString());
		albumContentValues.put(DadhooDB.Albums.MOTHERNAME, mAlbumMotherName.getText().toString());
		Long timestamp = new Date().getTime();
		albumContentValues.put(DadhooDB.Albums.TIMESTAMP, timestamp);
		if(pictureContentUri != null) {//insert the picture content uri only if the snapshot exists
    		albumContentValues.put(DadhooDB.Albums.PICTURE_URI, pictureContentUri.toString());
    	}
		
		return getContentResolver().insert(DadhooDB.Albums.ALBUMS_CONTENT_URI, albumContentValues);
	}
	
	/* Create a File for saving an image or video 
	 *  Create a file Uri for saving an image or video
	 * 
	 */
	private Uri getOutputMediaFileUri(int type) {
		// To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//nvironment.DIRECTORY_PICTURES), "com.dadhoo.app");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()) {
	        if (! mediaStorageDir.mkdirs()) {
	            Log.d("Dadhoo", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
//	    } else if(type == MEDIA_TYPE_VIDEO) {
//	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return Uri.fromFile(mediaFile);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" + pictureFileUri.getPath(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}


	
	
}
