/**
 * 
 */
package com.dadhoo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.activities.NewEventActivity;
import com.dadhoo.adapters.EventAdapter;
import com.dadhoo.database.DadhooDB;
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

	private int myLastVisiblePos;
	
	/* The activity that creates an instance of this dialog fragment must
	    * implement this interface in order to receive event callbacks.
	    * Each method passes the DialogFragment in case the host needs to query it. 
	    */
	   public interface NoticeEventListFragmentListener {
	       public void onScrollDown();
	       public void onScrollUp();
	   }
	   
	   //Use this instance of the interface to deliver action events
	   NoticeEventListFragmentListener mListener;

	   @Override
	   public void onAttach(Activity activity) {
	       super.onAttach(activity);
	       // Verify that the host activity implements the callback interface
	       try {
	           // Instantiate the NoticeFragmentListener so we can send events to the host
	           mListener = (NoticeEventListFragmentListener) activity;
	       } catch (ClassCastException e) {
	           // The activity doesn't implement the interface, throw exception
	           throw new ClassCastException(activity.toString()
	                   + " must implement NoticeDialogListener");
	       }
	   }

	
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

    	View rootView = inflater.inflate(R.layout.gridview_container_event_list, container, false);
    	GridView gridView = (GridView) rootView.findViewById(R.id.gridview_container_event);
    	
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
	        
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
		            	intent.putExtra("album_id", album_id);
		            	startActivity(intent);
	            	}
				}
	        });
		} else {//there are not event yet. Show an icon to easily create one
			
			System.out.println("NO events yet");
			gridView.setAdapter(new ImageAdapterNewEvent(this.getActivity()));
			gridView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	boolean useFragment = false;
	            	if (useFragment) { //multi-pane
	            		//update the fragment contained in the getActivity()
	            	} else {//one-pane call the activity
	            		Intent intent = new Intent(getActivity(), NewEventActivity.class);
	            		intent.putExtra("album_id", album_id);
		     			startActivity(intent);
	            	}
	            }
	        });
		}
		
		myLastVisiblePos = gridView.getFirstVisiblePosition();
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
		    public void onScroll(AbsListView view, int firstVisibleItem,
		            		int visibleItemCount, int totalItemCount) {
				int currentFirstVisPos = view.getFirstVisiblePosition();
				if(currentFirstVisPos > myLastVisiblePos) {
	                mListener.onScrollDown();
	            }
	            if(currentFirstVisPos < myLastVisiblePos) {
	            	mListener.onScrollUp();
	            }
	            myLastVisiblePos = currentFirstVisPos;
		    }

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
		    }
		); 	

      return gridView;
  
	}
}

/**
 * 
 * Adapter used to show an image to add a new album if there is not any yet
 * @author gaecarme
 *
 */
class ImageAdapterNewEvent extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    public ImageAdapterNewEvent(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

       public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
    	ImageView picture;
        TextView name;
     
        if(v == null) {
           v = mInflater.inflate(R.layout.gridview_item, parent, false);
           v.setTag(R.id.picture, v.findViewById(R.id.picture));
           v.setTag(R.id.text, v.findViewById(R.id.text));
           
           picture = (ImageView) v.getTag(R.id.picture);
           
           name = (TextView) v.getTag(R.id.text);
        } else {
            picture = (ImageView) convertView.getTag(R.id.picture);
            name =  (TextView) convertView.getTag(R.id.text);
        }
        
        picture.setScaleType(ScaleType.CENTER);
        picture.setImageResource(R.drawable.ic_action_new);
        LayoutParams params = picture.getLayoutParams();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        picture.setLayoutParams(params);
        picture.setPadding(100, 50, 100, 100);
        
        name.setTextColor(Color.GRAY);
        name.setBackgroundColor(Color.TRANSPARENT);
        name.setPadding(20, 0, 0, 0);
        name.setText(R.string.create_a_new_event);
        name.setTextSize(20);
        return v;
    }

	@Override
	public int getCount() {
		return 1;
	}
}
