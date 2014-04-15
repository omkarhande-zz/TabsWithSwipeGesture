package info.androidhive.tabsswipe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialsFragment extends Fragment {
	ArrayList<String> name;
	ArrayList<String> id;
	ArrayList<String> desc;
	ArrayList<String> rate, image_name;
	Bitmap bmp;
	String did, dname, response, image_url;
	ArrayAdapter<String> adapter,adap_autocomplete;
	SimpleAdapter map_adapter,map_newadapter;
	AutoCompleteTextView atv;
	ListView lv;
	ArrayList<HashMap<String,String>> map_list;
	ArrayList<HashMap<String,Object>> map_newlist;
	ImageView img;
	ProgressBar pb;
	int cust_id, pair_id;
	String server;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		server = getString(R.string.server_global);
		View rootView = inflater.inflate(R.layout.specials_list_layout, container, false);
		lv = (ListView)rootView.findViewById(R.id.listViewFaculty);
		map_newlist = new ArrayList<HashMap<String,Object>>();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		cust_id = Integer.valueOf(prefs.getString("id", ""));
		pair_id = Integer.valueOf(prefs.getString("pair_id", ""));
		GetSpecials task = new GetSpecials();
		task.execute();
		return rootView;
	}
	public void show(final int pos)
    {

//		final Dialog d = new Dialog(this.getActivity(),R.style.cust_dialog);
		final Dialog d = new Dialog(this.getActivity());
        d.setTitle("Add Item");
        d.setContentView(R.layout.dialog);
        String img_name=null;
        
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        TextView tvName = (TextView)d.findViewById(R.id.itemName);
        TextView tvDesc = (TextView)d.findViewById(R.id.itemDesc);
        TextView tvRate = (TextView)d.findViewById(R.id.itemRate);
        img = (ImageView)d.findViewById(R.id.image);
        img.setVisibility(View.GONE);
        pb = (ProgressBar)d.findViewById(R.id.progressBar1);
        img_name = image_name.get(pos);
        image_url = "http://"+server+"/order/images/"+img_name;
        Log.d("img url", image_url);
        GetImage img_task = new GetImage();
        
        
        tvName.setText(name.get(pos));
        tvDesc.setText(desc.get(pos));
        tvRate.setText("Rs. "+rate.get(pos)+"  ");
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
				String str = id.get(pos);
				String rsp = add.addItem(cust_id, Integer.valueOf(str), np.getValue(), pair_id,server);
				d.dismiss();
				String msg = String.valueOf(np.getValue());
				msg = msg+" "+name.get(pos)+" added to cart!";
				Toast.makeText(getActivity(), rsp, Toast.LENGTH_LONG).show();
			}
		});
        d.show();
        img_task.execute();
    }
	
	private class GetSpecials extends AsyncTask<String, Void, Boolean>{

		JSONArray array;
		JSONObject obj;
		String item_name, item_id, item_des, item_rate, item_image;
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://"+server+"/order/specials.php");
				
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntity = responseGet.getEntity();
		        if (resEntity != null) 
		        {  
		                 	response = EntityUtils.toString(resEntity);
		                    Log.d("response", response);
		        }
				array = new JSONArray(response);
				int arrlen = array.length();
				Log.d("Array Length", Integer.toString(arrlen));
				map_newlist = new ArrayList<HashMap<String,Object>>();
				HashMap<String,Object> tmp_newmap;
				id = new ArrayList<String>();
				desc = new ArrayList<String>();
				name = new ArrayList<String>();
				rate = new ArrayList<String>();
				image_name = new ArrayList<String>();
				for(int i=0;i<arrlen;i++)
				{
					
					obj = array.getJSONObject(i);
					item_name = obj.getString("name");
					item_id = obj.getString("id");
					item_des = obj.getString("des");
					item_rate = obj.getString("rate");
					item_image = obj.getString("image_name");
					
					tmp_newmap = new HashMap<String,Object>();
					tmp_newmap.put("name", item_name);
					tmp_newmap.put("id", item_id);
					id.add(item_id);
					desc.add(item_des);
					name.add(item_name);
					rate.add(item_rate);
					image_name.add(item_image);
					tmp_newmap.put("chamber", item_des);
					tmp_newmap.put("image",R.drawable.chicken );
					map_newlist.add(tmp_newmap);
//					Toast.makeText(LoginActivity.this, tmp_fname, Toast.LENGTH_LONG).show();
					
				}
				
			}catch(Exception e){
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			map_newadapter = new SimpleAdapter(getActivity(),map_newlist,R.layout.specials_row_layout,
		    		new String[]{"name","chamber","image"}, 
		    		new int[]{R.id.spName,R.id.spDesc,R.id.imageViewFac}) ;
			lv.setAdapter(map_newadapter);
			lv.setOnItemClickListener(new OnItemClickListener(){
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
					// TODO Auto-generated method stub
					//show();
					show(arg2);
				}			
			});
		}
		
	}
	public class GetImage extends AsyncTask<String, Void, Boolean>{

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
