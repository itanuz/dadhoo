/**
 * 
 */
package com.dadhoo.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.dadhoo.database.DadhooDbHelper;

/**
 * @author admin
 *
 */
public class AlbumContentProvider extends ContentProvider {

    private static final int ALBUMS = 1;
    private static final int ALBUM_ID = 2;
    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        sUriMatcher.addURI(Album.SIMPLE_AUTHORITY, Album.SimpleVideos.VIDEO_NAME, ALBUMS);
        // use of the hash character indicates matching of an id
//        sUriMatcher.addURI(FinchVideo.SIMPLE_AUTHORITY, FinchVideo.SimpleVideos.VIDEO_NAME + "/#", VIDEO_ID);
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
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
