package info.androidhive.tabsswipe.adapter;

import info.androidhive.tabsswipe.AboutUs;
import info.androidhive.tabsswipe.MenuFragment;
import info.androidhive.tabsswipe.SpecialsFragment;
import info.androidhive.tabsswipe.OrderCart;
import info.androidhive.tabsswipe.FeedbackFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new SpecialsFragment();
		case 1:
			// Games fragment activity
			return new MenuFragment();
		case 2:
			// Movies fragment activity
			return new OrderCart();
		case 3:
			return new FeedbackFragment();
		case 4:
			return new AboutUs();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 5;
	}

}
