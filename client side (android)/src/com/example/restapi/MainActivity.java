package com.example.restapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
* The MainActivity class that will handle login process..
* @author  Anil Chaubey
* @version 1.0
* @since   2014-05-17 
*/
public class MainActivity extends Activity {
	
	private Button loginButton;
	private EditText userPassword, userName;
	private TextView info;
	private TextView tv;
	private HttpPost httppost;
	private HttpResponse response;
	private HttpClient httpclient;
	private List<NameValuePair> nameValuePairs;
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loginButton = (Button)findViewById(R.id.loginButton);  
		userName = (EditText)findViewById(R.id.userName);
        userPassword= (EditText)findViewById(R.id.userPassword);
        tv = (TextView)findViewById(R.id.tv);
        info = (TextView)findViewById(R.id.info);
        info.setText("Please enter username and password to login.");
        
        loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(MainActivity.this, "", 
                        "Validating user...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	login();					      
					    }
					  }).start();				
			}
		});
	}
	/**
	* The login() method send the username and password to the server via http post request and receives the response in JSON format
	*/
private void login(){
		try{			
			 
			httpclient=new DefaultHttpClient();
			
			httppost= new HttpPost("http://192.168.137.1:80/restapi/index.php"); // make sure the url is correct.		

			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("tag","auth")); 
			nameValuePairs.add(new BasicNameValuePair("username",userName.getText().toString().trim()));  // $username = $_POST['username'];
			nameValuePairs.add(new BasicNameValuePair("password",userPassword.getText().toString().trim())); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response=httpclient.execute(httppost); //Execute HTTP Post Request
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler(); // getting response from php
			final String response = httpclient.execute(httppost, responseHandler);
			
			JSONObject jsonObject = (JSONObject) JSONValue.parse(response); // converting string response to json

            
			runOnUiThread(new Runnable() {
			    public void run() {
			    	tv.setText("Response from server : " + response);
					dialog.dismiss();
			    }
			});
			
			if((Boolean)jsonObject.get("success")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
				    }
				});
				
			}else{
				showAlert();				
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
	public void showAlert(){
		MainActivity.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    	builder.setTitle("Login Failed.");
		    	builder.setMessage("Check username/password.")  
		    	       .setCancelable(false)
		    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	           public void onClick(DialogInterface dialog, int id) {
		    	           }
		    	       });		    	       
		    	AlertDialog alert = builder.create();
		    	alert.show();		    	
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
