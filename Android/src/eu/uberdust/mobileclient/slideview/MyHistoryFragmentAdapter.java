package eu.uberdust.mobileclient.slideview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

 public class MyHistoryFragmentAdapter extends FragmentPagerAdapter {
	protected static final String[] CONTENT = new String[] { "History", "Graph", };
	
	private int mCount = CONTENT.length;

	public MyHistoryFragmentAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int position) {
		return MyHistoryFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}
	
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}