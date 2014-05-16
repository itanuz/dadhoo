
package com.dadhoo.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.activities.EventsListActivity;
import com.dadhoo.activities.NewAlbumActivity;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.util.ImageFetcherFromFile;

/**
 * Fragment that appears in the "content_frame", shows an Album
 * @author gaecarme
 *
 */

public class AlbumFragment extends Fragment {

    private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageFetcherFromFile mImageFetcher;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcherFromFile(getActivity(), mImageThumbSize);
    	
    	View rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
    	GridView gridView = (GridView) rootView.findViewById(R.id.image);

    	CursorLoader cursorLoader = new CursorLoader(this.getActivity(), 
    												 DadhooDB.Albums.ALBUMS_CONTENT_URI, 
    												 null, 
    												 null, 
    												 null, 
    												 null);
    	Cursor cursor = cursorLoader.loadInBackground();
		
		AlbumImageAdapter albumImageAdapter = new AlbumImageAdapter(this.getActivity(), 
																	R.layout.gridview_item, 
																	cursor, 
																	mImageFetcher,
																	new String[]{DadhooDB.Albums.PICTURE_ID}, 
																	null, 
																	0);
		if(cursor.moveToFirst()){//there are album 
			gridView.setAdapter(albumImageAdapter);		
	        gridView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	boolean useFragment = false;
	            	if (useFragment) { //multi-pane
	            		//update the fragment contained in the getActivity()
	            	} else {//one-pane call the activity
		            	Intent intent = new Intent(getActivity(), EventsListActivity.class);
		            	intent.putExtra("position", position);
		            	intent.putExtra("album_id", id);
		            	startActivity(intent);
	            	}
	            }
	        });
	        
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
		            	Intent intent = new Intent(getActivity(), NewAlbumActivity.class);
		            	intent.putExtra("album_id", id);
		            	startActivity(intent);
	            	}
					return true;
				}
	        });
		} else {//there are not album yet. Show an icon to easily create one
			gridView.setAdapter(new ImageAdapter(this.getActivity()));
			gridView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	boolean useFragment = false;
	            	if (useFragment) { //multi-pane
	            		//update the fragment contained in the getActivity()
	            	} else {//one-pane call the activity
	            		Intent intent = new Intent(getActivity(), NewAlbumActivity.class);
		     			startActivity(intent);
	            	}
	            }
	        });
		}

        return gridView;
    
	}
}

class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    public ImageAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mThumbIds.length;
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
        picture.setImageResource(mThumbIds[position]);
        LayoutParams params = picture.getLayoutParams();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        picture.setLayoutParams(params);
        picture.setPadding(100, 50, 100, 100);
        
        name.setTextColor(Color.GRAY);
        name.setBackgroundColor(Color.TRANSPARENT);
        name.setPadding(20, 0, 0, 0);
        name.setText(R.string.album_new_album);
        name.setTextSize(20);
        return v;
    }

    // references to our images
    private Integer[] mThumbIds = {
    		R.drawable.ic_action_new
    };
}
