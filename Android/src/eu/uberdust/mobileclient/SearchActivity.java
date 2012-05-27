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
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.NodeTree;
import eu.uberdust.mobileclient.model.RoomTree;
import eu.uberdust.mobileclient.model.TestbedTree;

/**
 * This is the Search activity in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class SearchActivity extends DashboardActivity 
{
	
	GlobalData gdata;
	TestbedTree currentTestbed;
	TestbedTree testbedVirtual;
	RoomTree room;
	NodeTree node;
	
	ListView lv;
	EditText search;
	
	int real=0;
	int virtual=0;

	List<HashMap<String, Object>> fillMaps;
	
	int i,j,k;
	
	String[] from = new String[] {"Room", "Node"};
	int[] to = new int[] {R.id.room, R.id.node };
	
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_search);
    setTitleFromActivityLabel (R.id.title_text);
 
    
	fillMaps = new ArrayList<HashMap<String, Object>>();
    
    search = (EditText) findViewById(R.id.search);
	search.addTextChangedListener(new TextWatcher(){
        public void afterTextChanged(Editable s) {
        		itemSearch(s.toString());

        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        public void onTextChanged(CharSequence s, int start, int before, int count){}
    });     
    
    
	lv= (ListView)findViewById(R.id.list);
    lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {
        	
        	((GlobalData) getApplicationContext()).setCurrentRoom((RoomTree)fillMaps.get(position).get("Object"));
        	if(real>0){
        		if(position<real)
        			changetoRoom();
        		else
        			changetoVirturalRoom();
        	}
        	else{
        		changetoVirturalRoom();
        	}
        }
    });
    }

public void itemSearch(String in){	
	fillMaps.clear();
	gdata = (GlobalData) getApplicationContext();
	
	
	currentTestbed =gdata.getCurrentTestbed();
	testbedVirtual =gdata.getCurrentVirtualTestbed();
	
	
	real = search (currentTestbed, in);
	virtual = search (testbedVirtual, in);
    
	SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this, fillMaps, R.layout.row, from, to);
    lv.setAdapter(adapter);
    }

private int search(TestbedTree testbed, String in){
	int num=0;
	for(i=0;i<testbed.getRoomnum();i++){
		room=testbed.getRoom(i);
		if(room.getName().contains(in) && !in.equals("")){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("Room",room.getName());
			map.put("Node","");
			map.put("Object", room);
			fillMaps.add(map);
			num++;
		}
		
		for(j=0;j<room.getNodenum();j++){
			node = room.getNode(j);
			//Pattern.compile(Pattern.quote(in), Pattern.CASE_INSENSITIVE).matcher(node.getName()).find();
			if(node.getName().contains(in) && !in.equals("") && !room.getName().contains(in)){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("Room",room.getName());
				map.put("Node", node.getName());
				map.put("Object", room);
				fillMaps.add(map);
				num++;
			}
			
			for(k=0;k<node.getCapabilitiesNum();k++){
				if(node.getCapability(k).getAttribute().contains(in) && !in.equals("") && !node.getName().contains(in) && !room.getName().contains(in)){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Room",room.getName());
					map.put("Node",node.getName());
					map.put("Object",room);
					fillMaps.add(map);
					num++;
				}
			}
		}
	}
	
	return num;
	
}

private void changetoRoom(){
	Intent intent = new Intent(this, CapabilitiesActivity.class);
	this.startActivity(intent);
}

private void changetoVirturalRoom(){
	
	Intent intent = new Intent(this, CommandCapabilitiesActivity.class);
	this.startActivity(intent);
}
	
} // end class
