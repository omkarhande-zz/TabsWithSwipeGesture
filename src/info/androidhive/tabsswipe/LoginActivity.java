package info.androidhive.tabsswipe;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;



public class LoginActivity extends Activity{

	EditText email, pass;
	Button b;
	RadioButton rb1, rb2;
	String response;
	JSONArray array;
	JSONObject obj;
	int auth;
	Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		b = (Button)findViewById(R.id.loginButton);
		rb1 = (RadioButton)findViewById(R.id.radioUser);
		rb2 = (RadioButton)findViewById(R.id.radioAdmin);
		email = (EditText)findViewById(R.id.email);
		pass = (EditText)findViewById(R.id.pass);
		auth=0;
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(rb1.isChecked()){
					i = new Intent(LoginActivity.this,MainActivity.class);
					
				}else{
					i = new Intent(LoginActivity.this,AdminActivity.class);
				}
				GetUserDetails task= new GetUserDetails();
				task.execute();
//				startActivity(i);
//				Toast.makeText(LoginActivity.this, String.valueOf, Toast.LENGTH_SHORT).show();
//				if(auth == 1){
//					startActivity(i);
//				}else{
//					Toast.makeText(LoginActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
//				}
			}
		});
	}
	private class GetUserDetails extends AsyncTask<String,Void,Boolean>{
		
		String tmp_fname = null;
		String tmp_id = null;
		String tmp_active = null;

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			try
			{
				int k=25;
				
//				Toast.makeText(LoginActivity.this, "Hello", Toast.LENGTH_SHORT).show();
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				String URL = folder+"login.php?type=user&email=omkarsayajihande@gmail.com&pass=welcome123";
				Log.d("faculty list","login folder="+URL);
				
				HttpGet get = new HttpGet("http://192.168.144.1/order/login.php?type=user&email=omkarsayajihande@gmail.com&pass=welcome123");
				
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
					
					tmp_fname = obj.getString("name");
					tmp_id = obj.getString("id");
					tmp_active = obj.getString("active");
					
					if(tmp_active.equals("1")){
						Log.d("Active", tmp_active);
						auth=1;
						return true;
					}
//					Toast.makeText(LoginActivity.this, tmp_fname, Toast.LENGTH_LONG).show();
					
				}
				return false;
			}
			catch(Exception e)
			{
				Log.d("get faculty list problem",e.getMessage());
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			int k=25;
//			while(k>0){
//				Log.d("Check", "Check "+k);
//				k--;
//			}
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    SharedPreferences.Editor editor = prefs.edit();
			if(result){
				Toast.makeText(LoginActivity.this, "Authenticated", Toast.LENGTH_LONG).show();
				Log.d("Authenticated", tmp_fname+tmp_id+tmp_active);
			    editor.putString("name",tmp_fname);
			    editor.putString("id",tmp_id);
//			    editor.putString("active",tmp_active);
			    editor.commit();
			    startActivity(i);
			}else{
				Toast.makeText(LoginActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
}
