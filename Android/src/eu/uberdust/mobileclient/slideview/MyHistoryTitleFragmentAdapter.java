package eu.uberdust.mobileclient.slideview;

import android.support.v4.app.FragmentManager;

import com.viewpagerindicator.TitleProvider;

public class MyHistoryTitleFragmentAdapter extends MyHistoryFragmentAdapter implements TitleProvider {
	public MyHistoryTitleFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public String getTitle(int position) {
		return MyHistoryFragmentAdapter.CONTENT[position % CONTENT.length];
	}
}