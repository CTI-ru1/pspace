package eu.uberdust.mobileclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.uberdust.mobileclient.datasource.DataSource;
import eu.uberdust.mobileclient.datasource.ServerDatabaseHandler;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.Server;
import eu.uberdust.mobileclient.model.Testbed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;




public class ServerPreferencesActivity extends DashboardActivity {

	private SimpleAdapter listAdapter;
	private ListView listView;
	private List<HashMap<String,String>> listMap;
	private Dialog newServerDialog;
	private ProgressDialog loadingDialog;
	private AlertDialog testbedSelect;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serverpreferences);
          
        listView = (ListView)findViewById(R.id.serverpreferenceslist);

        // Create the item mapping
        String[] from = new String[] {"name", "url"};
        int[] to = new int[] { R.id.servernameitem, R.id.serverurlitem};

        listMap = new ArrayList<HashMap<String, String>>();

        // Create and set adapter
        listAdapter = new SimpleAdapter(this, listMap, R.layout.row_serverpreferences, from, to);
        listView.setAdapter(listAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                		listItemClick(position);
                }
            });
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                    int position, long id) {
            			listItemLongClick(position);
            			return false;
            }
        });
        
        fillListFromDatabase();
        
	
	}
	public void onClickAddServer(View v)
    {
		//set up dialog
		newServerDialog = new Dialog(this);
		newServerDialog.setContentView(R.layout.addserverdialog);
		newServerDialog.setTitle("Add server");
		newServerDialog.setCancelable(true);
		Button okButton = (Button) newServerDialog.findViewById(R.id.addserverdialog_okbutton);
		Button cancelButton = (Button) newServerDialog.findViewById(R.id.addserverdialog_cancelbutton);
		okButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	dialogOkClick();
		    }
		  });
		cancelButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	dialogCancelClick();
		    }
		  });
		newServerDialog.show();
    }


	
	
	
	
	public void dialogCancelClick()
	{
		newServerDialog.dismiss();
	}
	private void fillListFromDatabase()
	{
		ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
        listMap.clear();
        List<Server> allServers = serverDatabase.getAllServers();
        for(int i = 0; i < allServers.size() ; i++){
        	HashMap<String, String> tempMap = new HashMap<String, String>();
        	Server tempServer = allServers.get(i);
        	tempMap.put("name", tempServer.getName());
        	tempMap.put("url", tempServer.getUrl());
        	tempMap.put("id", Integer.toString(tempServer.getId()));
        	listMap.add(tempMap);
        }
        serverDatabase.close();
        listAdapter.notifyDataSetChanged();
	}
	public void listItemClick(int position) 
	{
		SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putInt("prefServerId", Integer.parseInt( listMap.get(position).get("id") ) );
		ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
    	Server currentServer = serverDatabase.getServer(Integer.parseInt( listMap.get(position).get("id") ) );
    	prefsEditor.putInt("prefTestbedId",serverDatabase.getDefaultTestbed(currentServer.getId()));
    	prefsEditor.commit();
    	serverDatabase.close();
		new DownloadTask().execute( currentServer.getUrl(), Integer.toString(prefs.getInt("prefTestbedId", 0)) );   
		loadingDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.loading_please_wait), true);
		
	}
	public void listItemLongClick(int position)
	{
		ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
		Server target = new Server();
		target.setId(Integer.parseInt(listMap.get(position).get("id")));
		serverDatabase.deleteServer(target);
		serverDatabase.close();
		fillListFromDatabase();

	}
	
	
	public void dialogOkClick()
	{
		int i;
		EditText serverName = (EditText) newServerDialog.findViewById(R.id.addserverdialog_name);
		EditText serverUrl = (EditText) newServerDialog.findViewById(R.id.addserverdialog_url);
		
		final Server newServer = new Server();
		newServer.setName(serverName.getText().toString());
		newServer.setUrl(serverUrl.getText().toString());

		// Edw prepei na prosti8etai kai default testbed
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
    	    	addServerToDatabase(newServer, item);
    	    }
    	});
    	testbedSelect = builder.create();
    	testbedSelect.show();
		newServerDialog.dismiss();
	}
	
	
	public void addServerToDatabase(Server server, int defTestbed)
	{
		ServerDatabaseHandler serverDatabase = new ServerDatabaseHandler(this);
    	serverDatabase.addServer(server,defTestbed);
    	serverDatabase.close();
    	fillListFromDatabase();
    	testbedSelect.dismiss();
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
	    	finish();
	    	
	    }

	}
	
}
