package eu.uberdust.mobileclient;

import java.util.ArrayList;
import java.util.List;

import eu.uberdust.mobileclient.datasource.DataSource;
import eu.uberdust.mobileclient.datasource.ServerDatabaseHandler;
import eu.uberdust.mobileclient.model.Server;
import eu.uberdust.mobileclient.model.Testbed;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FirstTimeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsttime);
        
        
    }
    
    public void onClickOk(View v)
    {
    	int i;
    	EditText serverName = (EditText) findViewById(R.id.firsttime_servername);
    	EditText serverUrl = (EditText) findViewById(R.id.firsttime_serverurl);
    	
    	ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
    	Server newServer = new Server();
    	newServer.setName(serverName.getText().toString());
    	newServer.setUrl(serverUrl.getText().toString());
    	newServer.setId(0);
    	serverDatabase.addServer(newServer);
    	serverDatabase.close();
    	SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
    	SharedPreferences.Editor prefsEditor = prefs.edit();
    	prefsEditor.putInt("prefServerId", 1);
    	prefsEditor.commit();
    	DataSource tempDS = new DataSource(newServer);
    	List<Testbed> testbeds = tempDS.getTestbeds();
    	ArrayList<CharSequence> testbedName = new ArrayList<CharSequence>();

    	for(i=0;i<testbeds.size();i++) {
    		testbedName.add(testbeds.get(i).getName());
    	}
    	
   	
    	final CharSequence[] items = testbedName.toArray(new CharSequence[testbedName.size()]);
    	

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Please select a testbed:");
    	builder.setItems(items, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	    	SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
    	    	SharedPreferences.Editor prefsEditor = prefs.edit();
    	    	prefsEditor.putInt("prefTestbedId", item);
    	    	prefsEditor.commit();
    	    	setTestbedToDatabase(item);
    	    	returnToHome();
    	    }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    public void setTestbedToDatabase(int testbed)
    {
    	ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
    	serverDatabase.setDefaultTestbed(1, testbed);
    	serverDatabase.close();
    }
    public void returnToHome() 
    {
    	startActivity (new Intent(getApplicationContext(), HomeActivity.class));
    	this.finish();
    }
    
}