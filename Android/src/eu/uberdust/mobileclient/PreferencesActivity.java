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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import eu.uberdust.mobileclient.datasource.DataSource;
import eu.uberdust.mobileclient.datasource.ServerDatabaseHandler;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.Server;
import eu.uberdust.mobileclient.model.Testbed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.TextView;

/**
 * This is the activity for feature 6 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class PreferencesActivity extends PreferenceActivity 
{

	Preferences a;
	private ProgressDialog loadingDialog;
	
/**
 * onCreate
 *
 * Called when the activity is first created. 
 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
 * 
 * Always followed by onStart().
 *
 * @param savedInstanceState Bundle
 */

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences); 
    setContentView (R.layout.activity_preferences);
    setTitleFromActivityLabel (R.id.title_text);
    
    Preference serversbutton = (Preference)findPreference("managesevers");
    serversbutton.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) { 
                    	openServersManager(); 
                        return true;
                    }
                });
    
    final ListPreference testbedslist = (ListPreference)findPreference("selecttestbed");

    
    SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
    ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
	Server currentServer = serverDatabase.getServer(prefs.getInt("prefServerId", 0));
    serverDatabase.close();
    
    
    
    DataSource tempDS = new DataSource(currentServer);
	List<Testbed> testbeds = tempDS.getTestbeds();
	ArrayList<CharSequence> testbedName = new ArrayList<CharSequence>();
	ArrayList<CharSequence> testbedValues = new ArrayList<CharSequence>();
	int i;
	for(i=0;i<testbeds.size();i++) {
		testbedName.add(testbeds.get(i).getName());
		testbedValues.add(Integer.toString(i));
	}
	final CharSequence[] items = testbedName.toArray(new CharSequence[testbedName.size()]);
	final CharSequence[] values = testbedValues.toArray(new CharSequence[testbedValues.size()]);	
	
    testbedslist.setEntries(items);
    testbedslist.setEntryValues(values);

    
    testbedslist.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference,
                Object newValue) {
            	setCurrentTestbed(newValue);
            return true;
        }

});
    
    Preference clearcache = (Preference) findPreference("clearcache");
    clearcache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference arg0) { 
        	clearCache(); 
            return true;
        }
    });
     
}

public void clearCache() 
{
	File dir = new File(Environment.getExternalStorageDirectory() + "/uberdust-mobile/cache");
	if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            new File(dir, children[i]).delete();
        }
    }
}

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Refresh testbeds list
        
        final ListPreference testbedslist = (ListPreference)findPreference("selecttestbed");
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
    	Server currentServer = serverDatabase.getServer(prefs.getInt("prefServerId", 0));
        serverDatabase.close();
        DataSource tempDS = new DataSource(currentServer);
    	List<Testbed> testbeds = tempDS.getTestbeds();
    	ArrayList<CharSequence> testbedName = new ArrayList<CharSequence>();
    	ArrayList<CharSequence> testbedValues = new ArrayList<CharSequence>();
    	int i;
    	for(i=0;i<testbeds.size();i++) {
    		testbedName.add(testbeds.get(i).getName());
    		testbedValues.add(Integer.toString(i));
    	}
    	final CharSequence[] items = testbedName.toArray(new CharSequence[testbedName.size()]);
    	final CharSequence[] values = testbedValues.toArray(new CharSequence[testbedValues.size()]);	
    	
        testbedslist.setEntries(items);
        testbedslist.setEntryValues(values);
   
}


public void setCurrentTestbed(Object newValue)
{
	SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
    ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
	Server currentServer = serverDatabase.getServer(prefs.getInt("prefServerId", 0));
	serverDatabase.setDefaultTestbed(currentServer.getId(), Integer.parseInt(newValue.toString()));
    serverDatabase.close();
    SharedPreferences.Editor prefsEdit = prefs.edit();
    prefsEdit.putInt("prefTestbedId", Integer.parseInt(newValue.toString()));
    prefsEdit.commit();
    
    new DownloadTask().execute( currentServer.getUrl(), Integer.toString(prefs.getInt("prefTestbedId", 0)) );   
	loadingDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.loading_please_wait), true);
}


private class DownloadTask extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(String... arguments) {
       
    	long totalSize = 0;
    	
    	GlobalData gdata = (GlobalData) getApplicationContext();
		// Create a server
	    Server srv = new Server();
	    // Set server URL
	    srv.setUrl(arguments[0]);
	    // Create and bind to a datasource
	    gdata.setData(new DataSource(srv));
	    // Get a list of available testbeds
	    // This will send a server request
		gdata.setCurrentTestbed(gdata.getData().getRoomsAndNodes(gdata.getData().getTestbeds().get(Integer.parseInt(arguments[1]))));
	
		return totalSize;
    	
    }
    
    protected void onPostExecute(Long result) {
    	loadingDialog.dismiss();  	
    }

}



public void openServersManager()
{
	startActivityForResult(new Intent(getApplicationContext(), ServerPreferencesActivity.class),0);
}

/**
 * Handle the click on the home button.
 * 
 * @param v View
 * @return void
 */

public void onClickHome (View v)
{
    goHome (this);
}

/**
 * Handle the click on the search button.
 * 
 * @param v View
 * @return void
 */

public void onClickSearch (View v)
{
    startActivity (new Intent(getApplicationContext(), SearchActivity.class));
}

/**
 * Handle the click on the About button.
 * 
 * @param v View
 * @return void
 */

public void onClickAbout (View v)
{
    startActivity (new Intent(getApplicationContext(), AboutActivity.class));
}



/**
 */
// More Methods

/**
 * Go back to the home activity.
 * 
 * @param context Context
 * @return void
 */

public void goHome(Context context) 
{
    final Intent intent = new Intent(context, HomeActivity.class);
    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity (intent);
}

/**
 * Use the activity label to set the text in the activity's title text view.
 * The argument gives the name of the view.
 *
 * <p> This method is needed because we have a custom title bar rather than the default Android title bar.
 * See the theme definitons in styles.xml.
 * 
 * @param textViewId int
 * @return void
 */

public void setTitleFromActivityLabel (int textViewId)
{
    TextView tv = (TextView) findViewById (textViewId);
    if (tv != null) tv.setText (getTitle ());
} // end setTitleText

    
} // end class
