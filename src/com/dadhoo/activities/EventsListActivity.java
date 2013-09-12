package com.dadhoo.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

import com.dadhoo.R;
import com.dadhoo.fragments.EventListFragment;

public class EventsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list_view);
		
		FragmentManager fragmentMgr = getFragmentManager();
		FragmentTransaction fragmentTx = fragmentMgr.beginTransaction();
		if (null == fragmentMgr.findFragmentByTag("FRAG_2")) {
			fragmentTx.add(R.id.event_list, new EventListFragment(), "FRAG_2");
		}
		fragmentTx.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_list, menu);
		return true;
	}
 }
