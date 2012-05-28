package eu.uberdust.mobileclient.slideview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import eu.uberdust.mobileclient.R;
import eu.uberdust.mobileclient.datasource.DataSource;
import eu.uberdust.mobileclient.model.Capability;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.Reading;

public final class MyHistoryFragment extends Fragment {
	private static  GlobalData gdata;
	private static final String KEY_CONTENT = "MyHistoryFragment:Content";
	Capability currentCapability;
	private DataSource dataSource;
	public List<Reading> readings; 
	View view;
	ListView lv;
	List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

	String[] from = new String[] {"Timestamp", "Value"};
	int[] to = new int[] {R.id.node, R.id.room };


	public static MyHistoryFragment newInstance(String content) {
		
		MyHistoryFragment fragment;
		fragment = new MyHistoryFragment();
		fragment.mContent = content;
		
		return fragment;
	}
	
	private String mContent = "???";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
		
		gdata = (GlobalData) getActivity().getApplicationContext();
	    currentCapability=gdata.getCurrentCapability();
	    this.dataSource=gdata.getData();
	    readings=dataSource.getReadingsHistory(currentCapability, 10);
	 	int num = readings.size();
	 	GraphViewData[] data = new GraphViewData[num];
	    for(int i=0;i<num;i++){
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("Timestamp", readings.get(i).getTimestamp());
	    	map.put("Value", readings.get(i).getValue());
	    	fillMaps.add(map);
	    	try{
	    	data[i] = new GraphViewData(i, (double) Double.parseDouble(readings.get(i).getValue()));
	    	}
	    	catch (NumberFormatException e){
	    		data[i] = new GraphViewData(i, 0.0d);
	    	}
	    }
	    
	    if(mContent.equals("Graph")){
	    	view = inflater.inflate(R.layout.slideview_graph, container, false);
	    	GraphView graphView;
		 	graphView = new LineGraphView(getActivity().getApplicationContext(),"History");
		 	graphView.addSeries(new GraphViewSeries(data));
		 	graphView.setViewPort(0, 40);
		 	graphView.setScrollable(true);
		 	LinearLayout layout = (LinearLayout) view.findViewById(R.id.graph1);
		 	layout.addView(graphView);
	    }
	    if(mContent.equals("History")){
	    	view = inflater.inflate(R.layout.slideview_history, container, false);
	    	lv= (ListView) view.findViewById(R.id.list);
		    SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), fillMaps, R.layout.row, from, to);
		    lv.setAdapter(adapter);
	    }
	    
	    return view;
	}
		

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	
}
