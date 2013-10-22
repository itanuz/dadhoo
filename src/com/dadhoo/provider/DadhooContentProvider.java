/**
 * 
 */
package com.dadhoo.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.dadhoo.database.DadhooDbHelper;

/**
 * @author gaecarme
 *
 */
public class DadhooContentProvider extends ContentProvider {

    private static final int ALBUMS = 1;//all albums
    private static final int ALBUM_ID = 2;//only one album
    
    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Albums.ALBUM_NAME, ALBUMS);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Albums.ALBUM_NAME + "/#", ALBUM_ID);
    }

    private static HashMap<String, String> sVideosProjectionMap;
    static {
        // example projection map, not actually used in this application
        sVideosProjectionMap = new HashMap<String, String>();
        sVideosProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
//        sVideosProjectionMap.put(FinchVideo.Videos.TITLE, FinchVideo.Videos.TITLE);
//        sVideosProjectionMap.put(FinchVideo.Videos.VIDEO, FinchVideo.Videos.VIDEO);
//        sVideosProjectionMap.put(FinchVideo.Videos.DESCRIPTION, FinchVideo.Videos.DESCRIPTION);
    }
	
    private DadhooDbHelper mOpenDbHelper;

	
	@Override
	public boolean onCreate() {
		mOpenDbHelper = new DadhooDbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
			case ALBUMS:
				return DadhooDB.Albums.ALBUM_DIR_CONTENT_TYPE;
			case ALBUM_ID:
				return DadhooDB.Albums.ALBUM_ITEM_CONTENT_TYPE;
			default:
				throw new IllegalArgumentException("Unknown video type: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
           
        switch (sUriMatcher.match(uri)) {
			case ALBUMS:
				// verifyValues(values);
				SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
				long rowId = db.insert(DadhooDbHelper.ALBUM_TABLE_NAME, DadhooDB.Albums.ALBUM_NAME, values);
				if (rowId > 0) {
					Uri albumURi = ContentUris.withAppendedId(DadhooDB.Albums.CONTENT_URI, rowId);
					getContext().getContentResolver().notifyChange(albumURi, null);
					return albumURi;
				}
				
				throw new SQLException("Failed to insert row into " + uri);
	//		case EVENTS:
	//			break;
			default:
				throw new IllegalArgumentException("Unknown video type: " + uri);
	}


	}
	
}
