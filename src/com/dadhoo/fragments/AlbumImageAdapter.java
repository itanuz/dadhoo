/**
 * 
 */
package com.dadhoo.fragments;

import java.util.ArrayList;
import java.util.List;

import com.dadhoo.provider.DadhooDB;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

/**
 * @author gaecarme
 *
 */
public class AlbumImageAdapter extends SimpleCursorAdapter {

	private Context mContext;
	private Cursor mCursor;

	public AlbumImageAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
		mCursor = c;
	}

//    public int getCount() {
//        return mThumbUris.size();
//    }
//
//    public Object getItem(int position) {
//        return null;
//    }
//
//    public long getItemId(int position) {
//        return 0;
//    }
//
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
    	Uri pictureContentUri = Uri.parse(mCursor.getString(8));
    	CursorLoader pictureCursorLoader = new CursorLoader(mContext,
															pictureContentUri,
															null,
															null,
															null,
															null);
    	Cursor pictureCursor = pictureCursorLoader.loadInBackground();
    	
    	Bitmap myBitmap = null;
        if(pictureCursor.moveToFirst()){
        	int thCulumnIndex = pictureCursor.getColumnIndex(DadhooDB.Pictures._DATA);
        	String thumbPath = pictureCursor.getString(thCulumnIndex);
        	myBitmap = BitmapFactory.decodeFile(thumbPath);
        	imageView.setImageBitmap(myBitmap);
        }
    	
//        imageView.setImageURI(mThumbUris.get(position));
        return imageView;
    }

    // references to our images
    private final List<Uri> mThumbUris = new ArrayList<Uri>();
    
    public void addPictureFileUri(Uri pictureFileUri) {
    	this.mThumbUris.add(pictureFileUri);
    }	
}

