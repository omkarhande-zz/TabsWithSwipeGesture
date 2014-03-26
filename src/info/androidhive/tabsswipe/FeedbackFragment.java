package info.androidhive.tabsswipe;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackFragment extends Fragment {
	EditText content;
	Button b;
	int cust_id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());		
		cust_id = Integer.valueOf(prefs.getString("id", ""));

		View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
		content = (EditText) rootView.findViewById(R.id.feedback_test);  
		b = (Button)rootView.findViewById(R.id.send_feedback);
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fb = content.getText().toString();
				if(fb.isEmpty()){
					Toast.makeText(getActivity(), "Feedback is empty", Toast.LENGTH_SHORT).show();
				}else{
					AddFeedback task = new AddFeedback();
					try {
						task.execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(getActivity(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		 
		return rootView;
	}
	
	private class AddFeedback extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try{
				HttpClient client = new DefaultHttpClient();  
//				String folder = getString(R.string.server_addr);
				HttpPost post = new HttpPost("http://192.168.144.1/order/add_feedback.php");
				ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("content", content.getText().toString()));
				pairs.add(new BasicNameValuePair("cust_id", String.valueOf(cust_id)));
		        
		        post.setEntity(new UrlEncodedFormEntity(pairs));
		        client.execute(post);  
		        			
			}catch(Exception e){
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			content.setText("");
		}
		
		
	}
}

