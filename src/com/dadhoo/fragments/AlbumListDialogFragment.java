/**
 * 
 */
package com.dadhoo.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.dadhoo.database.DadhooDB;

/**
 * @author gaecarme
 *
 */
public class AlbumListDialogFragment extends DialogFragment {
	
	private final static String TAG = "AlbumListDialogFragment";
	
	private ArrayList<Integer> mSelectedAlbumItems;
	
	private int[] mAlbumSelected;
	
	public static AlbumListDialogFragment newInstance(Cursor eventEventAlbumCursor) {
		Log.d(TAG, "New Instance");
		AlbumListDialogFragment fragment = new AlbumListDialogFragment();
		if(eventEventAlbumCursor != null) {
			Bundle fragmentArgs = new Bundle();
			if(eventEventAlbumCursor.moveToFirst()) {
				int[] albumSelected = new int[eventEventAlbumCursor.getCount()];
				int i = 0;
				while (!eventEventAlbumCursor.isAfterLast()) {
					albumSelected[i] = eventEventAlbumCursor.getInt(2);
					eventEventAlbumCursor.moveToNext();
		            i++;
		        }
				fragmentArgs.putIntArray("SELECTED_ALBUMS_ID", albumSelected);
			}
			eventEventAlbumCursor.close();
			fragment.setArguments(fragmentArgs); 
		}
		return fragment;
	}

	public ArrayList<Integer> getmSelectedAlbumItems() {
		return mSelectedAlbumItems;
	}


	public void setmSelectedAlbumItems(ArrayList<Integer> mSelectedAlbumItems) {
		this.mSelectedAlbumItems = mSelectedAlbumItems;
	}


/* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. 
    */
   public interface NoticeDialogListener {
       public void onDialogPositiveClick(DialogFragment dialog);
       public void onDialogNegativeClick(DialogFragment dialog);
   }
   
   //Use this instance of the interface to deliver action events
   NoticeDialogListener mListener;

   @Override
   public void onAttach(Activity activity) {
       super.onAttach(activity);
       // Verify that the host activity implements the callback interface
       try {
           // Instantiate the NoticeDialogListener so we can send events to the host
           mListener = (NoticeDialogListener) activity;
       } catch (ClassCastException e) {
           // The activity doesn't implement the interface, throw exception
           throw new ClassCastException(activity.toString()
                   + " must implement NoticeDialogListener");
       }
   }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAlbumSelected = getArguments() != null ? getArguments().getIntArray("SELECTED_ALBUMS_ID") : null;	
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//Select all albums
		CursorLoader cursorLoaderAlbum = new CursorLoader(this.getActivity(), 
				DadhooDB.Albums.ALBUMS_CONTENT_URI, 
				null, 
				null,
				null, 
				null);
		
		Cursor cursorAlbum = cursorLoaderAlbum.loadInBackground();
		
		final int[] albumIds = new int[cursorAlbum.getCount()];
		//Title list to pass to the dialog
		CharSequence[] albumTitles = new CharSequence[cursorAlbum.getCount()];
		//array to store which album has been selected
		boolean[] selected = new boolean[cursorAlbum.getCount()];
		
		mSelectedAlbumItems = new ArrayList<Integer>(); 
		
		int i = 0;
		cursorAlbum.moveToFirst();
        while (!cursorAlbum.isAfterLast()) {
        	albumIds[i] = cursorAlbum.getInt(0);
        	albumTitles[i] = cursorAlbum.getString(1);
        	selected[i] = false;                
            if(mAlbumSelected != null) {
            	for(int j = 0; j < mAlbumSelected.length; j++) {
            		if(mAlbumSelected[j] == cursorAlbum.getInt(0)) {
            			selected[i] = true;
            			mSelectedAlbumItems.add(albumIds[i]);
            		}
            	}
            }
            cursorAlbum.moveToNext();
            i++;
        }
        cursorAlbum.close();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		 
		// set title
		alertDialogBuilder.setTitle("Link the event with one or more Albums");

		// set dialog message
		alertDialogBuilder
			.setCancelable(true)
			.setMultiChoiceItems(albumTitles, selected, new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which, boolean isChecked) {
	                   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedAlbumItems.add(albumIds[which]);
	                   } else if (mSelectedAlbumItems.contains(albumIds[which])) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedAlbumItems.remove(Integer.valueOf(albumIds[which]));
	                   }
	               }
	           })
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// Send the positive button event back to the host activity
                    mListener.onDialogPositiveClick(AlbumListDialogFragment.this);
				}
			  })
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});

		return alertDialogBuilder.create();
	}

}
