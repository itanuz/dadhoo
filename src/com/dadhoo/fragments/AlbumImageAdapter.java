/**
 * 
 */
package com.dadhoo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.util.ImageWorker;
import com.dadhoo.util.Utils;

/**
 * @author gaecarme
 *
 */
public class AlbumImageAdapter extends SimpleCursorAdapter {

	private Context mContext;
	private Cursor mCursor;
	private ImageWorker mWorker;

	public AlbumImageAdapter(Context context, int layout, Cursor c, ImageWorker worker, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
		mCursor = c;
		mWorker = worker;
	}

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
            
        } else {
            imageView = (ImageView) convertView;
        }
        mCursor.moveToPosition(position);
        
        String pictureContentUriString = mCursor.getString(3);
        if (pictureContentUriString != null) {
        	Uri pictureContentUri = ContentUris.withAppendedId(DadhooDB.Pictures.PICTURE_CONTENT_URI, Long.parseLong(pictureContentUriString));
        	mWorker.loadImage(Utils.getPicturePath(pictureContentUri, mContext), imageView);
        } else {
        	//put here a fake image to show that there is no image for the album  
	        imageView.setImageResource(R.drawable.empty_photo);
        }
    	
        return imageView;
    }

    // references to our images
    private final List<Uri> mThumbUris = new ArrayList<Uri>();
    
    public void addPictureFileUri(Uri pictureFileUri) {
    	this.mThumbUris.add(pictureFileUri);
    }	
}

