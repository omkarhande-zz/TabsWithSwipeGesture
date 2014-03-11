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
import android.os.AsyncTask;
import android.os.Bundle;
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
		b.setOnClickListener(new View.OnClickListener() {
		Intent i;	
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
				Toast.makeText(LoginActivity.this, "HEy", Toast.LENGTH_SHORT).show();
				startActivity(i);
			}
		});
	}
	private class GetUserDetails extends AsyncTask<String,Void,String>{
		

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String tmp_fname = null;
			try
			{
				int k=10;
				
//				Toast.makeText(LoginActivity.this, "Hello", Toast.LENGTH_SHORT).show();
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				String URL = folder+"login.php?type=user&email=omkarsayajihande@gmail.com&pass=welcome123";
				Log.d("faculty list","login folder="+URL);
				
				HttpGet get = new HttpGet("http://192.168.144.1/order/login.php?type=user&email=omkarsayajihande@gmail.com&pass=welcome123");
				while(k>0){
					Log.d("Check", "Check "+k);
					k--;
				}
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
//					Toast.makeText(LoginActivity.this, tmp_fname, Toast.LENGTH_LONG).show();
					
				}
				return tmp_fname;
			}
			catch(Exception e)
			{
				Log.d("get faculty list problem",e.getMessage());
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("Name",result);
			Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
		}
		
	}
}
