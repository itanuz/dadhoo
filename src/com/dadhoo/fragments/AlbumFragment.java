
/**
 * 
 *
 *
 * 
 */
package com.dadhoo.fragments;

import android.app.Fragment;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.activities.EventsListActivity;
import com.dadhoo.activities.NewAlbumActivity;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.util.ImageFetcherFromFile;

/**
 * @author gaecarme
 *
 */
/**
 * Fragment that appears in the "content_frame", shows an Album
 */
public class AlbumFragment extends Fragment {

    private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageFetcherFromFile mImageFetcher;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //query tutte le pictureContentUri di tutti album
    	//per ogni pictureContentUri query _data e aggiungila all-array 
    	

		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcherFromFile(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
    	
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

        return gridView;
    
	}
}
