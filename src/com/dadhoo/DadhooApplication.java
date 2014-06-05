package com.dadhoo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * This class is used to initialize Parse.It is called one time before any application component
 *  
 * @author gaecarme
 *
 */
public class DadhooApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();

		/*
		 * In this tutorial, we'll subclass ParseObject for convenience to
		 * create and modify Meal objects
		 */
//		ParseObject.registerSubclass(Album.class);
//		ParseObject.registerSubclass(Event.class);

		/*
		 * Fill in this section with your Parse credentials
		 */
		Parse.initialize(this, "duB5Cz0lEKhfuW6sl0aDYmYYXzRtOxPveVmvcZMk", "sy4ENhFv3e6zVNFCHyMb7SVcFdZpqXggCSz20T2g");

		/*
		 * This app lets an anonymous user create and save photos of meals
		 * they've eaten. An anonymous user is a user that can be created
		 * without a username and password but still has all of the same
		 * capabilities as any other ParseUser.
		 * 
		 * After logging out, an anonymous user is abandoned, and its data is no
		 * longer accessible. In your own app, you can convert anonymous users
		 * to regular users so that data persists.
		 * 
		 * Learn more about the ParseUser class:
		 * https://www.parse.com/docs/android_guide#users
		 */
		ParseUser.enableAutomaticUser();

		/*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 */
		ParseACL defaultACL = new ParseACL();

		/*
		 * If you would like all objects to be private by default, remove this
		 * line
		 */
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}
}
