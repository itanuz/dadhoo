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

public class EventsListActivity extends FragmentActivity {
	private Long album_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent().getExtras() != null) {
			album_id  = getIntent().getExtras().getLong("album_id");
		}

		setContentView(R.layout.activity_events_list_view);
		
		FragmentManager fragmentMgr = getSupportFragmentManager();
		FragmentTransaction fragmentTx = fragmentMgr.beginTransaction();
		if (null == fragmentMgr.findFragmentByTag("FRAG_EVENTS_FILTERED_BY_ALBUM_ID")) {
			fragmentTx.add(R.id.event_list, null != album_id ? EventListFragment.newInstance(album_id) : new EventListFragment(), 
					"FRAG_EVENTS_FILTERED_BY_ALBUM_ID");
		}
		fragmentTx.addToBackStack(null);
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
//			NavUtils.navigateUpFromSameTask(this);
			Intent intent = new Intent(this, MainActivity.class);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_list, menu);
		return true;
	}
 }
