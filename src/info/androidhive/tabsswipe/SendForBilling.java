package info.androidhive.tabsswipe;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class SendForBilling {

	int order, pair;
	public void send(int order_id, int pair_id){
		String response = null;
		order = order_id;
		pair = pair_id;
		
		BillOrder task = new BillOrder();
		task.execute();
		
	}
	
	private class BillOrder extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/bill_order.php?"+"order_id="+order);
				
		        HttpResponse responseGet = client.execute(get);  
			}catch(Exception e){
				Log.d("ERROR", e.getMessage());
			}
			return null;
		}
		
	}
}
