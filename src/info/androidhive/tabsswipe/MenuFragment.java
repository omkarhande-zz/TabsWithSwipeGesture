package info.androidhive.tabsswipe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends Fragment {
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	ArrayList<String[]> drinks, starters, mainCourse, rice, deserts, bread;
	List<String> drinks_name, starters_name, mainCourse_name, rice_name, deserts_name, bread_name;
	String cust_name, cust_id, pair_id;
	String image_url;
	Bitmap bmp;
	ImageView img;
	ProgressBar pb;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_games, container, false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		cust_name = prefs.getString("name", "");
		cust_id = prefs.getString("id", "");
		pair_id = prefs.getString("pair_id", "");
				expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
				GetMenu task = new GetMenu();
				task.execute();		
		return rootView;
	}
	public void show(final String name, String desc, final String id, String price, String image_name)
    {

		final Dialog d = new Dialog(this.getActivity());
        d.setTitle("Add Item");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        TextView itemName = (TextView) d.findViewById(R.id.itemName);
        TextView itemDesc = (TextView) d.findViewById(R.id.itemDesc);
        TextView itemRate = (TextView) d.findViewById(R.id.itemRate);
        pb = (ProgressBar)d.findViewById(R.id.progressBar1);
        img = (ImageView)d.findViewById(R.id.image);
        img.setVisibility(View.GONE);
        itemName.setText(name);
        itemDesc.setText(desc);
        itemRate.setText("Rs. "+price+"  ");
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
        b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddToCart add = new AddToCart();
				String rsp = add.addItem(Integer.valueOf(cust_id), Integer.valueOf(id), np.getValue(), Integer.valueOf(pair_id));

				
				Toast.makeText(getActivity(),rsp , Toast.LENGTH_LONG).show();
				d.dismiss();
//				Toast.makeText(getActivity(), cust_id+", "+id+", ", Toast.LENGTH_LONG).show();
			}
		});
        d.show();
        image_url = "http://192.168.144.1/order/images/"+image_name;
        GetImage img_task = new GetImage();
        img_task.execute();
//        d.getWindow().setLayout(350, 450);


    }
	private class GetMenu extends AsyncTask<String, Void, Boolean>{
		JSONArray array;
		JSONObject obj;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<String>>();

			// Adding child data
			listDataHeader.add("Drinks");
			listDataHeader.add("Starters");
			listDataHeader.add("Main Course");
			listDataHeader.add("Breads");
			listDataHeader.add("Rice");
			listDataHeader.add("Deserts");
			
			drinks = new ArrayList<String[]>();
			starters = new ArrayList<String[]>();
			mainCourse = new ArrayList<String[]>();
			bread = new ArrayList<String[]>();
			rice = new ArrayList<String[]>();
			deserts = new ArrayList<String[]>();
			
			drinks_name = new ArrayList<String>();
			starters_name = new ArrayList<String>();
			mainCourse_name = new ArrayList<String>();
			bread_name = new ArrayList<String>();
			rice_name = new ArrayList<String>();
			deserts_name = new ArrayList<String>();
			
		
			try{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/menu.php");
				
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntity = responseGet.getEntity();
		        String response = null;
				if (resEntity != null) 
		        {  
		                 	response = EntityUtils.toString(resEntity);
		                    Log.d("response", response);
		        }
				array = new JSONArray(response);
				int arrlen = array.length();
				String item_name, item_id, item_des,item_grp, item_rate, item_image;
				for(int i=0;i<arrlen;i++)
				{
					
					obj = array.getJSONObject(i);
					item_name = obj.getString("name");
					item_id = obj.getString("id");
					item_des = obj.getString("des");
					item_grp = obj.getString("group_id");
					item_rate = obj.getString("rate");
					item_image = obj.getString("image_name");
					Log.d("group", item_grp);
					String strings = item_id+">"+item_name+">"+item_des+">"+item_grp+">"+item_rate+">"+item_image;
					String[] input = strings.split(">");
					if(item_grp.equals("1")){
						drinks.add(input);
						drinks_name.add(item_name);
					}else if(item_grp.equals("2")){
						starters.add(input);
						starters_name.add(item_name);
					}else if(item_grp.equals("3")){
						mainCourse.add(input);
						mainCourse_name.add(item_name);
						
					}else if(item_grp.equals("4")){
						bread.add(input);
						bread_name.add(item_name);
						
					}else if(item_grp.equals("5")){
						rice.add(input);
						rice_name.add(item_name);
					}else if(item_grp.equals("6")){
						deserts.add(input);
						deserts_name.add(item_name);
					}		
				}
				listDataChild.put(listDataHeader.get(0), drinks_name); // Header, Child data
				listDataChild.put(listDataHeader.get(1), starters_name);
				listDataChild.put(listDataHeader.get(2), mainCourse_name);
				listDataChild.put(listDataHeader.get(3), bread_name);
				listDataChild.put(listDataHeader.get(4), rice_name);
				listDataChild.put(listDataHeader.get(5), deserts_name);
				
			}catch(Exception e){
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

			// setting list adapter
			expListView.setAdapter(listAdapter);
			expListView.setOnChildClickListener(new OnChildClickListener(){

				@Override
				public boolean onChildClick(ExpandableListView arg0,
						View arg1, int arg2, int arg3, long arg4) {
					// TODO Auto-generated method stub
					//show();
					//id, name, desc, grp
					String[] retVal = null;
					String sel_name = null, sel_des = null, sel_id=null, sel_rate = null, sel_image = null;
					if(arg2 == 0){
						retVal = drinks.get(arg3);		
					}else if(arg2 == 1){
						retVal = starters.get(arg3);
					}else if(arg2 == 2){
						retVal = mainCourse.get(arg3);
					}else if(arg2 == 3){
						retVal = bread.get(arg3);
					}else if(arg2 == 4){
						retVal = rice.get(arg3);
					}else if(arg2 == 5){
						retVal = deserts.get(arg3);
					}
					Log.d("RetVal", retVal[3]);
					sel_name = retVal[1];
					sel_des = retVal[2];
					sel_id = retVal[0];
					sel_rate = retVal[4];
					sel_image = retVal[5];
//					Toast.makeText(getActivity(), sel_name+", "+sel_des+", "+sel_id, Toast.LENGTH_SHORT).show();
					show(sel_name, sel_des, sel_id, sel_rate, sel_image);
					return false;
				}
				
			});	
		}
		
	}
	private class GetImage extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			URL url = null;
			try {
				url = new URL(image_url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pb.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			img.setImageBitmap(bmp);
		}
		
	}
}
