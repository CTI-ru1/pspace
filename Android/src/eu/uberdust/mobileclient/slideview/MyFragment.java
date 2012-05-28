package eu.uberdust.mobileclient.slideview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import eu.uberdust.mobileclient.R;
import eu.uberdust.mobileclient.ReadingsHistoryActivity;
import eu.uberdust.mobileclient.model.Capability;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.Reading;

public final class MyFragment extends Fragment {
	private static  GlobalData gdata;
	
    private Capability currentCapability;
    private String value;
    private String timestamp;
    private String nodename;
	View view;


	public static MyFragment newInstance(Capability capab, String noden ) {
		
		MyFragment fragment;
		fragment = new MyFragment();
		fragment.currentCapability= capab;
		fragment.nodename=noden;
		
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		gdata = (GlobalData) getActivity().getApplicationContext();
		Reading reading = gdata.getData().getReading(currentCapability);
		value = reading.getValue();
		timestamp = reading.getTimestamp();
		Button buttonGraph;
		TextView text;
		TextView text2;
		TextView text3;
		ProgressBar mProgress;
		
		if(currentCapability.getAttribute().contains("temperature") || currentCapability.getAttribute().contains("ac_temp") ){
			view = inflater.inflate(R.layout.slideview_temperature, container, false);
			mProgress = (ProgressBar) view.findViewById(R.id.vertical_progressbar);
			try{
			mProgress.setProgress((int)Double.parseDouble(value+20));
			}catch (NumberFormatException e){
				mProgress.setProgress(0);
	    	}
		}
		else if (currentCapability.getAttribute().contains("light") || currentCapability.getAttribute().contains("lamp")){
			view = inflater.inflate(R.layout.slideview_light, container, false);
		}
		else {
			view = inflater.inflate(R.layout.slideview_default, container, false);
		}
		
		
		text = (TextView) view.findViewById(R.id.value);
		text2 = (TextView) view.findViewById(R.id.time);
		text3 = (TextView) view.findViewById(R.id.nodename);
		text.setText(value);
		text2.setText("Time: "+ timestamp);
		text3.setText("Node: "+nodename);

		buttonGraph = (Button) view.findViewById(R.id.graph);
		buttonGraph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	gdata.setCurrentCapability(currentCapability);
            	startActivity (new Intent(getActivity(), ReadingsHistoryActivity.class));
            }
        });
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("Value", value);
		outState.putString("Timestamp", timestamp);
		outState.putString("Nodename", nodename);
	}

	
}
