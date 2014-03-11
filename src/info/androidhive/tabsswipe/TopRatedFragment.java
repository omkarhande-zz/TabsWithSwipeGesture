package info.androidhive.tabsswipe;

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
import android.widget.Button;
import android.widget.ListView;

public class TopRatedFragment extends Fragment {
	ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
		lv = (ListView) rootView.findViewById(R.id.listView1);   
		
		 String[] values = new String[] { "Bday bash", "Promotion Treat", "School Reunion",
                 "Son's Birthday", "New Year's Eve"};

            ArrayAdapter<String> files = new ArrayAdapter<String>(getActivity(), 
                     android.R.layout.simple_list_item_1, 
                     values);

          lv.setAdapter(files);
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
        d.setTitle("Send Feedback");
        d.setContentView(R.layout.dialog2);
       

      d.show();


    }
}
