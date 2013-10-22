/**
 * 
 */
package com.dadhoo.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author gaecarme Public API for the example FinchVideo caching content
 *         provider example.
 * 
 *         The public API for a content provider should only contain information
 *         that should be referenced by content provider clients. Implementation
 *         details such as constants only used by a content provider subclass
 *         should not appear in the provider API.
 * 
 */
public final class DadhooDB {
	public static final int ID_COLUMN = 0;
	public static final int TITLE_COLUMN = 1;
	public static final int DESCRIPTION_COLUMN = 2;
	public static final int THUMB_URI_COLUMN = 3;
	public static final int THUMB_WIDTH_COLUMN = 4;
	public static final int THUMB_HEIGHT_COLUMN = 5;
	public static final int TIMESTAMP_COLUMN = 6;
	public static final int QUERY_TEXT_COLUMN = 7;
	public static final int MEDIA_ID_COLUMN = 8;

	public static final String AUTHORITY = "com.dadhoo.provider";

	/**
	 * Albums columns
	 */
	public static final class Albums implements BaseColumns {
		public static final String DEFAULT_SORT_ORDER = "timestamp DESC";

		// This class cannot be instantiated
		private Albums() {
		}

		// uri references all albums
		public static final Uri ALBUMS_URI = Uri.parse("content://"	+ AUTHORITY + "/" + Albums.ALBUM_NAME);

		/**
		 * The content:// style URI for this table
		 */
		public static final Uri CONTENT_URI = ALBUMS_URI;

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a list of album.
		 */
		public static final String ALBUM_DIR_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.dadhoo.provider.album";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * video.
		 */
		public static final String ALBUM_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.com.dadhoo.provider.album";

		/**
		 * The album itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ALBUM_NAME = "album";

		/**
		 * The title of the video
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String TITLE = "title";

		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BABYNAME = "babyname";

		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BIRTHDATE = "birthdate";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String FATHERNAME = "fathername";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String MOTHERNAME = "mothername";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BIRTHLOCATION = "birthlocation";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String PICTURE_ID = "picture_id";
		
		public static final String USER_ID = "user_id";
		
		
		/**
		 * Name of the column that contains the timestamp when a YouTube media
		 * element was inserted into the FinchVideoContentProvider database.
		 */
		public static final String TIMESTAMP = "timestamp";

		/**
		 * Name of the thumb data column.
		 */
		public static final String _DATA = "_data";
	}

	/**
	 * Videos content provider public API for more advanced videos example.
	 */
	public static final class Videos implements BaseColumns {
		// This class cannot be instantiated
		private Videos() {
		}

//		// uri references all videos
//		public static final Uri VIDEOS_URI = Uri.parse("content://" + AUTHORITY
//				+ "/" + Albums.VIDEO_NAME);
//
//		public static final Uri THUMB_URI = Uri.parse("content://" + AUTHORITY
//				+ "/" + Videos.THUMB);
//
//		/**
//		 * The content:// style URI for this table
//		 */
//		public static final Uri CONTENT_URI = VIDEOS_URI;
//
//		/**
//		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
//		 */
//		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.finch.video";
//
//		/**
//		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
//		 * video.
//		 */
//		public static final String CONTENT_VIDEO_TYPE = "vnd.android.cursor.item/vnd.finch.video";
//
//		/**
//		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
//		 * thumbnail.
//		 */
//		public static final String CONTENT_THUMB_TYPE = "vnd.android.cursor.item/vnd.finch.videoThumb";

		/**
		 * The title of the video
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String TITLE = "title";

		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BABYNAME = "babyname";

		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BIRTHDATE = "birthdate";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String FATHERNAME = "fathername";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String MOTHERNAME = "mothername";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BIRTHLOCATION = "birthlocation";
		
		/**
		 * The video itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String PICTURE_ID = "picture_id";
		
		public static final String USER_ID = "user_id";
		
		
		/**
		 * Name of the column that contains the timestamp when a YouTube media
		 * element was inserted into the FinchVideoContentProvider database.
		 */
		public static final String TIMESTAMP = "timestamp";

		/**
		 * Name of the thumb data column.
		 */
		public static final String _DATA = "_data";
	}
}
