package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OrderCart extends Fragment {
	ListView lv;
	TextView tv;
	int cust_id,pair_id, grand_total;
	ArrayAdapter<String> mAdapter;
	SimpleAdapter map_adapter,map_newadapter;
	ArrayList<String> orderItems, itemId, itemTotal;
	ArrayList<HashMap<String,Object>> map_newlist;
	Button b;
	ImageButton ib;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		map_newlist = new ArrayList<HashMap<String,Object>>();
		grand_total = 0;
		cust_id = Integer.valueOf(prefs.getString("id", ""));
		pair_id = Integer.valueOf(prefs.getString("pair_id", ""));
		View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
		tv = (TextView)rootView.findViewById(R.id.totalamout);
		lv = (ListView)rootView.findViewById(R.id.cartList);
		b = (Button)rootView.findViewById(R.id.placeOrder);
		ib = (ImageButton)rootView.findViewById(R.id.refreshCart);
//		String[] values = new String[] { "Butter Chicken", "Chicken Hyderabadi Biryani", "Cheese Garlic Naan",
//                "Malai Kofta", "Tandoori Chicken"};
		
		GetOrderDetails task = new GetOrderDetails();
		task.execute();  
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PlaceOrder task = new PlaceOrder();
				String rsp;
				rsp = task.place(cust_id);
				NotifyGCM notify_waiter = new NotifyGCM();
				notify_waiter.notify(1,"New order approval request","Order Approval", pair_id);
				Toast.makeText(getActivity(), rsp, Toast.LENGTH_LONG).show();
			}
		});
		ib.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
				Fragment frg = null;
				frg = getFragmentManager().findFragmentByTag(getTag());
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(frg);
				ft.attach(frg);
				ft.commit();
			}
		});
		
		return rootView;
	}
	
	private class GetOrderDetails extends AsyncTask<String,Void,Boolean>{
		String response;
		JSONArray array;
		JSONObject obj;
		String name, id, total, quant;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/cart.php/?cust_id="+cust_id);
				
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
				
				orderItems = new ArrayList<String>();
				itemId = new ArrayList<String>();
				itemTotal = new ArrayList<String>();
				HashMap<String,Object> tmp_newmap;
				for(int i=0;i<arrlen;i++)
				{	
					obj = array.getJSONObject(i);
					name = obj.getString("name");
					id = obj.getString("id");
					total = obj.getString("total");
					quant = obj.getString("quant");
					
					tmp_newmap = new HashMap<String,Object>();
					tmp_newmap.put("name",quant+" "+name);
					tmp_newmap.put("rate", total);
					map_newlist.add(tmp_newmap);
					itemId.add(id);
					orderItems.add(name+" - "+total);
					itemTotal.add(total);
					
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
//			ArrayAdapter<String> files = new ArrayAdapter<String>(getActivity(), 
//                    android.R.layout.simple_list_item_1, 
//                    orderItems);
			for (String s : itemTotal){
				grand_total += Integer.valueOf(s);
			}
			 tv.setText("Rs. "+String.valueOf(grand_total));
			 map_newadapter = new SimpleAdapter(getActivity(),map_newlist,R.layout.cart_row,
			    		new String[]{"name","rate"}, 
			    		new int[]{R.id.itemName,R.id.itemPrice}) ;
				lv.setAdapter(map_newadapter);
			
         lv.setAdapter(map_newadapter);
         SwipeDismissListViewTouchListener touchListener =
                 new SwipeDismissListViewTouchListener(
                         lv,
                         new SwipeDismissListViewTouchListener.OnDismissCallback() {
                             @Override
                             public void onDismiss(ListView listView, final int[] reverseSortedPositions) {
                            	 int index;
                            	 Log.d("array", String.valueOf(reverseSortedPositions[0]));
                            	 Log.d("orderItems", orderItems.get(reverseSortedPositions[0]));
                            	 index = reverseSortedPositions[0];
                            	 new AlertDialog.Builder(getActivity()).setTitle("Warning")
                            	 .setMessage("Are you sure you want to remove this item?")
                            	 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            		 
                            	     public void onClick(DialogInterface dialog, int whichButton) {
//                            	    	 Log.d("array-2", String.valueOf(reverseSortedPositions[0]));                           	    
                            	    	 int index = reverseSortedPositions[0];
                            	    	 int deduct = Integer.valueOf(itemTotal.get(index));
//                            	    	 Toast.makeText(getActivity(),orderItems.get(reverseSortedPositions[0])+" Item dismissed",Toast.LENGTH_SHORT).show();
                            	    	 DeleteFromCart delete = new DeleteFromCart();
                            	    	 String rsp;
                            	    	 rsp = delete.deleteItem(Integer.valueOf(itemId.get(index)));
                            	    	 
                            	    	 if(rsp.equals("Item deleted")){
                            	    		 grand_total -= deduct;
                            	    		 tv.setText("Rs. "+String.valueOf(grand_total));
//	                            	    	 for (int position : reverseSortedPositions) {
//	                                             mAdapter.remove(mAdapter.getItem(position));
//	                                         }
	                            	    	 
//	                                         mAdapter.notifyDataSetChanged();
                            	    		 Fragment frg = null;
                            					frg = getFragmentManager().findFragmentByTag(getTag());
                            					final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            					ft.detach(frg);
                            					ft.attach(frg);
                            					ft.commit();
                            	    	 }else{
                            	    		 NotifyGCM notify_waiter = new NotifyGCM();
                            			     notify_waiter.notify(1,"You have a new deletion request","Update Approval", pair_id);
                            	    	 }
                            	    	 Toast.makeText(getActivity(),rsp,Toast.LENGTH_SHORT).show();
                            	     }})
                            	  .setNegativeButton(android.R.string.no, null).show();
                            	 
                             }
                         });
         lv.setOnTouchListener(touchListener);
         // Setting this scroll listener is required to ensure that during ListView scrolling,
         // we don't look for swipes.
         lv.setOnScrollListener(touchListener.makeScrollListener());
		}
		
	}

}
