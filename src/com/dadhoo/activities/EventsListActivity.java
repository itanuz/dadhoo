package com.dadhoo.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.dadhoo.R;
import com.dadhoo.fragments.EventListFragment;
import com.dadhoo.fragments.EventListFragment.NoticeEventListFragmentListener;
import com.dadhoo.fragments.EventListHeaderFragment;

public class EventsListActivity extends FragmentActivity implements NoticeEventListFragmentListener {
	private Long album_id;
	private String album_title;
	private String album_picture_id;
	private String album_timestamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent().getExtras() != null) {
			album_id  = getIntent().getExtras().getLong("album_id");
			album_title = getIntent().getExtras().getString("album_title");
			album_picture_id  = getIntent().getExtras().getString("album_picture_id");
			album_timestamp  = getIntent().getExtras().getString("album_timestamp");
		}

		setContentView(R.layout.frag_container_events_list_view);

		FragmentManager fragmentMgr = getSupportFragmentManager();
		FragmentTransaction fragmentTx = fragmentMgr.beginTransaction();
		if (null != album_id  && null == fragmentMgr.findFragmentByTag("FRAG_EVENTS_HEADER")) {
			fragmentTx.add(R.id.event_list_header, 
					EventListHeaderFragment.newInstance(album_id, album_title, album_picture_id, album_timestamp), 
					"FRAG_EVENTS_HEADER");
		}
		
		if (null == fragmentMgr.findFragmentByTag("FRAG_EVENTS_FILTERED_BY_ALBUM_ID")) {
			fragmentTx.add(R.id.event_list_content, null != album_id ? EventListFragment.newInstance(album_id,
																									 album_title,
																									 album_picture_id,
																									 album_timestamp) : new EventListFragment(), 
					"FRAG_EVENTS_FILTERED_BY_ALBUM_ID");
		}
		fragmentTx.commit();
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.add_event:
			Intent intentNewEvent = new Intent(this, NewEventActivity.class);
    		intentNewEvent.putExtra("is_edit", true);
    		intentNewEvent.putExtra("album_id", album_id);
    		intentNewEvent.putExtra("album_title", album_title);	
    		intentNewEvent.putExtra("album_picture_id", album_picture_id);
    		intentNewEvent.putExtra("album_timestamp", album_timestamp); 
    		startActivity(intentNewEvent);
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_list, menu);
		return true;
	}

	@Override
	public void onScrollDown() {
		if (null != getSupportFragmentManager().findFragmentByTag("FRAG_EVENTS_HEADER")) {
			FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
			EventListHeaderFragment headerFragment = (EventListHeaderFragment)getSupportFragmentManager().findFragmentByTag("FRAG_EVENTS_HEADER");
			fragmentTx.hide(headerFragment);
			fragmentTx.commit();
		}
	}

	@Override
	public void onScrollUp() {
		if (null != getSupportFragmentManager().findFragmentByTag("FRAG_EVENTS_HEADER")) {
			FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
			EventListHeaderFragment headerFragment = (EventListHeaderFragment)getSupportFragmentManager().findFragmentByTag("FRAG_EVENTS_HEADER");
			fragmentTx.show(headerFragment);
			fragmentTx.commit();
		}
	}
	
	@Override
    public void onBackPressed() {
    	super.onBackPressed();
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    }
 }
