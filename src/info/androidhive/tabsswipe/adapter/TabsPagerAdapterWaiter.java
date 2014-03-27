package info.androidhive.tabsswipe.adapter;

import info.androidhive.tabsswipe.OrderList;
import info.androidhive.tabsswipe.UnbilledFragment;
import info.androidhive.tabsswipe.UpdateList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapterWaiter extends FragmentPagerAdapter {

	public TabsPagerAdapterWaiter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new UpdateList();
		case 1:
			// Games fragment activity
			return new OrderList();
		case 2:
			// Games fragment activity
			return new UnbilledFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
