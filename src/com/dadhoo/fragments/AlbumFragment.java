
/**
 * 
 *
 *
 * 
 */
package com.dadhoo.fragments;

import java.net.URI;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.dadhoo.R;
import com.dadhoo.activities.EventsListActivity;
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
                //TODO: check here if there is space or not checking the framework contain 
            	boolean useFragment = false;
            	if (useFragment) { //multi-pane
            		//update the fragment contained in the getActivity()
            	} else {//one-pane call the activity
	            	Intent intent = new Intent(getActivity(), EventsListActivity.class);
	            	intent.putExtra("position", position);
	            	startActivity(intent);
            	}
            }
        });
        return gridView;
    }
}
