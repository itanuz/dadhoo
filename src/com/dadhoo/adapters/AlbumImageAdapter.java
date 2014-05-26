/**
 * 
 */
package com.dadhoo.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dadhoo.R;
import com.dadhoo.database.DadhooDB;
import com.dadhoo.database.pojo.Album;
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

    
    /* create a new  gridview_item for each item contained in the cursor
     * 
     * @see android.widget.CursorAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	ImageView picture;
        TextView name;
     
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
        String title =  mCursor.getString(1);//Album title
        
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
    
    @Override
	public Object getItem(int position) {
		mCursor.moveToPosition(position);
		
		Album album = new Album();
		album.setId(mCursor.getInt(0));
		album.setTitle(mCursor.getString(1));
		album.setPictureId(mCursor.getString(3));
		album.setTimestamp(mCursor.getString(2));
		
		return album;//Album title
	}
}

