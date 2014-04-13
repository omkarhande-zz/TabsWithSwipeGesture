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

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderList extends Fragment{

	ListView lv, lvOrder;
	ArrayList<String> id, name, pair_id;
	ArrayAdapter<String> adapter;
	ArrayList<String> orderItems;
	ArrayList<HashMap<String,Object>> map_newlist;
	TextView totalAmount;
	Intent i;
	String orderId = null;
	String waiter_id;
	SimpleAdapter map_adapter,map_newadapter;
	public void show(final int pos)
    {

		final Dialog d = new Dialog(this.getActivity());
        d.setTitle("Order Approval");
        d.setContentView(R.layout.dialog4);
        Button cancel = (Button)d.findViewById(R.id.cancelOrder);
        Button approve = (Button)d.findViewById(R.id.approveOrder);
        totalAmount = (TextView)d.findViewById(R.id.totalamout);
        lvOrder = (ListView)d.findViewById(R.id.order_details);
        orderId = id.get(pos);

       
        cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
        approve.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ApproveOrder order = new ApproveOrder();
				order.approve(Integer.valueOf(id.get(pos)));
				NotifyGCM task  = new NotifyGCM();
				task.notify(2,"Congratulations! Your order is placed and getting ready!", "Order Placed", Integer.valueOf(pair_id.get(pos)));
				
				d.dismiss();
				
				Fragment frg = null;
				frg = getFragmentManager().findFragmentByTag(getTag());
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(frg);
				ft.attach(frg);
				ft.commit();
			}
		});
        ViewOrderDetails task = new ViewOrderDetails();
        
        task.execute();
        d.show();
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		waiter_id = prefs.getString("waiter_id", "");
		View rootView = inflater.inflate(R.layout.order_requests, container, false);
		lv = (ListView)rootView.findViewById(R.id.orders_approval);
		i = new Intent(getActivity(),ViewOrder.class);
		
		GetOrders task = new GetOrders();
		task.execute();
		return rootView;
	}
	private class ViewOrderDetails extends AsyncTask<String, Void, Boolean>{
		String response;
		JSONArray array;
		JSONObject obj;
		String name, total,quant;
		int amount=0;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/view_order.php/?id="+orderId);
				Log.d("URL", "http://192.168.144.1/order/view_order.php/?id="+orderId);
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
				HashMap<String, Object> tmp_newmap;
				map_newlist = new ArrayList<HashMap<String,Object>>();
				for(int i=0;i<arrlen;i++)
				{	
					obj = array.getJSONObject(i);
					name = obj.getString("name");
					total = obj.getString("total");
					quant = obj.getString("quant");
					tmp_newmap = new HashMap<String,Object>();
					tmp_newmap.put("name",quant+" "+name);
					tmp_newmap.put("rate", total);
					amount+=Integer.valueOf(total);
					map_newlist.add(tmp_newmap);
					
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
			map_newadapter = new SimpleAdapter(getActivity(),map_newlist,R.layout.cart_row,
		    		new String[]{"name","rate"}, 
		    		new int[]{R.id.itemName,R.id.itemPrice}) ;
			lvOrder.setAdapter(map_newadapter);
			totalAmount.setText("Rs."+amount);
			
		}
		
	}
	private class GetOrders extends AsyncTask<String,Void,Boolean>{

		String response;
		JSONArray array;
		JSONObject obj;
		String order_id, order_name, order_pair;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			id = new ArrayList<String>();
			name = new ArrayList<String>();
			pair_id = new ArrayList<String>();
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/get_orders.php?waiter_id="+waiter_id);
				
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
					order_pair = obj.getString("pair_id");
				
					id.add(order_id);
					name.add(order_name);			
					pair_id.add(order_pair);
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
//					editor.putString("id",id.get(arg2));
//					editor.putString("waiter_id", waiter_id);
//					editor.putString("pair_id", pair_id.get(arg2));
//				    editor.commit();
//				    startActivity(i);
					show(arg2);
					
				}
				
			});
		}
		
		
	}

}
