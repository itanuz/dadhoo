package com.dadhoo.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.database.DadhooDbHelper;
import com.dadhoo.util.ImageFetcherFromFile;
import com.dadhoo.util.Utils;

public class NewAlbumActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private static final int MEDIA_TYPE_IMAGE = 1;
	
	private Uri pictureFileUri;
	private Uri pictureContentUri;
	
	private EditText mAlbumTitleText;
	private ImageView mAlbumImage;
	
	private long album_id = -1;
	private boolean isDetailMode = false;
	private boolean isEdit = false;

	private int mImageThumbSize;
	private int mImageThumbSpacing;

	private ImageFetcherFromFile mImageFetcher;

	private DadhooDbHelper mOpenDbHelper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_album);
		
		mOpenDbHelper = new DadhooDbHelper(this);
		
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

     // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width) / 2;
        
        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcherFromFile(this, longest);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        
		mAlbumTitleText = (EditText) findViewById(R.id.album_title);
		mAlbumImage = (ImageView) findViewById(R.id.album_image);
		
		if (getIntent().getExtras() != null) {
			album_id = getIntent().getExtras().getLong("album_id");
			isDetailMode = getIntent().getExtras().getBoolean("is_update");
			isEdit = getIntent().getExtras().getBoolean("is_edit");
		}
		
		if (isEdit) {
			getActionBar().setIcon(R.drawable.ic_done);
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setTitle(null);
			getActionBar().setHomeButtonEnabled(true);
		}
		
		if(!isEdit && isDetailMode) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(null);
			getActionBar().setHomeButtonEnabled(true);
		}
		
		if (isDetailMode) {//updqte then initialize fields.
			CursorLoader cursorLoaderAlbum = new CursorLoader(this, 
															 DadhooDB.Albums.ALBUMS_CONTENT_URI, 
															 null, 
															 BaseColumns._ID + " = ?",
															 new String[] {Long.toString(album_id)}, 
															 null);
			Cursor cursorAlbum = cursorLoaderAlbum.loadInBackground();
			
			if(cursorAlbum.moveToFirst()){
				 mAlbumTitleText.setText(cursorAlbum.getString(1));
				 //Add the title only if it is in not in edit mode
				 if (!isEdit) {
					 getActionBar().setTitle(cursorAlbum.getString(1));
					 mAlbumTitleText.setCursorVisible(false);
					 mAlbumTitleText.setClickable(false);
					 mAlbumTitleText.setFocusable(false);
					 mAlbumTitleText.setFocusableInTouchMode(false);
				 }
				 String pictureContentUriString = cursorAlbum.getString(3);
			        if (pictureContentUriString != null) {
			        	pictureContentUri = ContentUris.withAppendedId(DadhooDB.Pictures.PICTURE_CONTENT_URI, Long.parseLong(pictureContentUriString));
			        	mImageFetcher.loadImage(Uri.parse(Utils.getPicturePath(pictureContentUri, this)), mAlbumImage);
			        }
			}
		}
		
		if((!isDetailMode && isEdit) || (isDetailMode && isEdit) ) {
			mAlbumImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					pictureFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
					startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			});
		} 
	
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (pictureFileUri != null) {
			outState.putString("PICTURE_URI", pictureFileUri.toString());
		}
		if (pictureContentUri != null) {
			outState.putString("PICTURE_URI", pictureContentUri.toString());
		}
		super.onSaveInstanceState(outState);
	}
	
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getString("PICTURE_URI") != null) {
			pictureFileUri = (savedInstanceState != null) 
					? Uri.parse(savedInstanceState.getString("PICTURE_URI")) : null;  
			if (isDetailMode) {
				mImageFetcher.loadImage(Uri.parse(Utils.getPicturePath(pictureFileUri, this)), mAlbumImage);
			} else {
				mImageFetcher.loadImage(pictureFileUri.getPath(), mAlbumImage);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crud_album_menu, menu);
		if (isEdit) {
			menu.removeItem(R.id.album_action_delete);
			menu.removeItem(R.id.album_action_edit);
			menu.removeItem(R.id.album_action_share);
		} else {
			menu.removeItem(R.id.album_action_camera);
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(isDetailMode) {//
					if(isEdit) {//update album
						int rowAffected = updateAlbum();
						if(rowAffected != 0) {
							Intent intent = new Intent(this, AlbumListActivity.class);
							intent.putExtra("albums_list", true);
							Toast.makeText(this, "updated", Toast.LENGTH_LONG).show();
							finish();
							startActivity(intent);
						} else {
							//album cannot be created
						}
					} else {//go back to MainActivity
						NavUtils.navigateUpFromSameTask(this);
						return true;
					}
				} else {//Insert
					int affectedRows = insertAlbum();
					if(affectedRows > 0) {
						Intent intent = new Intent(this, AlbumListActivity.class);
						intent.putExtra("albums_list", true);
						Toast.makeText(this, "created", Toast.LENGTH_LONG).show();
						startActivity(intent);
					} else {
						//album cannot be created
					}
				}
				if(pictureFileUri != null) {
					addPictureToGallery();
				}
				return true;
			case R.id.album_action_delete:
				int rowAffected = deleteAlbum();
				if(rowAffected != 0) { 
					Intent intent = new Intent(this, AlbumListActivity.class);
				intent.putExtra("albums_list", true);
				Toast.makeText(this, "deleted", Toast.LENGTH_LONG).show();
				startActivity(intent);
				}
				return true;
			case R.id.album_action_edit:	
				Intent intentEdit = getIntent();
				intentEdit.putExtra("is_edit", true);
			    finish();
			    startActivity(intentEdit);
				return true;
			case R.id.album_action_camera:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				pictureFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		return super.onOptionsItemSelected(item);
	}

	private int deleteAlbum() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if (pictureFileUri != null) {
			ops.add(ContentProviderOperation.newDelete(DadhooDB.Pictures.PICTURE_CONTENT_URI)
					.withSelection(BaseColumns._ID + " = ?", new String[] {pictureContentUri.getLastPathSegment()})
					.build());
		}
		ops.add(ContentProviderOperation.newDelete(DadhooDB.Albums.ALBUMS_CONTENT_URI)
				.withSelection(BaseColumns._ID + " = ?", new String[] {Long.toString(album_id)})
				.build());

		try {
			ContentProviderResult[] applyBatch = getContentResolver().applyBatch(DadhooDB.AUTHORITY, ops);
			for(ContentProviderResult cpr : applyBatch) {
				affected += cpr.count;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	
		return affected;
	}

	/**
	 * Update the Album item and the related picture only if it has been modified.
	 * This method uses the <a>DadhooContentProvider.applyBatch()</a> method
	 * 
	 * @return number of rows affected
	 */
	private int updateAlbum() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if(pictureContentUri == null && pictureFileUri != null) {
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Pictures.PICTURE_CONTENT_URI)
					.withValue(DadhooDB.Pictures._DATA, pictureFileUri.getPath())
					.build());
			ops.add(ContentProviderOperation.newUpdate(DadhooDB.Albums.ALBUMS_CONTENT_URI)
					.withSelection(BaseColumns._ID + " = ?", new String[] {Long.toString(album_id)})
					.withValue(DadhooDB.Albums.TITLE, mAlbumTitleText.getText().toString())
					.withValue(DadhooDB.Albums.TIMESTAMP, new Date().getTime())
					.withValueBackReference(DadhooDB.Albums.PICTURE_ID, 0)
					.build());
		} else {
			ops.add(ContentProviderOperation.newUpdate(DadhooDB.Albums.ALBUMS_CONTENT_URI)
					.withSelection(BaseColumns._ID + " = ?", new String[] {Long.toString(album_id)})
					.withValue(DadhooDB.Albums.TITLE, mAlbumTitleText.getText().toString())
					.withValue(DadhooDB.Albums.TIMESTAMP, new Date().getTime())
					.build());
			if (pictureFileUri != null) {
				ops.add(ContentProviderOperation.newUpdate(DadhooDB.Pictures.PICTURE_CONTENT_URI)
						.withSelection(BaseColumns._ID + " = ?", new String[] {pictureContentUri.getLastPathSegment()})
						.withValue(DadhooDB.Pictures._DATA, pictureFileUri.getPath())
						.build());
			}
		}
		

		try {
			ContentProviderResult[] applyBatch = getContentResolver().applyBatch(DadhooDB.AUTHORITY, ops);
			for(ContentProviderResult cpr : applyBatch) {
				if(cpr.count != null) {
					affected += cpr.count;
				} else {
					affected++;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	
		return affected;
	}

	/**
	 * Insert a new Album and a Picture if it has been provided
	 * @param pictureContentUri the reference to the picture 
	 * @return the Uri that refers to the new Album created
	 */
	private int insertAlbum() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if (pictureFileUri != null && !mAlbumTitleText.getText().toString().isEmpty()) {
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Pictures.PICTURE_CONTENT_URI)
					.withValue(DadhooDB.Pictures._DATA, pictureFileUri.getPath())
					.build());
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Albums.ALBUMS_CONTENT_URI)
					.withValue(DadhooDB.Albums.TITLE, mAlbumTitleText.getText().toString())
					.withValue(DadhooDB.Albums.TIMESTAMP, new Date().getTime())
					.withValueBackReference(DadhooDB.Albums.PICTURE_ID, 0)
					.build());
		}

		if (pictureFileUri == null && !mAlbumTitleText.getText().toString().isEmpty()) {
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Albums.ALBUMS_CONTENT_URI)
					.withValue(DadhooDB.Albums.TITLE, mAlbumTitleText.getText().toString())
					.withValue(DadhooDB.Albums.TIMESTAMP, new Date().getTime())
					.build());
		}
		
		try {
			ContentProviderResult[] applyBatch = getContentResolver().applyBatch(DadhooDB.AUTHORITY, ops);
			affected = applyBatch.length;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
		
		return affected;
	}
	
	/* Create a File for saving an image or video 
	 */
	private Uri getOutputMediaFileUri(int type) {
		// To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

//	    File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//nvironment.DIRECTORY_PICTURES), "com.dadhoo.app");
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DADHOO");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists()) {
	        if (!mediaStorageDir.mkdirs()) {
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" + pictureFileUri.getPath(), Toast.LENGTH_LONG).show();
	            mImageFetcher.loadImage(pictureFileUri.getPath(), mAlbumImage);
 	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}
	
	/**
	 * Add the picture saved to the gallery invoking the media scanner
	 */
	private void addPictureToGallery() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(pictureFileUri.getPath());
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}

	@Override
    public void onBackPressed() {
    	super.onBackPressed();
    	Intent intent = new Intent(this, AlbumListActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         //disable the back button if the activity is in UPDATE view 
    	 if (keyCode == KeyEvent.KEYCODE_BACK) {
    		if(!isEdit) { 
    			return true;
    		} else if (isDetailMode) {//if not in edit mode then go back to the same activity passing the parameter again just to be sure
    		 Intent intentEdit = getIntent();
    		 intentEdit.putExtra("is_edit", false);
    		 intentEdit.putExtra("is_update", true);
    		 intentEdit.putExtra("album_id", album_id);
    		 finish();
    		 startActivity(intentEdit);
    		} else {
    			onBackPressed();
    		}
    	 }
         //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
         return super.onKeyDown(keyCode, event);    
    }
}
