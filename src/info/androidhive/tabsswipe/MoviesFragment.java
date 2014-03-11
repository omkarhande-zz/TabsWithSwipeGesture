package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;

public class MoviesFragment extends Fragment {
	ArrayList<String> fname;
	ArrayList<String> fid;
	ArrayList<String> url;
	ArrayList<String> fcontact;
	
	String did, dname, response;
	ArrayAdapter<String> adapter,adap_autocomplete;
	SimpleAdapter map_adapter,map_newadapter;
	AutoCompleteTextView atv;
	ListView lv;
	ArrayList<HashMap<String,String>> map_list;
	ArrayList<HashMap<String,Object>> map_newlist;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.specials_list_layout, container, false);
		lv = (ListView)rootView.findViewById(R.id.listViewFaculty);
		map_newlist = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> tmp_newmap = new HashMap<String,Object>();
		tmp_newmap.put("name", "Chicken Handi");
		tmp_newmap.put("id", "1");
		tmp_newmap.put("chamber", "Grilled chicken dipped in tomato gravy");
		tmp_newmap.put("image",R.drawable.chicken );
		map_newlist.add(tmp_newmap);
		tmp_newmap = new HashMap<String,Object>();
		tmp_newmap.put("name", "Paneer Pasanda");
		tmp_newmap.put("id", "2");
		tmp_newmap.put("chamber", "Paneer stuffed with paneer");
		tmp_newmap.put("image",R.drawable.veg2 );
		map_newlist.add(tmp_newmap);
		tmp_newmap = new HashMap<String,Object>();
		tmp_newmap.put("name", "Bhatti da kukkad");
		tmp_newmap.put("id", "2");
		tmp_newmap.put("chamber", "YOYO");
		tmp_newmap.put("image",R.drawable.chicken );
		map_newlist.add(tmp_newmap);
		map_newadapter = new SimpleAdapter(this.getActivity(),map_newlist,R.layout.specials_row_layout,
	    		new String[]{"name","chamber","image"}, 
	    		new int[]{R.id.spName,R.id.spDesc,R.id.imageViewFac}) ;
	    
		//set to the above adapter later
		lv.setAdapter(map_newadapter);
		lv.setOnItemClickListener(new OnItemClickListener(){


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				show();
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

}
