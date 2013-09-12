package com.dadhoo.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

import com.dadhoo.R;
import com.dadhoo.fragments.CustomArrayAdapter;

public class EventsListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);
//		FragmentManager fragmentMgr = getFragmentManager();
//		FragmentTransaction fragmentTx = fragmentMgr.beginTransaction();
//		fragmentTx.add(R.id.event_list, tag);
//		fragmentTx.commit();

		//---using custom array adapter---
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, presidents, imageIDs);
        setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_list, menu);
		return true;
	}

    String[] presidents = {
            "Hello World",
            "I was born...",
            "My first day at home",
            "My family",
            "My dad and me at the park",
            "My mom",
            "My first tooth",
            "First time in Calabria"
    };

    Integer[] imageIDs = {
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4,
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4
    };	
}
