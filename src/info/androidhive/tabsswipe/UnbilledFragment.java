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

import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class UnbilledFragment extends Fragment{

	String waiter_id;
	ListView lv;
	ArrayList<String> name, id, pair_id;
	ArrayAdapter<String> adapter;
	
	public void show(final int id_order, final int id_pair)
    {

		final Dialog d = new Dialog(this.getActivity());
        d.setTitle("Request Approval");
        d.setContentView(R.layout.dialog3);
     
        Button cancel = (Button)d.findViewById(R.id.cancel);
        Button approve = (Button)d.findViewById(R.id.approve);
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
//				Code to refresh a fragment !
				NotifyGCM task  = new NotifyGCM();
//				int some_id  = Integer.valueOf(waiter_id);
//				Toast.makeText(getActivity(), waiter_id, Toast.LENGTH_LONG).show();
				task.notify(2,"Your bill is on the way", "Bill Processing",id_pair);
				SendForBilling sendForBilling = new SendForBilling();
				sendForBilling.send(id_order, id_pair);
				d.dismiss();
				Fragment frg = null;
				frg = getFragmentManager().findFragmentByTag(getTag());
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(frg);
				ft.attach(frg);
				ft.commit();
			}
		});
        d.show();
        


    }
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		return super.onCreateView(inflater, container, savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		waiter_id = prefs.getString("waiter_id", "");
		View rootView = inflater.inflate(R.layout.unbilled, container, false);
		lv = (ListView)rootView.findViewById(R.id.unbilled);
		GetUnbilled task = new GetUnbilled();
		task.execute();
		return rootView;
	}
	
	private class GetUnbilled extends AsyncTask<String, Void, Boolean>{

		JSONArray array;
		JSONObject obj;
		String response, order_id, order_name,order_pair;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			id = new ArrayList<String>();
			name = new ArrayList<String>();
			pair_id = new ArrayList<String>();
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/get_unbilled.php?waiter_id="+waiter_id);
				
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
					show(Integer.valueOf(id.get(arg2)),Integer.valueOf(pair_id.get(arg2)));
					
				}
				
			});
		}
		
	}

}
