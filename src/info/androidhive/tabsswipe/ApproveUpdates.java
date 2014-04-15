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

import android.os.AsyncTask;
import android.util.Log;

public class ApproveUpdates {
	
	int req_id;
	String server;
	public void approve(int id, String addr){
		req_id = id;
		server = addr;
		Approve task = new Approve();
		task.execute();
	}

	private class Approve extends AsyncTask<String,Void,Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			JSONArray array;
			JSONObject obj;
			String response = null;
			try{
					HttpClient client = new DefaultHttpClient();  
//					String folder = getString(R.string.server_addr);
					HttpGet get = new HttpGet("http://"+server+"/order/approve_update.php?id="+req_id);
					
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
					
//					Toast.makeText(LoginActivity.this, tmp_fname, Toast.LENGTH_LONG).show();					
				}catch(Exception e){
					
				}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}
}
