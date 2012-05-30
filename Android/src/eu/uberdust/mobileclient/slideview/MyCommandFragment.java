package eu.uberdust.mobileclient.slideview;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import eu.uberdust.mobileclient.R;
import eu.uberdust.mobileclient.model.Capability;
import eu.uberdust.mobileclient.model.GlobalData;
import eu.uberdust.mobileclient.model.Reading;

public final class MyCommandFragment extends Fragment {
	private static  GlobalData gdata;
	
    private Capability currentCapability;
    private String value;
    private String timestamp;
    private String nodename;
	View view;


	public static MyCommandFragment newInstance(Capability capab, String noden ) {
		
		MyCommandFragment fragment;
		fragment = new MyCommandFragment();
		fragment.currentCapability= capab;
		fragment.nodename=noden;
		
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		gdata = (GlobalData) getActivity().getApplicationContext();
		Reading reading = gdata.getData().getReading(currentCapability);
		TextView text3;
		ToggleButton button;
		
		
		if (currentCapability.getAttribute().contains("light") || currentCapability.getAttribute().contains("lamp")){
			view = inflater.inflate(R.layout.slideview_command_light, container, false);
			button =(ToggleButton)view.findViewById(R.id.toggleButton1);
			if(reading.getValue().equals("1.0")){
				button.setChecked(true);
			}
			else{
				button.setChecked(false);
			}
						
			button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        	
		        	Calendar c = Calendar.getInstance(); 
		        	timestamp = String.valueOf(c.getTimeInMillis());
		        	if(isChecked==true)
		        		gdata.getData().sendCommand(currentCapability,timestamp , "1.0");
		        	else
		        		gdata.getData().sendCommand(currentCapability,timestamp , "0.0");
		        }
			});
		}
		else {
			view = inflater.inflate(R.layout.slideview_command_default, container, false);
		}
		
		
		text3 = (TextView) view.findViewById(R.id.nodename);
		text3.setText("Node: "+nodename);

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
