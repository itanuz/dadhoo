/**
 * 
 */
package com.dadhoo.database;

import com.dadhoo.provider.DadhooContentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author gaecarme 
 * 
 * This class contains constants to be used by the database helper {@link DadhooDbHelper}
 * and the ContentProvider {@link DadhooContentProvider} 
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
		 * The picture_id itself
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
	 * Events columns
	 */
	public static final class Events implements BaseColumns {
		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		// This class cannot be instantiated
		private Events() {
		}

		/**
		 * The events itself
		 */
		public static final String EVENT_NAME = "event";

		/**
		 *  URI that references all events
		 */
		public static final Uri EVENTS_CONTENT_URI = Uri.parse("content://"	+ AUTHORITY + "/" + Events.EVENT_NAME);
		
		public static final Uri EVENT_ID_CONTENT_URI = Uri.parse("content://"	+ AUTHORITY + "/" + Events.EVENT_NAME + "/#");

		public static final Uri FILTER_EVENTS_BY_ALBUM_ID_URI = Uri.parse("content://"	+ AUTHORITY + "/" + Albums.ALBUM_NAME + "/" + "#" + "/" + Events.EVENT_NAME);
		
		/**
		 * The MIME type of {@link #EVENTS_CONTENT_URI} providing a list of event.
		 */
		public static final String EVENT_DIR_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.dadhoo.provider.event";

		/**
		 * The MIME type of a {@link #EVENTS_CONTENT_URI} sub-directory of a single album.
		 */
		public static final String EVENT_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.com.dadhoo.provider.event";

		/**
		 * The title of the event
		 */
		public static final String TITLE = "title";

		/**
		 * The note of the event
		 */
		public static final String NOTE = "note";
		
		/**
		 * Name of the column that contains the timestamp when an album
		 * element was inserted into the DadhooContentProvider database.
		 */
		public static final String INSERTED = "inserted";

		/**
		 * Name of the column that contains the timestamp when an album
		 * element was modified into the DadhooContentProvider database.
		 */
		public static final String MODIFIED = "modified";
		
		/**
		 * The picture_id itself
		 */
		public static final String PICTURE_ID = "picture_id";
		
		public static final String USER_ID = "user_id";
		
		
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
	
	/**
	 * Events columns
	 */
	public static final class GroupEvents implements BaseColumns {
		public static final String DEFAULT_SORT_ORDER = "modified DESC";

		// This class cannot be instantiated
		private GroupEvents() {
		}

		/**
		 * The group_events itself
		 */
		public static final String GROUP_EVENT_NAME = "group_event";

		/**
		 *  URI that references all events
		 */
		public static final Uri GROUP_EVENTS_CONTENT_URI = Uri.parse("content://"	+ AUTHORITY + "/" + GROUP_EVENT_NAME);
		
		/**
		 *  URI that references all events
		 */
		public static final Uri GROUP_EVENTS_BY_ALBUM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + 
																							Albums.ALBUM_NAME + "/#/" + Events.EVENT_NAME + "/#");

		/**
		 * The MIME type of {@link #GROUP_EVENTS_CONTENT_URI} providing a list of event.
		 */
		public static final String GROUP_EVENT_DIR_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.dadhoo.provider.group_event";

		/**
		 * The MIME type of a {@link #GROUP_EVENTS_CONTENT_URI} sub-directory of a single album.
		 */
		public static final String GROUP_EVENT_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.com.dadhoo.provider.group_event";

		/**
		 * The event id
		 */
		public static final String EVENT_ID = "event_id";

		/**
		 * The album id
		 */
		public static final String ALBUM_ID = "album_id";
		
		/**
		 * Name of the column that contains the time stamp when an album
		 * element was modified into the DadhooContentProvider database.
		 */
		public static final String MODIFIED = "modified";
	}
}
