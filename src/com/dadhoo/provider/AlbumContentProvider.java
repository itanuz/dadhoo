/**
 * 
 */
package com.dadhoo.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author admin
 *
 */
public class AlbumContentProvider extends ContentProvider {

	public static final String SIMPLE_VIDEO = "dadhoo";
    public static final String ALBUM_TABLE_NAME = "albums";

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
	
    private static class DadhooVideoDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = SIMPLE_VIDEO + ".db";
        private static int DATABASE_VERSION = 10;

        DadhooVideoDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createTable(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ALBUM_TABLE_NAME + ";");
            createTable(sqLiteDatabase);
        }

        private void createTable(SQLiteDatabase sqLiteDatabase) {
            String qs = "CREATE TABLE " + ALBUM_TABLE_NAME + " (" +
                    BaseColumns._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + "";
//                    FinchVideo.SimpleVideos.TITLE_NAME + " TEXT, " +
//                    FinchVideo.SimpleVideos.DESCRIPTION_NAME + " TEXT, " +
//                    FinchVideo.SimpleVideos.URI_NAME + " TEXT);";
            sqLiteDatabase.execSQL(qs);
        }
    }


    private DadhooVideoDbHelper mOpenDbHelper;

	
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
