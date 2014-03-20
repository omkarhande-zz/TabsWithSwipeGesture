package info.androidhive.tabsswipe;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;

public class OrderList extends Fragment{

	ListView lv;
	ArrayList<String> id, name;
	ArrayAdapter<String> adapter;
	Intent i;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.order_requests, container, false);
		lv = (ListView)rootView.findViewById(R.id.orders_approval);
		i = new Intent(getActivity(),ViewOrder.class);
		GetOrders task = new GetOrders();
		task.execute();
		return rootView;
	}
	
	private class GetOrders extends AsyncTask<String,Void,Boolean>{

		String response;
		JSONArray array;
		JSONObject obj;
		String order_id, order_name;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			id = new ArrayList<String>();
			name = new ArrayList<String>();
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/get_orders.php");
				
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntity = responseGet.getEntity();
		        if (resEntity != null) 
		        {  
		                 	response = EntityUtils.toString(resEntity);
		                    Log.d("response-orders", response);
		        }
				array = new JSONArray(response);
				int arrlen = array.length();
				Log.d("Array Length", Integer.toString(arrlen));
				
				for(int i=0;i<arrlen;i++)
				{
					
					obj = array.getJSONObject(i);
					order_id = obj.getString("id");
					order_name = obj.getString("name");
				
					id.add(order_id);
					name.add(order_name);			
					
				}
				return true;
			}catch(Exception e){
				Log.d("Error", e.getMessage());
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		    final SharedPreferences.Editor editor = prefs.edit();
		    
			adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
	                android.R.id.text1,name);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
//					Toast.makeText(getActivity(), id.get(arg2), Toast.LENGTH_SHORT).show();
					editor.putString("id",id.get(arg2));
				    editor.commit();
				    startActivity(i);
					
				}
				
			});
		}
		
		
	}

}
