/**
 * 
 */
package com.dadhoo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.dadhoo.R;

/**
 * @author gaecarme
 *
 */
/**
 * Fragment that appears in the "content_frame", shows an Album
 */
public class AlbumFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public AlbumFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.image);
        gridView.setAdapter(new ImageAdapter(getActivity()));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return gridView;
    }
}
