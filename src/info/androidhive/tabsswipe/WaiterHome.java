package info.androidhive.tabsswipe;

import info.androidhive.tabsswipe.adapter.TabsPagerAdapterWaiter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class WaiterHome extends FragmentActivity implements ActionBar.TabListener {

	public int index;
	private ViewPager viewPager;
	private TabsPagerAdapterWaiter mAdapter;
	private ActionBar actionBar;
	String waiter_id, name, pair_id;
	// Tab titles
	private String[] tabs = { "Update Requests", "New Orders", "Unbilled" };
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.waiter_activity_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		int some_id = item.getItemId();
		if(some_id == R.id.action_refresh){
			Intent i = new Intent(WaiterHome.this,WaiterHome.class);
        	finish();
        	startActivity(i);
            return true;
		}else if (some_id == R.id.action_settings){
			return true;
		}else if (some_id == R.id.logout){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.clear();
		    editor.commit();
		    Intent i = new Intent(WaiterHome.this,LoginActivity.class);
		    startActivity(i);
			return true;
		}else{
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waiter_home);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		name = prefs.getString("name", "");
		waiter_id = prefs.getString("id", "");
		pair_id = prefs.getString("pair_id", "");
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("waiter_id", waiter_id);
	    editor.commit();
		viewPager = (ViewPager) findViewById(R.id.pager2);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapterWaiter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				index = position;
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	//	show();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
}
