package info.androidhive.tabsswipe;

import java.util.concurrent.ExecutionException;

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
import android.widget.Toast;

public class AddToCart {
	int cust_id, item_id, quant, pair_id;
	String response;
	JSONArray array;
	JSONObject obj;
	
	public String addItem(int cust, int item, int q, int pair){
		cust_id = cust;
		item_id = item;
		pair_id = pair;
		quant = q;
		Log.d("input", String.valueOf(cust_id)+" "+String.valueOf(item_id)+" "+String.valueOf(quant));
		CheckAndAdd task = new CheckAndAdd();
		try {
			task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	private class CheckAndAdd extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/add.php?"+"cust_id="+cust_id+"&item_id="+item_id+"&quant="+quant);
				
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

				for(int i=0;i<arrlen;i++)
				{	
					obj = array.getJSONObject(i);
					response = obj.getString("response");
				}
				if(response.equals("Request sent for approval")){
					NotifyGCM taskGCM = new NotifyGCM();
					taskGCM.notify(1, "New Update Request", "You have a new update request to approve", Integer.valueOf(pair_id));
				}
				
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
