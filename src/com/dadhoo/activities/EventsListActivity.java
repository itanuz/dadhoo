package com.dadhoo.activities;

import com.dadhoo.R;
import com.dadhoo.R.layout;
import com.dadhoo.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EventsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_list, menu);
		return true;
	}

}
