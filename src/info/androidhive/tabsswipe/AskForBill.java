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

public class AskForBill {
	
	int id_cust, id_pair;
	String rsp = null;
	public String ask(int cust_id, int pair_id){
		
		id_cust = cust_id;
		id_pair = pair_id;
		AskBill task = new AskBill();
		try {
			task.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp;
	}
	
	private class AskBill extends AsyncTask<String, Void, Boolean>{

		String response;
		JSONArray array;
		JSONObject obj;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				String URL = "http://192.168.144.1/order/bill.php?"+"cust_id="+String.valueOf(id_cust);
				HttpGet get = new HttpGet(URL);
				Log.d("URL", URL);
		        HttpResponse responseGet = client.execute(get);  
		        HttpEntity resEntity = responseGet.getEntity();
		        if (resEntity != null) 
		        {  
		                 	response = EntityUtils.toString(resEntity);
		                    Log.d("response-ask-for-bill", response);
		        }
				array = new JSONArray(response);
				int arrlen = array.length();
				Log.d("Array Length", Integer.toString(arrlen));

				for(int i=0;i<arrlen;i++)
				{	
					obj = array.getJSONObject(i);
					response = obj.getString("msg");
				}
				rsp = response;
				if(response.equals("Sending bill request")){
					NotifyGCM taskGCM = new NotifyGCM();
					taskGCM.notify(1, "New Billing Request", "You have a new bill approval", Integer.valueOf(id_pair));
				}
				
			}catch(Exception e){
				
			}
			return null;
		}
		
	}

}
