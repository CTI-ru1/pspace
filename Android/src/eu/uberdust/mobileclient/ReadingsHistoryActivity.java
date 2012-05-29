package eu.uberdust.mobileclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import eu.uberdust.mobileclient.slideview.MyHistoryFragmentAdapter;
import eu.uberdust.mobileclient.slideview.MyHistoryTitleFragmentAdapter;

public class ReadingsHistoryActivity extends FragmentActivity {
	MyHistoryFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_history);
	    setTitleFromActivityLabel (R.id.title_text);
	    
	    mAdapter = new MyHistoryTitleFragmentAdapter(getSupportFragmentManager());
	    
		mPager = (ViewPager)findViewById(R.id.history_pager);
		mPager.setAdapter(mAdapter);
		
		mIndicator = (TitlePageIndicator) findViewById(R.id.history_indicator);
		mIndicator.setViewPager(mPager);
	}
	
	/**
	 */
	// Click Methods

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

	/**
	 * Show a string on the screen via Toast.
	 * 
	 * @param msg String
	 * @return void
	 */

	public void toast (String msg)
	{
	    Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	} // end toast

	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) 
	{
	    Log.d("Demo", msg);
	    toast (msg);
	}
	
}