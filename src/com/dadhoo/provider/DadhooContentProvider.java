/**
 * 
 */
package com.dadhoo.provider;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.dadhoo.database.DadhooDB;
import com.dadhoo.database.DadhooDbHelper;

/**
 * @author gaecarme
 *
 */
public class DadhooContentProvider extends ContentProvider {

    private static final int ALBUMS = 1;//all albums
    private static final int ALBUM_ID = 2;//only one album
    
    private static final int PICTURES = 3;//only one picture
    private static final int PICTURE_ID = 4;//only one picture
    
    private static final int EVENTS = 5;//all events
    private static final int EVENT_ID = 6;//only one event
    private static final int FILTER_EVENTS_BY_ALBUM_ID = 9;//only one event
    
    private static final int GROUP_EVENTS = 7;//all group_events
    private static final int GROUP_EVENT_ID = 8;//only one group_event
    
    private static UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Albums.ALBUM_NAME, ALBUMS);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Albums.ALBUM_NAME + "/#", ALBUM_ID);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Pictures.PICTURE_NAME, PICTURES);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Pictures.PICTURE_NAME + "/#", PICTURE_ID);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Events.EVENT_NAME, EVENTS);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Albums.ALBUM_NAME + "/#/" + DadhooDB.Events.EVENT_NAME, FILTER_EVENTS_BY_ALBUM_ID);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.Albums.ALBUM_NAME + "/#", EVENT_ID);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.GroupEvents.GROUP_EVENT_NAME, GROUP_EVENTS);
        sUriMatcher.addURI(DadhooDB.AUTHORITY, DadhooDB.GroupEvents.GROUP_EVENT_NAME + "/#", GROUP_EVENT_ID);
    }

    private DadhooDbHelper mOpenDbHelper;

	
	@Override
	public boolean onCreate() {
		mOpenDbHelper = new DadhooDbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		// If no sort order is specified use the default da modificare!!!!
        String orderBy= DadhooDB.Albums.DEFAULT_SORT_ORDER;
        String orderByModified = DadhooDB.Events.DEFAULT_SORT_ORDER;
        
        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
            orderByModified = sortOrder;
        }

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case ALBUMS:
                cursor = getDb().query(DadhooDbHelper.ALBUM_TABLE_NAME, 
                						projection,
                						selection, 
                						selectionArgs,
                						null, 
                						null, 
                						orderBy);
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.Albums.ALBUMS_CONTENT_URI);
                break;
            case ALBUM_ID:
            	// query the database for a specific album
                long albumID = ContentUris.parseId(uri);
                cursor = getDb().query(DadhooDbHelper.ALBUM_TABLE_NAME,
				                		projection,
				                        BaseColumns._ID + " = " + albumID,
				                        selectionArgs, 
				                        null, 
				                        null, 
				                        orderBy);
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.Albums.ALBUMS_CONTENT_URI);
                break;
            case PICTURE_ID:
                // query the database for a specific picture
                long pictureID = ContentUris.parseId(uri);
                cursor = getDb().query(DadhooDbHelper.PICTURE_TABLE_NAME, 
		                				  projection,
		                				  BaseColumns._ID + " = " + pictureID +
		                				  (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
		                                selectionArgs, 
		                                null, 
		                                null, 
		                                null);
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.Pictures.PICTURE_CONTENT_URI);
                break;
            case EVENTS:
            	//query all events
                cursor = getDb().query(DadhooDbHelper.EVENT_TABLE_NAME, 
                						projection,
                						selection, 
                						selectionArgs,
                						null, 
                						null, 
                						orderByModified);
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.Events.EVENTS_CONTENT_URI);
                break;
            case EVENT_ID:
            	//query one event based on a specific ID
            	long eventID = ContentUris.parseId(uri);
                cursor = getDb().query(DadhooDbHelper.EVENT_TABLE_NAME, 
                						projection,
                						BaseColumns._ID + " = " + eventID, 
                						selectionArgs,
                						null, 
                						null, 
                						orderByModified);
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.Events.EVENTS_CONTENT_URI);
                break;
            case FILTER_EVENTS_BY_ALBUM_ID:
            	//query one event based on a specific ID
            	long albumId =  Long.parseLong(uri.getPathSegments().get(1));
                cursor = getDb().rawQuery("SELECT e.* " +
                							" FROM events e, group_events ge, albums a " +
                							"  WHERE a._id = ge.album_id " +
                									" and e._id = ge.event_id " +
                									" and a._id = ?", 
                						   new String[] {Long.toString(albumId)});
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.Events.EVENTS_CONTENT_URI);
                break;
                 
            case GROUP_EVENTS:
            	//query all grroup_events items
                cursor = getDb().query(DadhooDbHelper.GROUP_EVENTS_TABLE_NAME, 
                						projection,
                						selection, 
                						selectionArgs,
                						null, 
                						null, 
                						orderByModified);
                cursor.setNotificationUri(getContext().getContentResolver(), DadhooDB.GroupEvents.GROUP_EVENTS_CONTENT_URI);
                break;    
            default:
                throw new IllegalArgumentException("Unsupported uri: " + uri);
        }

        return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// verifyValues(values);

		getContext().getContentResolver().notifyChange(uri, null);
		
		SQLiteDatabase db = getDb();
		
		int affected;
		switch (sUriMatcher.match(uri)) {
		case ALBUMS:
			affected = db.update(DadhooDbHelper.ALBUM_TABLE_NAME, values, selection, selectionArgs);
			break;
		case PICTURES:
			affected = db.update(DadhooDbHelper.PICTURE_TABLE_NAME, values, selection, selectionArgs);
			break;
		case EVENTS:
			affected = db.update(DadhooDbHelper.EVENT_TABLE_NAME, values, selection, selectionArgs);
			break;
		case GROUP_EVENTS:
			affected = db.update(DadhooDbHelper.GROUP_EVENTS_TABLE_NAME, values, selection, selectionArgs);
			break;	
		default:
			throw new IllegalArgumentException("unsupported uri: " + uri);
			}
		return affected;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		getContext().getContentResolver().notifyChange(uri, null);
		
		SQLiteDatabase db = getDb();
		
		int affected;
		switch (sUriMatcher.match(uri)) {
		case ALBUMS:
			affected = db.delete(DadhooDbHelper.ALBUM_TABLE_NAME, selection, selectionArgs);
			break;
		case PICTURES:
			affected = db.delete(DadhooDbHelper.PICTURE_TABLE_NAME, selection, selectionArgs);
			break;
		case EVENTS:
			affected = db.delete(DadhooDbHelper.EVENT_TABLE_NAME, selection, selectionArgs);
			break;
		case GROUP_EVENTS:
			affected = db.delete(DadhooDbHelper.GROUP_EVENTS_TABLE_NAME, selection, selectionArgs);
			break;	
		default:
			throw new IllegalArgumentException("unsupported uri " + uri);
			}
		return affected;
	}

	@Override
	public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
	        throws OperationApplicationException {
	    final SQLiteDatabase db = getDb();
	    db.beginTransaction();
	    try {
	        ContentProviderResult[] results = super.applyBatch(operations);
	        db.setTransactionSuccessful();
	        return results;
	    } finally {
	        db.endTransaction();
	    }
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
           
        SQLiteDatabase db = getDb();

        switch (sUriMatcher.match(uri)) {
			case ALBUMS:
				// verifyValues(values);
				long albumId = db.insert(DadhooDbHelper.ALBUM_TABLE_NAME, DadhooDB.Albums.ALBUM_NAME, values);
				if (albumId > 0) {
					Uri albumURi = ContentUris.withAppendedId(DadhooDB.Albums.ALBUMS_CONTENT_URI, albumId);
					getContext().getContentResolver().notifyChange(albumURi, null);
					return albumURi;
				}
				throw new SQLException("Failed to insert row into " + uri);
			case PICTURES:
				long pictureId = db.insert(DadhooDbHelper.PICTURE_TABLE_NAME, DadhooDB.Pictures.PICTURE_NAME, values);
				if (pictureId > 0) {
					Uri pictureUri = ContentUris.withAppendedId(DadhooDB.Pictures.PICTURE_CONTENT_URI, pictureId);
					getContext().getContentResolver().notifyChange(pictureUri, null);
					return pictureUri;
				}
				throw new SQLException("Failed to insert row into " + uri);
			case EVENTS:
				// verifyValues(values);
				long eventID = db.insert(DadhooDbHelper.EVENT_TABLE_NAME, DadhooDB.Events.EVENT_NAME, values);
				if (eventID > 0) {
					Uri eventURi = ContentUris.withAppendedId(DadhooDB.Events.EVENTS_CONTENT_URI, eventID);
					getContext().getContentResolver().notifyChange(eventURi, null);
					return eventURi;
				}
				throw new SQLException("Failed to insert row into " + uri);	
			case GROUP_EVENTS:
				// verifyValues(values);
				long grouoEventID = db.insert(DadhooDbHelper.GROUP_EVENTS_TABLE_NAME, DadhooDB.GroupEvents.GROUP_EVENT_NAME, values);
				if (grouoEventID > 0) {
					Uri eventURi = ContentUris.withAppendedId(DadhooDB.GroupEvents.GROUP_EVENTS_CONTENT_URI, grouoEventID);
					getContext().getContentResolver().notifyChange(eventURi, null);
					return eventURi;
				}
				throw new SQLException("Failed to insert row into " + uri);	
	
			
			default:
				throw new IllegalArgumentException("unsupported uri: " + uri);
	}


	}
	
	private SQLiteDatabase getDb() { 
		return mOpenDbHelper.getWritableDatabase(); 
	}
}
