/**
 * 
 */
package com.dadhoo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * @author gaecarme
 *
 */
public class DadhooDbHelper extends SQLiteOpenHelper {

	public static final String DADHOO = "dadhoo";

	public static final String ALBUM_TABLE_NAME = "albums";
	public static final String EVENT_TABLE_NAME = "events";
    public static final String PICTURE_TABLE_NAME = "pictures";

	
    private static final String DATABASE_NAME = DADHOO + ".db";
    private static int DATABASE_VERSION = 5;

    public DadhooDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDbScheme(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ALBUM_TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PICTURE_TABLE_NAME + ";");
        createDbScheme(sqLiteDatabase);
    }

    private void createDbScheme(SQLiteDatabase sqLiteDatabase) {
        String createAlbumsTable = "CREATE TABLE " + ALBUM_TABLE_NAME + " (" +
						                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "" +
						                DadhooDB.Albums.TITLE + " TEXT, " +
						                DadhooDB.Albums.TIMESTAMP + " LONG, " +
						                DadhooDB.Albums.PICTURE_ID + " TEXT);";
        
        String createEventsTable = "CREATE TABLE " + EVENT_TABLE_NAME + " (" +
						                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "" +
						                DadhooDB.Events.TITLE + " TEXT, " +
						                DadhooDB.Events.NOTE + " TEXT, " +
						                DadhooDB.Events.INSERTED + " LONG, " +
						                DadhooDB.Events.MODIFIED + " LONG, " +
						                DadhooDB.Events.PICTURE_ID + " TEXT);";
        
        String createPicturesTable = " CREATE TABLE " + PICTURE_TABLE_NAME + " (" +
                						BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "" +
                						DadhooDB.Pictures._DATA + " TEXT );";
        
        sqLiteDatabase.execSQL(createAlbumsTable);
        sqLiteDatabase.execSQL(createEventsTable);
        sqLiteDatabase.execSQL(createPicturesTable);
    }
}



