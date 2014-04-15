package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewOrder extends Activity{

	ListView lv;
	Button b;
	String id, waiter_id, pair_id;
	ArrayList<String> orderItems;
	ArrayAdapter<String> adapter;
	String server;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		server = getString(R.string.server_global);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		id = prefs.getString("id", "");
		pair_id = prefs.getString("pair_id", "");
		waiter_id = prefs.getString("waiter_id", "");
		setContentView(R.layout.view_order);
		lv = (ListView)findViewById(R.id.items);
		b = (Button)findViewById(R.id.approveOrder);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ApproveOrder order = new ApproveOrder();
				order.approve(Integer.valueOf(id),server);
				NotifyGCM task  = new NotifyGCM();
				task.notify(2,"Congratulations! Your order is placed and getting ready!", "Order Placed", Integer.valueOf(pair_id),server);
				onBackPressed();
			}
		});
		ViewOrderDetails task = new ViewOrderDetails();
		task.execute();
	}
	
	private class ViewOrderDetails extends AsyncTask<String, Void, Boolean>{
		String response;
		JSONArray array;
		JSONObject obj;
		String name, total,quant;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/view_order.php/?id="+id);
				Log.d("URL", "http://192.168.144.1/order/view_order.php/?id="+id);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntity = responseGet.getEntity();
		        if (resEntity != null) 
		        {  
		                 	response = EntityUtils.toString(resEntity);
		                    Log.d("response-cart-items", response);
		        }
				array = new JSONArray(response);
				int arrlen = array.length();
				Log.d("Array Length", Integer.toString(arrlen));
				
				orderItems = new ArrayList<String>();
				
				for(int i=0;i<arrlen;i++)
				{	
					obj = array.getJSONObject(i);
					name = obj.getString("name");
					total = obj.getString("total");
					quant = obj.getString("quant");
					HashMap<String, Object> tmp_newmap = new HashMap<String,Object>();
					tmp_newmap.put("name",quant+" "+name);
					tmp_newmap.put("rate", total);
					
					
					
					orderItems.add(name+" - "+total);
					
//					Toast.makeText(LoginActivity.this, tmp_fname, Toast.LENGTH_LONG).show();
					
				}
				return true;
			}catch(Exception e){
				
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,
	                android.R.id.text1,orderItems);
			lv.setAdapter(adapter);
			
		}
		
	}

}
