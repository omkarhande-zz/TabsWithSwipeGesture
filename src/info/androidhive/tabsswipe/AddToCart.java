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

import android.os.AsyncTask;
import android.util.Log;

public class AddToCart {
	int cust_id, item_id, quant;
	
	public void addItem(int cust, int item, int q){
		cust_id = cust;
		item_id = item;
		quant = q;
		Log.d("input", String.valueOf(cust_id)+" "+String.valueOf(item_id)+" "+String.valueOf(quant));
		CheckAndAdd task = new CheckAndAdd();
		task.execute();
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
