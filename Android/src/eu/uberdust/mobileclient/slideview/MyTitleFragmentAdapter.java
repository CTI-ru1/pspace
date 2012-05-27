package eu.uberdust.mobileclient.slideview;

import android.support.v4.app.FragmentManager;

import com.viewpagerindicator.TitleProvider;

import eu.uberdust.mobileclient.model.RoomTree;

public class MyTitleFragmentAdapter extends MyFragmentAdapter implements TitleProvider {
	public MyTitleFragmentAdapter(FragmentManager fm, RoomTree currentRoom) {
		super(fm, currentRoom);
	}

	@Override
	public String getTitle(int position) {
		return MyFragmentAdapter.CONTENT[position % CONTENT.length];
	}
}