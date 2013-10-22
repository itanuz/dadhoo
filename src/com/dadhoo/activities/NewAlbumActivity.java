package com.dadhoo.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.dadhoo.R;
import com.dadhoo.provider.DadhooDB;

public class NewAlbumActivity extends Activity {
	
	private EditText mAlbumTitleText;
	private EditText mAlbumBabyName;
	private EditText mAlbumBirthLocation;
	private EditText mAlbumMotherName;
	private EditText mAlbumFatherName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_album);
		
		mAlbumTitleText = (EditText) findViewById(R.id.album_title);
		mAlbumBabyName = (EditText) findViewById(R.id.album_baby_name);
		mAlbumBirthLocation = (EditText) findViewById(R.id.album_birth_location);
		mAlbumMotherName = (EditText) findViewById(R.id.album_mother_name);
		mAlbumFatherName = (EditText) findViewById(R.id.album_father_name);
		
		setupActionBar();
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setTitle(R.string.new_album_actionbar_title);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_album, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_done:
				Uri uriInserted = insertAlbum();
				if(uriInserted != null) {
					Intent intent = new Intent(this, MainActivity.class);
					Toast.makeText(this, "Album created", Toast.LENGTH_LONG).show();
					startActivity(intent);
				}
				return true;
			}
		return super.onOptionsItemSelected(item);
	}

	private Uri insertAlbum() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DadhooDB.Albums.TITLE, mAlbumTitleText.getText().toString());
		contentValues.put(DadhooDB.Albums.BABYNAME, mAlbumBabyName.getText().toString());
		contentValues.put(DadhooDB.Albums.BIRTHLOCATION, mAlbumBirthLocation.getText().toString());
		contentValues.put(DadhooDB.Albums.FATHERNAME, mAlbumFatherName.getText().toString());
		contentValues.put(DadhooDB.Albums.MOTHERNAME, mAlbumMotherName.getText().toString());
		
		return getContentResolver().insert(DadhooDB.Albums.CONTENT_URI, contentValues);
		
	}

}
