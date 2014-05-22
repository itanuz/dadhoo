/**
 * 
 */
package com.dadhoo.fragments;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.activities.NewEventActivity;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.database.DadhooDbHelper;
import com.dadhoo.util.ImageFetcherFromFile;

/**
 * @author gaecarme
 *
 */
public class EventListFragment extends Fragment {
	
	private static final String TAG = "EventListFragment";
	
	private int mImageThumbSize;
	private ImageFetcherFromFile mImageFetcher;

	private Long album_id;
	
	public static Fragment newInstance(Long album_id) {
		Log.d(TAG, "New Instance");
		EventListFragment fragment = new EventListFragment();
		Bundle fragmentArgs = new Bundle();
		fragmentArgs.putLong("ALBUM_ID", album_id);
		fragment.setArguments(fragmentArgs); 
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		album_id = getArguments() != null ? getArguments().getLong("ALBUM_ID") : null;	
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);	

    	mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

    	// The ImageFetcher takes care of loading images into our ImageView children asynchronously
    	mImageFetcher = new ImageFetcherFromFile(getActivity(), mImageThumbSize);

    	View rootView = inflater.inflate(R.layout.framelayout_event_list, container, false);
    	GridView gridView = (GridView) rootView.findViewById(R.id.image_event);
    	
    	Uri uri = DadhooDB.Events.EVENTS_CONTENT_URI;
    	
    	if(album_id != null) {
    		uri = Uri.parse("content://com.dadhoo.provider.dadhoocontentprovider/album/" + album_id + "/event");
    	}
    	
  		CursorLoader cursorLoader = new CursorLoader(this.getActivity(), 
  													uri, 
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
			gridView.setAdapter(eventAdapter);
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
	        
	        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
	
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	            	boolean useFragment = false;
	            	Toast.makeText(getActivity(), 
	            					"POSITION: " + position + " ID: " + id, 	
	            					Toast.LENGTH_SHORT).
	            					show();
	
	            	if (useFragment) { //multi-pane
	            		//update the fragment contained in the getActivity()
	            	} else {//one-pane call the activity
		            	Intent intent = new Intent(getActivity(), NewEventActivity.class);
		            	intent.putExtra("event_id", id);
		            	startActivity(intent);
	            	}
					return true;
				}
	        });
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

      return gridView;
  
	}
}
