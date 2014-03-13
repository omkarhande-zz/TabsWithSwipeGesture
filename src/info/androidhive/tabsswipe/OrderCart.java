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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class OrderCart extends Fragment {
	ListView lv;
	int cust_id;
	 ArrayAdapter<String> mAdapter;
	ArrayList<String> orderItems, itemId, itemTotal;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		cust_id = Integer.valueOf(prefs.getString("id", ""));
		View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
		lv = (ListView)rootView.findViewById(R.id.cartList);
//		String[] values = new String[] { "Butter Chicken", "Chicken Hyderabadi Biryani", "Cheese Garlic Naan",
//                "Malai Kofta", "Tandoori Chicken"};
		
		GetOrderDetails task = new GetOrderDetails();
		task.execute();         
		
		return rootView;
	}
	
	private class GetOrderDetails extends AsyncTask<String,Void,Boolean>{
		String response;
		JSONArray array;
		JSONObject obj;
		String name, id, total;
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
				for(int i=0;i<arrlen;i++)
				{	
					obj = array.getJSONObject(i);
					name = obj.getString("name");
					id = obj.getString("id");
					total = obj.getString("total");
					
					
					
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
			mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
	                android.R.id.text1,orderItems);
			
         lv.setAdapter(mAdapter);
         SwipeDismissListViewTouchListener touchListener =
                 new SwipeDismissListViewTouchListener(
                         lv,
                         new SwipeDismissListViewTouchListener.OnDismissCallback() {
                             @Override
                             public void onDismiss(ListView listView, final int[] reverseSortedPositions) {
                            	 new AlertDialog.Builder(getActivity()).setTitle("Warning")
                            	 .setMessage("Are you sure you want to remove this item?")
                            	 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            	     public void onClick(DialogInterface dialog, int whichButton) {
                            	    	 String item = orderItems.get(orderItems.indexOf(lv.getItemAtPosition(reverseSortedPositions[0])));
                            	    	 Toast.makeText(getActivity(),item,Toast.LENGTH_SHORT).show();
//                            	    	 Toast.makeText(getActivity(),orderItems.get(reverseSortedPositions[0])+" Item dismissed",Toast.LENGTH_SHORT).show();
                            	    	 DeleteFromCart delete = new DeleteFromCart();
                            	    	 delete.deleteItem(Integer.valueOf(itemId.get(orderItems.indexOf(lv.getItemAtPosition(reverseSortedPositions[0])))));
                                         
                            	    	 for (int position : reverseSortedPositions) {
                                             mAdapter.remove(mAdapter.getItem(position));
                                         }
                                         mAdapter.notifyDataSetChanged();
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
