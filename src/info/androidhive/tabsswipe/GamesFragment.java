package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.NumberPicker;
import android.widget.Toast;

public class GamesFragment extends Fragment {
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_games, container, false);
		// get the listview
				expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
//				expListView.setGroupIndicator(getResources().getDrawable(R.drawable.down));
				// preparing list data
				prepareListData();

				listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

				// setting list adapter
				expListView.setAdapter(listAdapter);
				expListView.setOnChildClickListener(new OnChildClickListener(){

					@Override
					public boolean onChildClick(ExpandableListView arg0,
							View arg1, int arg2, int arg3, long arg4) {
						// TODO Auto-generated method stub
						show();
						
						return false;
					}
					
				});	
		
		return rootView;
	}
	public void show()
    {

		final Dialog d = new Dialog(this.getActivity());
        d.setTitle("Add Item");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

      d.show();


    }
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Drinks");
		listDataHeader.add("Starters");
		listDataHeader.add("Main Course");
		listDataHeader.add("Breads");
		listDataHeader.add("Rice");
		listDataHeader.add("Deserts");

		// Adding child data
		List<String> drinks = new ArrayList<String>();
		drinks.add("Margherita");
		drinks.add("Blood Mary");
		drinks.add("Long Island Ice Tea");
		drinks.add("Blue Lagoon");
		drinks.add("Screw Driver");
		

		List<String> starters = new ArrayList<String>();
		starters.add("Paneer Tikka");
		starters.add("Cheese Pakoda");
		starters.add("Chicken Tikka");
		starters.add("Murgh Malai Tikka");
		starters.add("Sheekh Kebab");

		List<String> mainCourse = new ArrayList<String>();
		mainCourse.add("Paneer Butter Masala");
		mainCourse.add("Dal Makhani");
		mainCourse.add("Malai Kofta");
		mainCourse.add("Murgh Makhni");
		mainCourse.add("Murgh-do-pyaza");
		mainCourse.add("Rogan Josh");
		
		List<String> bread = new ArrayList<String>();
		bread.add("Tandoori Roti");
		bread.add("Butter Roti");
		bread.add("Butter Naan");
		bread.add("Cheese Garlic Naan");
		bread.add("Lachha Parantha");
		
		List<String> rice = new ArrayList<String>();
		rice.add("Plain Rice");
		rice.add("Jeera Ricce");
		rice.add("Veg Hyderabadi Biryani");
		rice.add("Chicken Hyderabadi Biryani");
		rice.add("Mutton Hyderbadi Biryani");
		

		List<String> deserts = new ArrayList<String>();
		deserts.add("Chocolate Fondue");
		deserts.add("Tiramisu");
		deserts.add("Hot Chocolate Fudge");

		listDataChild.put(listDataHeader.get(0), drinks); // Header, Child data
		listDataChild.put(listDataHeader.get(1), starters);
		listDataChild.put(listDataHeader.get(2), mainCourse);
		listDataChild.put(listDataHeader.get(3), bread);
		listDataChild.put(listDataHeader.get(4), rice);
		listDataChild.put(listDataHeader.get(5), deserts);
	}
}
