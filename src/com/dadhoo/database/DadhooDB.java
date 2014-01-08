/**
 * 
 */
package com.dadhoo.database;

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

		/**
		 * The album itself
		 */
		public static final String ALBUM_NAME = "album";

		/**
		 *  URI that references all albums
		 */
		public static final Uri ALBUMS_CONTENT_URI = Uri.parse("content://"	+ AUTHORITY + "/" + Albums.ALBUM_NAME);

		/**
		 * The MIME type of {@link #ALBUMS_CONTENT_URI} providing a list of album.
		 */
		public static final String ALBUM_DIR_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.dadhoo.provider.album";

		/**
		 * The MIME type of a {@link #ALBUMS_CONTENT_URI} sub-directory of a single album.
		 */
		public static final String ALBUM_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.com.dadhoo.provider.album";

		/**
		 * The title of the video
		 */
		public static final String TITLE = "title";

		/**
		 * The picture_uri itself
		 */
		public static final String PICTURE_ID = "picture_id";
		
		public static final String USER_ID = "user_id";
		
		
		/**
		 * Name of the column that contains the timestamp when an album
		 * element was inserted into the DadhooContentProvider database.
		 */
		public static final String TIMESTAMP = "timestamp";
	}
	
	/**
	 * Albums columns
	 */
	public static final class Pictures implements BaseColumns {

		/**
		 * The album itself <P>Type: TEXT</P>
		 */
		public static final String PICTURE_NAME = "picture";
		/**
		 *  URI that references all albums
		 */
		public static final Uri PICTURE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PICTURE_NAME);

		/**
		 * The MIME type of {@link #ALBUMS_CONTENT_URI} providing a list of album.
		 */
		public static final String ALBUM_DIR_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.dadhoo.provider.album";

		/**
		 * The MIME type of a {@link #ALBUMS_CONTENT_URI} sub-directory of a single album.
		 */
		public static final String ALBUM_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.com.dadhoo.provider.album";

		/**
		 * The field containing the physical path of the picture on the file system 
		 */
		public static final String _DATA = "_data";
		
	
	}
		
}
