package info.androidhive.tabsswipe;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

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
//				HttpGet get = new HttpGet("http://192.168.144.1/order/notify.php?"+"user_id="+String.valueOf(user_id)+"&msg="+msg_content+"&title="+msg_title+"&type="+String.valueOf(msg_type));
				HttpPost post = new HttpPost("http://192.168.144.1/order/notify.php");
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("msg", msg_content));
				pairs.add(new BasicNameValuePair("title", msg_title));
				pairs.add(new BasicNameValuePair("type", String.valueOf(msg_type)));
				pairs.add(new BasicNameValuePair("user_id", String.valueOf(user_id)));
		        
		        post.setEntity(new UrlEncodedFormEntity(pairs));
		        HttpResponse responseGet = client.execute(post);
			}catch(Exception e){
				
			}
			return null;
		}
		
	}
}
