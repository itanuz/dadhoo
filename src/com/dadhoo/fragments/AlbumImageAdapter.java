/**
 * 
 */
package com.dadhoo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
	private LayoutInflater mInflater;
	
	public AlbumImageAdapter(Context context, int layout, Cursor c, ImageWorker worker, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);

		mInflater = LayoutInflater.from(context);
		mContext = context;
		mCursor = c;
		mWorker = worker;
	}

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	ImageView picture;
        TextView name;
//    	
//        if (convertView == null) {  // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(5, 5, 5, 5);
//            
//        } 
        
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
        
        mCursor.moveToPosition(position);
        
        String pictureContentUriString = mCursor.getString(3);//Picture URI
        String title = mCursor.getString(1);//Album title
        
        if (pictureContentUriString != null) {
        	Uri pictureContentUri = ContentUris.withAppendedId(DadhooDB.Pictures.PICTURE_CONTENT_URI, Long.parseLong(pictureContentUriString));
        	mWorker.loadImage(Utils.getPicturePath(pictureContentUri, mContext), picture);
        } else {
        	//put here a fake image to show that there is no image for the album  
	        picture.setImageResource(R.drawable.empty_photo);
        }
        name.setText(title);
    	
        return v;
    }

    // references to our images
    private final List<Uri> mThumbUris = new ArrayList<Uri>();
    
    public void addPictureFileUri(Uri pictureFileUri) {
    	this.mThumbUris.add(pictureFileUri);
    }
    
    private class Item
    {
        final String name;
        final int drawableId;

        Item(String name, int drawableId)
        {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}

