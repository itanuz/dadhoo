/**
 * 
 */
package com.dadhoo.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.util.ImageWorker;
import com.dadhoo.util.Utils;

/**
 * @author gaecarme
 * 
 */
public class EventAdapter extends SimpleCursorAdapter {

	private final static String TAG = "EventAdapter";

	private Context mContext;
	private Cursor mCursor;
	private ImageWorker mWorker;
	private LayoutInflater mInflater;

	public EventAdapter(Context context, int layout, Cursor c,
			ImageWorker worker, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mCursor = c;
		mWorker = worker;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, String.valueOf(position));

		View v = convertView;
		ImageView picture;
		TextView name;

		if (v == null) {
			Log.d(TAG, "The view is null");
			v = mInflater.inflate(R.layout.event_item, parent, false);
			v.setTag(R.id.picture, v.findViewById(R.id.event_picture));
			v.setTag(R.id.text, v.findViewById(R.id.event_note));
			picture = (ImageView) v.getTag(R.id.picture);
			name = (TextView) v.getTag(R.id.text);
		} else {
			Log.d(TAG, "The view is not null");
			picture = (ImageView) convertView.getTag(R.id.picture);
			name = (TextView) convertView.getTag(R.id.text);
		}

		mCursor.moveToPosition(position);

		String pictureContentUriString = mCursor.getString(5);// Picture URI
		String note = mCursor.getString(2);// Event note

		if (pictureContentUriString != null) {
			Log.d(TAG, "There is an image at " + pictureContentUriString);
			Uri pictureContentUri = ContentUris.withAppendedId(
					DadhooDB.Pictures.PICTURE_CONTENT_URI,
					Long.parseLong(pictureContentUriString));
			mWorker.loadImage(Utils.getPicturePath(pictureContentUri, mContext), picture);
		} else {
			// put here a fake image in case there is no image 
			picture.setImageResource(R.drawable.empty_photo);
		}
		name.setText(note);
		return v;
	}
}
