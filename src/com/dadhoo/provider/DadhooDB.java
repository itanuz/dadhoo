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
	public static final String AUTHORITY = "com.dadhoo.provider.dadhoocontentprovider";

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
		 * The babyname itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BABYNAME = "babyname";

		/**
		 * The birthdate itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BIRTHDATE = "birthdate";
		
		/**
		 * The fathername itself
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
		 * The birthlocation itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BIRTHLOCATION = "birthlocation";
		
		/**
		 * The picture_id itself
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String PICTURE_ID = "picture_id";
		
		public static final String USER_ID = "user_id";
		
		
		/**
		 * Name of the column that contains the timestamp when an album
		 * element was inserted into the DadhooContentProvider database.
		 */
		public static final String TIMESTAMP = "timestamp";

		/**
		 * Name of the thumb data column.
		 */
		public static final String _DATA = "_data";
	}
}
