
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

import com.dadhoo.R;
import com.dadhoo.activities.EventsListActivity;
import com.dadhoo.activities.NewAlbumActivity;
import com.dadhoo.provider.DadhooDB;

/**
 * @author gaecarme
 *
 */
/**
 * Fragment that appears in the "content_frame", shows an Album
 */
public class AlbumFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //query tutte le pictureContentUri di tutti album
    	//per ogni pictureContentUri query _data e aggiungila all-array 
    	
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
				R.layout.fragment_album_list, 
				cursor, 
				new String[]{DadhooDB.Albums.PICTURE_URI}, 
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
            	if (useFragment) { //multi-pane
            		//update the fragment contained in the getActivity()
            	} else {//one-pane call the activity
	            	Intent intent = new Intent(getActivity(), NewAlbumActivity.class);
	            	//Bundle bundle = new Bundle();
	            	//intent.putExtras(bundle);	            	
	            	
	            	intent.putExtra("album_id", id);
	            	
	            	startActivity(intent);
            	}
				return false;
			}

        
        
        });
        
        
        return gridView;
    }
}
