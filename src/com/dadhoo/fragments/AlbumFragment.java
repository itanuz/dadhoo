/**
 * 
 */
package com.dadhoo.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dadhoo.R;
import com.dadhoo.activities.EventsListActivity;

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
        View rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.image);
        gridView.setAdapter(new ImageAdapter(getActivity()));
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
