package info.androidhive.tabsswipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OrderCart extends Fragment {
	ListView lv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
		lv = (ListView)rootView.findViewById(R.id.cartList);
		String[] values = new String[] { "Butter Chicken", "Chicken Hyderabadi Biryani", "Cheese Garlic Naan",
                "Malai Kofta", "Tandoori Chicken"};

           ArrayAdapter<String> files = new ArrayAdapter<String>(getActivity(), 
                    android.R.layout.simple_list_item_1, 
                    values);

         lv.setAdapter(files);
		
		return rootView;
	}

}
