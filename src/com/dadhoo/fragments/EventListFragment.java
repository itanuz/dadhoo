/**
 * 
 */
package com.dadhoo.fragments;

import android.app.ListFragment;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.util.ImageFetcherFromFile;

/**
 * @author gaecarme
 *
 */
public class EventListFragment extends ListFragment {
	
	private static final String TAG = "EventListFragment";
	
	private int mImageThumbSize;
	private ImageFetcherFromFile mImageFetcher;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);	

    	mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

    	// The ImageFetcher takes care of loading images into our ImageView children asynchronously
    	mImageFetcher = new ImageFetcherFromFile(getActivity(), mImageThumbSize);

  		CursorLoader cursorLoader = new CursorLoader(this.getActivity(), 
  												 DadhooDB.Events.EVENTS_CONTENT_URI, 
  												 null, 
  												 null, 
  												 null, 
  												 null);
  		Cursor eventsCursor = cursorLoader.loadInBackground();
		
		EventAdapter eventAdapter = new EventAdapter(this.getActivity(), 
													R.layout.event_item, 
													eventsCursor, 
													mImageFetcher,
													new String[]{DadhooDB.Albums.PICTURE_ID}, 
													null, 
													0);
		
		if(eventsCursor.moveToFirst()){//there are events  
			Log.d(TAG, "Ther are events");
			setListAdapter(eventAdapter);
//	        gridView.setOnItemClickListener(new OnItemClickListener() {
//	        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            	boolean useFragment = false;
//	            	if (useFragment) { //multi-pane
//	            		//update the fragment contained in the getActivity()
//	            	} else {//one-pane call the activity
//		            	Intent intent = new Intent(getActivity(), EventsListActivity.class);
//		            	intent.putExtra("position", position);
//		            	intent.putExtra("album_id", id);
//		            	startActivity(intent);
//	            	}
//	            }
//	        });
	        
//	        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
	
//				@Override
//				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//	            	boolean useFragment = false;
//	            	Toast.makeText(getActivity(), 
//	            					"POSITION: " + position + " ID: " + id, 	
//	            					Toast.LENGTH_SHORT).
//	            					show();
//	
//	            	if (useFragment) { //multi-pane
//	            		//update the fragment contained in the getActivity()
//	            	} else {//one-pane call the activity
//		            	Intent intent = new Intent(getActivity(), NewAlbumActivity.class);
//		            	intent.putExtra("album_id", id);
//		            	startActivity(intent);
//	            	}
//					return true;
//				}
//	        });
		} else {//there are not album yet. Show an icon to easily create one
			
			System.out.println("NO events yet");
//			gridView.setAdapter(new ImageAdapter(this.getActivity()));
//			gridView.setOnItemClickListener(new OnItemClickListener() {
//	        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            	boolean useFragment = false;
//	            	if (useFragment) { //multi-pane
//	            		//update the fragment contained in the getActivity()
//	            	} else {//one-pane call the activity
//	            		Intent intent = new Intent(getActivity(), NewAlbumActivity.class);
//		     			startActivity(intent);
//	            	}
//	            }
//	        });
		}

//      return gridView;
  
	}
}
