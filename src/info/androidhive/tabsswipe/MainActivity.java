package info.androidhive.tabsswipe;

import info.androidhive.tabsswipe.adapter.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	String server;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	String name, id,pair_id;
	// Tab titles
	private String[] tabs = { "Today's Special", "Menu", "Cart", "Feedback", "About us" };

	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.user_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		int some_id = item.getItemId();
		NotifyGCM menu_action = new NotifyGCM();
		if(some_id == R.id.action_request){
			menu_action.notify(1, "Your assistance has been requested at table - "+id, "Assistance requested", Integer.valueOf(pair_id),server);
            return true;
		}else if (some_id == R.id.action_bill){
//			menu_action.notify(1, "Bill requested at table - "+id, "Bill requested", Integer.valueOf(pair_id));
			AskForBill task = new AskForBill();
			String res = task.ask(Integer.valueOf(id), Integer.valueOf(pair_id),server);
			Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
			return true;
		}else if (some_id == R.id.logout){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.clear();
		    editor.commit();
		    Intent i = new Intent(getBaseContext(),LoginActivity.class);
		    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    finish();
		    startActivity(i);
			return true;
		}else{
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		server = getString(R.string.server_global);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		name = prefs.getString("name", "");
		id = prefs.getString("id", "");
		pair_id = prefs.getString("pair_id", "");
		
		Toast.makeText(MainActivity.this, "Welcome "+name+"!", Toast.LENGTH_SHORT).show();

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

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
