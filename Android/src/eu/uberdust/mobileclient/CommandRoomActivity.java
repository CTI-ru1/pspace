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

import java.util.ArrayList;

import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.TestbedTree;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This is the activity for feature 1 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class CommandRoomActivity extends DashboardActivity 
{

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
	
	TestbedTree currentTestbed;	
	
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_room_command);
    setTitleFromActivityLabel (R.id.title_text);
    
    
    GlobalData gdata = (GlobalData) getApplicationContext();
    
    currentTestbed =gdata.getCurrentVirtualTestbed();
    
    int i;
    
    ListView lv= (ListView)findViewById(R.id.list);
    
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
    lv.setAdapter(adapter);
    
	
	for(i=0;i<currentTestbed.getRoomnum();i++){
    	listItems.add(currentTestbed.getRoom(i).getName());
    	 adapter.notifyDataSetChanged();
    }
    
        

    lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {
        		((GlobalData) getApplicationContext()).setCurrentRoom(currentTestbed.getRoom(position));
        		changetoRoom();
        }
    });
    
}

private void changetoRoom(){
	
	Intent intent = new Intent(this, CommandCapabilitiesActivity.class);
	this.startActivity(intent);
}

    
} // end class