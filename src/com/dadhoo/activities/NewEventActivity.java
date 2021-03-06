package com.dadhoo.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.database.DadhooDB.Albums;
import com.dadhoo.database.DadhooDB.GroupEvents;
import com.dadhoo.fragments.AlbumListDialogFragment;
import com.dadhoo.fragments.AlbumListDialogFragment.NoticeDialogListener;
import com.dadhoo.util.ImageFetcherFromFile;
import com.dadhoo.util.Utils;

public class NewEventActivity extends FragmentActivity implements NoticeDialogListener {
	
	private static final String TAG = "NewEventActivity";
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private static final int MEDIA_TYPE_IMAGE = 1;
	
	private Uri pictureFileUri;
	private Uri pictureContentUri;
	
	private EditText mEventNoteText;
	private ImageView mEventImage;
	
	private long event_id = -1;
	private long album_id = -1;
	private boolean isDetailMode = false;
	private boolean isNew = false;

	private int mImageThumbSize;
	private int mImageThumbSpacing;

	private Context context = this;
	private ImageFetcherFromFile mImageFetcher;

	private ArrayList<Integer> mSelectedItems;

	private Cursor mCursorGroupEvent;

	private String album_title;
	private String album_picture_id;
	private String album_timestamp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_event);
		
		//hide the keyboard 
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
        
        if (getIntent().getExtras() != null) {
			event_id = getIntent().getExtras().getLong("event_id");
			
			album_id = getIntent().getExtras().getLong("album_id");
			album_title = getIntent().getExtras().getString("album_title");
			album_picture_id  = getIntent().getExtras().getString("album_picture_id");
			album_timestamp  = getIntent().getExtras().getString("album_timestamp");
			
			isDetailMode = getIntent().getExtras().getBoolean("is_update");
			isNew = getIntent().getExtras().getBoolean("is_edit");
		}
		
		if (isNew) {
			getActionBar().setIcon(R.drawable.ic_done);
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setTitle(null);
			getActionBar().setHomeButtonEnabled(true);
		}
		
		if(!isNew && isDetailMode) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(null);
			getActionBar().setHomeButtonEnabled(true);
		}

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

        
        // The ImageFetcher takes care of loading an images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcherFromFile(this, longest);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        
		mEventNoteText = (EditText) findViewById(R.id.edit_note_event);
		mEventImage = (ImageView) findViewById(R.id.image_event);
		
		if (isDetailMode) {//then load all fields for the event
			CursorLoader cursorLoaderEvent = new CursorLoader(this, 
					 DadhooDB.Events.EVENTS_CONTENT_URI, 
					 null, 
					 BaseColumns._ID + " = ?",
					 new String[] {Long.toString(event_id)}, 
					 null);
			Cursor cursorEvent = cursorLoaderEvent.loadInBackground();
			
			if(cursorEvent.moveToFirst()){
				mEventNoteText.setText(cursorEvent.getString(2));
				if (!isNew) {
//					 getActionBar().setTitle(cursorAlbum.getString(1));
					mEventNoteText.setCursorVisible(false);
					mEventNoteText.setClickable(false);
					mEventNoteText.setFocusable(false);
					mEventNoteText.setFocusableInTouchMode(false);
				 }
				String pictureContentUriString = cursorEvent.getString(5);
				if (pictureContentUriString != null) {
					pictureContentUri = ContentUris.withAppendedId(DadhooDB.Pictures.PICTURE_CONTENT_URI, Long.parseLong(pictureContentUriString));
					mImageFetcher.loadImage(Uri.parse(Utils.getPicturePath(pictureContentUri, this)), mEventImage);
				}
			}
			cursorEvent.close();
		} 
		if((!isDetailMode && isNew) || (isDetailMode && isNew) ) {
			mEventImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					pictureFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
					startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			});
		} 
		//Add the button to show the dialog in order to choose one or more album
		Button addAlbumsButton = (Button) findViewById(R.id.button1);
		addAlbumsButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(isDetailMode) {
						CursorLoader cursorLoaderGroupEvent = new CursorLoader(NewEventActivity.this,
																		 DadhooDB.GroupEvents.GROUP_EVENTS_CONTENT_URI, 
																		 null, 
																		 DadhooDB.GroupEvents.EVENT_ID + " = ?",
																		 new String[] {Long.toString(event_id)}, 
																		 null);
						mCursorGroupEvent = cursorLoaderGroupEvent.loadInBackground();
					}
					//If the event is created from an album select the album id otherwise use the cursor
					DialogFragment listOfAlbum = album_id != 0 ? AlbumListDialogFragment.newInstance(album_id) : AlbumListDialogFragment.newInstance(mCursorGroupEvent);
					
					listOfAlbum.show(getSupportFragmentManager(), "ALBUM_LIST_DIALOG");
				}
		});
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
				mImageFetcher.loadImage(Uri.parse(Utils.getPicturePath(pictureFileUri, this)), mEventImage);
			} else {
				mImageFetcher.loadImage(pictureFileUri.getPath(), mEventImage);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.crud_events_menu, menu);
		if (isNew) {
			menu.removeItem(R.id.event_action_delete);
			menu.removeItem(R.id.event_action_edit);
			menu.removeItem(R.id.event_action_share);
		} else {
			menu.removeItem(R.id.event_action_camera);
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(isDetailMode) {//Update an album and a picture if necessary
					if(isNew) {
						int rowAffected = updateEvent();
						if(rowAffected != 0) {
							if(album_id != 0) {//go back to the event list for this album
								Intent intent = new Intent(this, EventsListActivity.class);
				            	intent.putExtra("album_id", album_id);
				            	intent.putExtra("album_title", album_title);
				            	intent.putExtra("album_picture_id", album_picture_id);
				            	intent.putExtra("album_timestamp", album_timestamp);
				            	intent.putExtra("is_edit", true);
				            	startActivity(intent);  
							} else {//go to the event list
								Intent intent = new Intent(this, EventsListActivity.class);
								Toast.makeText(this, "Event created", Toast.LENGTH_LONG).show();
								startActivity(intent);
							}
						} else {
							Log.d(TAG, "album cannot be created");
						}
					} else {//go back to MainActivity
						NavUtils.navigateUpFromSameTask(this);
						return true;
					}	
				} else {//Insert
					//in case a new event is created from an album create the event and the relationship with the album
					if(!isDetailMode && mSelectedItems == null && album_id != 0) {
						mSelectedItems = new ArrayList<Integer>();
						mSelectedItems.add((int) album_id);  
					}
					int affectedRows = insertEvent();
					if(affectedRows > 0) {
						if(album_id != 0) {//go back to the event list for this album
							Intent intent = new Intent(this, EventsListActivity.class);
			            	intent.putExtra("album_id", album_id);
			            	intent.putExtra("album_title", album_title);
			            	intent.putExtra("album_picture_id", album_picture_id);
			            	intent.putExtra("album_timestamp", album_timestamp);
			            	intent.putExtra("is_edit", true);
			            	startActivity(intent);  
						} else {//go to the event list
							Intent intent = new Intent(this, EventsListActivity.class);
							Toast.makeText(this, "Event created", Toast.LENGTH_LONG).show();
							startActivity(intent);
						}
					} else {
						Log.d(TAG, "album cannot be created");
					}
				}
				if(pictureFileUri != null) {
					addPictureToGallery();
				}
				return true;
			case R.id.event_action_edit:	
				Intent intentEdit = getIntent();//take the same intent to stay on the same activity
				intentEdit.putExtra("is_edit", true);
			    finish();
			    startActivity(intentEdit);
				return true;	
			case R.id.event_action_delete:
				int rowAffected = deleteEvent();
				if(rowAffected > 0) {
					if(album_id != 0) {//go back to the event list for this album
						Intent intent = new Intent(this, EventsListActivity.class);
		            	intent.putExtra("album_id", album_id);
		            	intent.putExtra("album_title", album_title);
		            	intent.putExtra("album_picture_id", album_picture_id);
		            	intent.putExtra("album_timestamp", album_timestamp);
		            	intent.putExtra("is_edit", true);
		            	startActivity(intent);  
					} else {//go to the event list
						Intent intent = new Intent(this, EventsListActivity.class);
						Toast.makeText(this, "Event deleted", Toast.LENGTH_LONG).show();
						startActivity(intent);
					}
				}
				return true;
			case R.id.event_action_camera:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				pictureFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		return super.onOptionsItemSelected(item);
	}

	private int deleteEvent() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if (pictureFileUri != null) {
			ops.add(ContentProviderOperation.newDelete(DadhooDB.Pictures.PICTURE_CONTENT_URI)
					.withSelection(BaseColumns._ID + " = ?", new String[] {pictureContentUri.getLastPathSegment()})
					.build());
		}
		ops.add(ContentProviderOperation.newDelete(Uri.withAppendedPath(DadhooDB.Events.EVENT_ID_CONTENT_URI, Long.toString(event_id)))
				.withSelection(BaseColumns._ID + " = ?", new String[] {Long.toString(event_id)})
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
	 * Update the Event item and the related picture only if it has been modified.
	 * This method uses the <a>DadhooContentProvider.applyBatch()</a> method
	 * 
	 * @return number of rows affected
	 */
	private int updateEvent() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newUpdate(DadhooDB.Events.EVENTS_CONTENT_URI)
				.withSelection(BaseColumns._ID + " = ?", new String[] {Long.toString(event_id)})
				.withValue(DadhooDB.Events.NOTE, mEventNoteText.getText().toString())
				.withValue(DadhooDB.Events.MODIFIED, new Date().getTime())
				.build());
		if (pictureFileUri != null) {
			ops.add(ContentProviderOperation.newUpdate(DadhooDB.Pictures.PICTURE_CONTENT_URI)
					.withSelection(BaseColumns._ID + " = ?", new String[] {pictureContentUri.getLastPathSegment()})
					.withValue(DadhooDB.Pictures._DATA, pictureFileUri.getPath())
					.build());
		}

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
	 * Insert a new Event and a Picture if it has been provided
	 * If a picture exists then it is inserted in the picture table
	 * @param pictureContentUri the reference to the picture 
	 * @return the Uri that refers to the new Album created
	 */
	private int insertEvent() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if (pictureFileUri != null && !mEventNoteText.getText().toString().isEmpty()) {
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Pictures.PICTURE_CONTENT_URI)
					.withValue(DadhooDB.Pictures._DATA, pictureFileUri.getPath())
					.build());
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Events.EVENTS_CONTENT_URI)
					.withValue(DadhooDB.Events.NOTE, mEventNoteText.getText().toString())
					.withValue(DadhooDB.Events.INSERTED, new Date().getTime())
					.withValue(DadhooDB.Events.MODIFIED, new Date().getTime())
					.withValueBackReference(DadhooDB.Events.PICTURE_ID, 0)
					.build());
			if(mSelectedItems != null && !isDetailMode) {
				for(Integer element : mSelectedItems) {
					ops.add(ContentProviderOperation.newInsert(DadhooDB.GroupEvents.GROUP_EVENTS_CONTENT_URI)
							.withValue(DadhooDB.Events.MODIFIED, new Date().getTime())
							.withValueBackReference(DadhooDB.GroupEvents.EVENT_ID, 1)
							.withValue(DadhooDB.GroupEvents.ALBUM_ID, element)
							.build());
				}
			}
		}

		if (pictureFileUri == null && !mEventNoteText.getText().toString().isEmpty()) {
			ops.add(ContentProviderOperation.newInsert(DadhooDB.Events.EVENTS_CONTENT_URI)
					.withValue(DadhooDB.Events.NOTE, mEventNoteText.getText().toString())
					.withValue(DadhooDB.Events.INSERTED, new Date().getTime())
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
	            mImageFetcher.loadImage(pictureFileUri.getPath(), mEventImage);
 	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		AlbumListDialogFragment albumListDialog = (AlbumListDialogFragment) dialog;
		mSelectedItems = albumListDialog.getmSelectedAlbumItems();
		if(isDetailMode) {
			removeAndInsertGroupEvent();
		}
	}

	/**
	 * Remove all group event items for a specific event. And insert the new groups.
	 */
	private int removeAndInsertGroupEvent() {
		int affected = 0;
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if(mSelectedItems != null) {
			ops.add(ContentProviderOperation.newDelete(DadhooDB.GroupEvents.GROUP_EVENTS_CONTENT_URI)
					.withSelection(DadhooDB.GroupEvents.EVENT_ID + " = ?", new String[] {Long.toString(event_id)})
					.build());
			for(Integer albumId : mSelectedItems) {
				Uri uri = Uri.parse("content://" + DadhooDB.AUTHORITY + "/" + Albums.ALBUM_NAME + "/" + album_id + "/" + GroupEvents.GROUP_EVENT_NAME + "/" + event_id);
				ops.add(ContentProviderOperation.newInsert(uri)
						.withValue(DadhooDB.Events.MODIFIED, new Date().getTime())
						.withValue(DadhooDB.GroupEvents.EVENT_ID, event_id)
						.withValue(DadhooDB.GroupEvents.ALBUM_ID, albumId)
						.build());
			}
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

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
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
    		if(!isNew) { 
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
