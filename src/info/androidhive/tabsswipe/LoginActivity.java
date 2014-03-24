package info.androidhive.tabsswipe;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class LoginActivity extends Activity{

	EditText email, pass;
	Button b;
	RadioButton rb1, rb2;
	String response;
	JSONArray array;
	JSONObject obj;
	int auth;
	Intent i;
	GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	 /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "658375083664";
    String SCRIPT_URL = null;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Demo";
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
		context = getApplicationContext();

//         Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
            b.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if(rb1.isChecked()){
    					i = new Intent(LoginActivity.this,MainActivity.class);
    					SCRIPT_URL = "http://192.168.144.1/order/login.php?type=user&email=omkarsayajihande@gmail.com&pass=welcome123&gcm_id="+regid;
    				}else{
    					i = new Intent(LoginActivity.this,WaiterHome.class);
    					SCRIPT_URL = "http://192.168.144.1/order/login.php?type=waiter&email=food.delivery.bits@gmail.com&pass=123&gcm_id="+regid;
    				}
    				GetUserDetails task= new GetUserDetails();
    				task.execute();
    			}
    		});
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
		
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
				
                   Thread.sleep(3000);
//               } catch (InterruptedException e) {
//               }
				int k=25;
				
//				Toast.makeText(LoginActivity.this, "Hello", Toast.LENGTH_SHORT).show();
				HttpClient client = new DefaultHttpClient();  
				String folder = getString(R.string.server_addr);
				String URL = folder+"login.php?type=user&email=omkarsayajihande@gmail.com&pass=welcome123";
				Log.d("faculty list","login folder="+URL);
				
				HttpGet get = new HttpGet(SCRIPT_URL);
				
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
			    editor.commit();
			    startActivity(i);
			}else{
				Toast.makeText(LoginActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
	
	
	
	private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
	
	
	/**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
      // Your implementation here.
    }
	
	private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    HttpClient client = new DefaultHttpClient();  
//    				String folder = getString(R.string.server_addr);
    				HttpGet get = new HttpGet("http://192.168.144.1/order/reg_gcm.php?"+"reg_id="+regid);
//    				onClick(b);
    		        HttpResponse responseGet = client.execute(get);  
//                    Bundle data = new Bundle();
//                    data.putString("my_message", "Hello World");
//                    data.putString("my_action",
//                            "info.androidhive.tabsswipe.ECHO_NOW");
//                    String id = Integer.toString(msgId.incrementAndGet());
//                    gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//                    msg = "Sent message";
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                mDisplay.append(msg + "\n");
            	Log.d("MSG", msg);
            }
        }.execute(null, null, null);
    }
	private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
