/**
 * 
 */
package com.dadhoo.fragments;

import android.app.ListFragment;
import android.os.Bundle;

import com.dadhoo.R;

/**
 * @author gaecarme
 *
 */
public class EventListFragment extends ListFragment {
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);	
      CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), presidents, imageIDs);
      setListAdapter(adapter);
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
