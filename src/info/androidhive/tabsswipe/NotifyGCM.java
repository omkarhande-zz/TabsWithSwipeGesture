package info.androidhive.tabsswipe;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

public class NotifyGCM {
	int msg_type, user_id;
	String msg_title, msg_content;
	
	public void notify(int type, String msg, String title,int id){
		msg_type = type;
		user_id = id;
		msg_title = title;
		msg_content = msg;
		SendNotification task = new SendNotification();
		task.execute();
	}
	
	private class SendNotification extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpGet get = new HttpGet("http://192.168.144.1/order/notify.php?"+"user_id="+user_id+"&msg="+msg_content+"&title="+msg_title+"&type="+msg_type);
				
		        HttpResponse responseGet = client.execute(get);
			}catch(Exception e){
				
			}
			return null;
		}
		
	}
}
