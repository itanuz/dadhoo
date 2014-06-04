/**
 * 
 */
package com.dadhoo.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.util.ImageFetcherFromFile;
import com.dadhoo.util.ImageWorker;
import com.dadhoo.util.Utils;

/**
 * This class represents the header fragment of an event list.
 * A header is a summary of an Album (Image and Title). 
 * 
 * @author gaecarme
 *
 */
public class EventListHeaderFragment extends Fragment {
	
	private static final String TAG = "EventListFragment";
	
	private int mImageThumbSize;
	private ImageFetcherFromFile mImageFetcher;

	private Long album_id;
	private String album_title;
	private String album_picture_id;
	private String album_timestamp;
	
	public static Fragment newInstance(Long album_id, String album_title, String album_picture_id, String timestamp) {
		Log.d(TAG, "New Instance");
		EventListHeaderFragment fragment = new EventListHeaderFragment();
		Bundle fragmentArgs = new Bundle();
		fragmentArgs.putLong("ALBUM_ID", album_id);
		fragmentArgs.putString("ALBUM_TITLE", album_title);
		fragmentArgs.putString("ALBUM_PICTURE_ID", album_picture_id);
		fragmentArgs.putString("ALBUM_TIMESTAMP", timestamp);
		fragment.setArguments(fragmentArgs); 
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		album_id = getArguments() != null ? getArguments().getLong("ALBUM_ID") : null;
		album_title = getArguments() != null ? getArguments().getString("ALBUM_TITLE") : null;
		album_picture_id = getArguments() != null ? getArguments().getString("ALBUM_PICTURE_ID") : null;
		album_timestamp = getArguments() != null ? getArguments().getString("ALBUM_TIMESTAMP") : null;
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);	

    	mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

    	// The ImageFetcher takes care of loading images into our ImageView children asynchronously
    	mImageFetcher = new ImageFetcherFromFile(getActivity(), mImageThumbSize);

    	View view = inflater.inflate(R.layout.event_header, container, false);

    	ImageView picture = (ImageView) view.findViewById(R.id.album_picture_header);
    	if (album_picture_id != null) {
    		Uri pictureContentUri = ContentUris.withAppendedId(DadhooDB.Pictures.PICTURE_CONTENT_URI, Long.parseLong(album_picture_id));
    		mImageFetcher.loadImage(Utils.getPicturePath(pictureContentUri, this.getActivity()), picture);
    	} else {
    		//put here a fake image to show that there is no image for the album  
    		picture.setImageResource(R.drawable.empty_photo);
    	}

    	TextView title = (TextView) view.findViewById(R.id.album_title_header);
    	title.setText(album_title);
    	
    	TextView time = (TextView) view.findViewById(R.id.album_timestamp_header);
    	time.setText(new SimpleDateFormat("d MMM yyyy HH:mm").format(new Date(Long.parseLong(album_timestamp))));
    	
      return view;
	}
}