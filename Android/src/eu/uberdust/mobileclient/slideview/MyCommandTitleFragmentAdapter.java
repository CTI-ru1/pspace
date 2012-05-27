package eu.uberdust.mobileclient.slideview;

import android.support.v4.app.FragmentManager;

import com.viewpagerindicator.TitleProvider;

import eu.uberdust.mobileclient.model.RoomTree;

public class MyCommandTitleFragmentAdapter extends MyCommandFragmentAdapter implements TitleProvider {
	public MyCommandTitleFragmentAdapter(FragmentManager fm, RoomTree currentRoom) {
		super(fm, currentRoom);
	}

	@Override
	public String getTitle(int position) {
		return MyCommandFragmentAdapter.CONTENT[position % CONTENT.length];
	}
}