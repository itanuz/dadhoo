package com.dadhoo.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dadhoo.R;
import com.dadhoo.adapters.DrawerArrayAdapter;
import com.dadhoo.fragments.AlbumListFragment;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerListener;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mLinkTitle;
    
    private boolean isAlbumCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mTitle = getResources().getString(R.string.title_activity_albums_list);
        mDrawerTitle = getTitle();
        mLinkTitle = getResources().getStringArray(R.array.drawer_link_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new DrawerArrayAdapter(this, mLinkTitle, iconIds));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xffff15));
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerListener = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerListener);
        mDrawerLayout.openDrawer(mDrawerList);
        
        if (savedInstanceState != null) {
            return;
        }
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
    	if (null == fragmentManager.findFragmentByTag("FRAG_ALBUMS")) {
        	fragmentTransaction.add(R.id.content_frame, new AlbumListFragment(), "FRAG_ALBUMS");
        }
        
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	 mDrawerList.setItemChecked(position, true);
             setTitle(mLinkTitle[position]);
             FragmentManager fragmentManager = getSupportFragmentManager();
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             switch (position) {
	     		case 0://new event
	     			Intent intentEvent = new Intent(getBaseContext(), NewEventActivity.class);
	     			intentEvent.putExtra("is_edit", true);
	     			startActivity(intentEvent);
	     			break;
	     		case 1://new album
	     			Intent intentNewAlbum = new Intent(getBaseContext(), NewAlbumActivity.class);
	     			intentNewAlbum.putExtra("is_edit", true);
	     			startActivity(intentNewAlbum);
	     			break;
	     		case 2://event list
	     			Intent intentEventList = new Intent(getBaseContext(), EventsListActivity.class);
	     			startActivity(intentEventList);
	     			break;
	     		case 3://album list
	     			fragmentTransaction.replace(R.id.content_frame, new AlbumListFragment());
	     	        fragmentTransaction.commit();
	     		case 4: //replace the current fragment with the setting fragment
	     			break;
	     		default:
	     			break;
     		}
             
             mDrawerLayout.closeDrawer(mDrawerList);           
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerListener.syncState();
    }

    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerListener.onConfigurationChanged(newConfig);
    }
    
    Integer[] iconIds = {
            R.drawable.ic_action_new_picture,
            R.drawable.ic_action_new_event,
            R.drawable.ic_collections_view_as_list,
            R.drawable.ic_collections_view_as_grid,
            R.drawable.ic_collections_view_as_grid,
    };
}