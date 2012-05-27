/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.uberdust.mobileclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import eu.uberdust.mobileclient.datasource.DataSource;
import eu.uberdust.mobileclient.datasource.ServerDatabaseHandler;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.Server;

/**
 * This is a simple activity that demonstrates the dashboard user interface pattern.
 *
 */

public class HomeActivity extends DashboardActivity 
{

	

/**
 * onCreate - called when the activity is first created.
 * Called when the activity is first created. 
 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
 * 
 * Always followed by onStart().
 *
 */
	public static  Server srv;
	private GlobalData gdata;
	
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_loading);
    SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
    if(prefs.contains("prefServerId")) {
    	ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
    	Server currentServer = serverDatabase.getServer(prefs.getInt("prefServerId", 0));
    	serverDatabase.close();
       	new DownloadTask().execute( currentServer.getUrl(), Integer.toString(prefs.getInt("prefTestbedId", 0)) );      	
    }
    else
    {
    	// Start the wizard
    	startActivity (new Intent(getApplicationContext(), FirstTimeActivity.class));
    	// Finish this activity. Will start again by the wizard
    	this.finish();
    }	  
}


private class DownloadTask extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(String... arguments) {
       
    	long totalSize = 0;
    	
    	gdata = (GlobalData) getApplicationContext();
		// Create a server
	    srv = new Server();
	    // Set server URL
	    srv.setUrl(arguments[0]);
	    // Create and bind to a datasource
	    gdata.setData(new DataSource(srv));
	    // Get a list of available testbeds
	    // This will send a server request
		gdata.setCurrentTestbed(gdata.getData().getRoomsAndNodes(gdata.getData().getTestbeds().get(Integer.parseInt(arguments[1]))));
		gdata.setCurrentVirtualTestbed(gdata.getData().getVirtualRoomsAndNodes(gdata.getData().getTestbeds().get(Integer.parseInt(arguments[1]))));
		return totalSize;
    	
    }
    
    protected void onPostExecute(Long result) {
    	setContentView(R.layout.activity_home);
    }

}




    
/**
 * onDestroy
 * The final call you receive before your activity is destroyed. 
 * This can happen either because the activity is finishing (someone called finish() on it, 
 * or because the system is temporarily destroying this instance of the activity to save space. 
 * You can distinguish between these two scenarios with the isFinishing() method.
 *
 */

protected void onDestroy ()
{
   super.onDestroy ();
}

/**
 * onPause
 * Called when the system is about to start resuming a previous activity. 
 * This is typically used to commit unsaved changes to persistent data, stop animations 
 * and other things that may be consuming CPU, etc. 
 * Implementations of this method must be very quick because the next activity will not be resumed 
 * until this method returns.
 * Followed by either onResume() if the activity returns back to the front, 
 * or onStop() if it becomes invisible to the user.
 *
 */

protected void onPause ()
{
   super.onPause ();
}

/**
 * onRestart
 * Called after your activity has been stopped, prior to it being started again.
 * Always followed by onStart().
 *
 */

protected void onRestart ()
{
   super.onRestart ();
}

/**
 * onResume
 * Called when the activity will start interacting with the user. 
 * At this point your activity is at the top of the activity stack, with user input going to it.
 * Always followed by onPause().
 *
 */

protected void onResume ()
{
   super.onResume ();
}

/**
 * onStart
 * Called when the activity is becoming visible to the user.
 * Followed by onResume() if the activity comes to the foreground, or onStop() if it becomes hidden.
 *
 */

protected void onStart ()
{
   super.onStart ();
}

/**
 * onStop
 * Called when the activity is no longer visible to the user
 * because another activity has been resumed and is covering this one. 
 * This may happen either because a new activity is being started, an existing one 
 * is being brought in front of this one, or this one is being destroyed.
 *
 * Followed by either onRestart() if this activity is coming back to interact with the user, 
 * or onDestroy() if this activity is going away.
 */

protected void onStop ()
{
   super.onStop ();
}


/**
 * Handle the click of a Feature button.
 * 
 * @param v View
 * @return void
 */

public void onClickFeature (View v)
{
    int id = v.getId ();
    switch (id) {
      case R.id.home_btn_feature1 :
           startActivity (new Intent(getApplicationContext(), RoomActivity.class));
           break;
      case R.id.home_btn_feature2 :
           startActivity(new Intent(getApplicationContext(), CommandRoomActivity.class));
           break;
      case R.id.home_btn_feature5 :
           startActivity (new Intent(getApplicationContext(), UsersActivity.class));
           break;
      case R.id.home_btn_feature6 :
           startActivity (new Intent(getApplicationContext(), PreferencesActivity.class));
           break;
      default: 
    	   break;
    }
}

/**
 */
// Click Methods


/**
 */
// More Methods

} // end class
