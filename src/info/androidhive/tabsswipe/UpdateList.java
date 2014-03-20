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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class UpdateList extends Fragment {

	ListView lv;
	ArrayList<String> request_id, name, action,quant, id;
	SimpleAdapter map_adapter,map_newadapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Welcome to update list", Toast.LENGTH_LONG).show();
		View rootView = inflater.inflate(R.layout.update_list, container, false);
		lv = (ListView)rootView.findViewById(R.id.updateList);
		GetUpdates task = new GetUpdates();
		task.execute();
		return rootView;
	}
	public void refreshFragment(){
		Fragment frg = null;
		frg = getFragmentManager().findFragmentByTag(getTag());
		final FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.detach(frg);
		ft.attach(frg);
		ft.commit();
	}
	private class GetUpdates extends AsyncTask<String, Void, Boolean>{
		
		JSONArray array;
		JSONObject obj;
		String response, item_name, item_id, item_action, item_quant;
		ArrayList<HashMap<String, Object>> map_newlist;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/get_requests.php");
				
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
//				request_id, name, action,quant, id;
				id = new ArrayList<String>();
				action = new ArrayList<String>();
				request_id = new ArrayList<String>();
				quant = new ArrayList<String>();
				name = new ArrayList<String>();
				
				for(int i=0;i<arrlen;i++)
				{
					
					obj = array.getJSONObject(i);
					item_name = obj.getString("name");
					item_id = obj.getString("id");
					item_action = obj.getString("type");
					item_quant = obj.getString("quant");
					if(item_action.equals("1")){
						item_action = "Update";
					}else{
						item_action = "Delete";
					}
					
					tmp_newmap = new HashMap<String,Object>();
					tmp_newmap.put("name", item_quant+" "+item_name);
					tmp_newmap.put("id", item_id);
					id.add(item_id);
					name.add(item_name);
					action.add(item_action);
					quant.add(item_quant);
					tmp_newmap.put("chamber", item_action);
					if(item_action.equals("Update")){
						tmp_newmap.put("image",android.R.drawable.ic_input_add);
					}else{
						tmp_newmap.put("image",android.R.drawable.ic_menu_delete);
					}
					map_newlist.add(tmp_newmap);
//					Toast.makeText(LoginActivity.this, tmp_fname, Toast.LENGTH_LONG).show();
					
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
			Log.d("RESULT", String.valueOf(result));
			map_newadapter = new SimpleAdapter(getActivity(),map_newlist,R.layout.update_list_row,
		    		new String[]{"name","chamber","image"}, 
		    		new int[]{R.id.spName,R.id.spDesc,R.id.imageViewFac}) ;
			lv.setAdapter(map_newadapter);
			lv.setOnItemClickListener(new OnItemClickListener(){
	        	 
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
//					Toast.makeText(getActivity(), id.get(arg2), Toast.LENGTH_LONG).show();
					show(Integer.valueOf(id.get(arg2)));
				}
	          });
		}
		
	}
	public void show(final int id)
    {

		final Dialog d = new Dialog(this.getActivity());
        d.setTitle("Request Approval");
        d.setContentView(R.layout.update_dialog);
        ImageButton yes = (ImageButton)d.findViewById(R.id.yes);
        ImageButton no = (ImageButton)d.findViewById(R.id.no);
        Button cancel = (Button)d.findViewById(R.id.cancel);
        yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Code to refresh a fragment !
				ApproveUpdates app = new ApproveUpdates();
				app.approve(id);
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

}
