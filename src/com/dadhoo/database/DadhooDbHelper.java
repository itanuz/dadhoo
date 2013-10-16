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

	public static final String SIMPLE_VIDEO = "dadhoo";
    public static final String ALBUM_TABLE_NAME = "albums";

	
    private static final String DATABASE_NAME = SIMPLE_VIDEO + ".db";
    private static int DATABASE_VERSION = 10;

    DadhooDbHelper(Context context) {
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
//                FinchVideo.SimpleVideos.TITLE_NAME + " TEXT, " +
//                FinchVideo.SimpleVideos.DESCRIPTION_NAME + " TEXT, " +
//                FinchVideo.SimpleVideos.URI_NAME + " TEXT);";
        sqLiteDatabase.execSQL(qs);
    }
}



