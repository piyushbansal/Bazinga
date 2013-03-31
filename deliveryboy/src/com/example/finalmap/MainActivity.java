package com.example.finalmap;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	ParseObject boy;
	String Name, Phone;
	int id;
	EditText name, phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "0rNEglAgEcFpXGLm8rqPsX73djfXMPxW4kCDl1sc", "Rwbd9wbYXtXOnfTdo8LQkhZKAEbRjtKSc032OPdh"); 

		setContentView(R.layout.activity_register);
		
		SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
	    boolean is_registered = settings.getBoolean("is_registered", false);
	    
	    if(is_registered==true)
	    {
	    	Intent i = new Intent(getApplicationContext(), MapActivity.class);
			startActivity(i);
	    }
		
		
		name = (EditText) findViewById(R.id.Name);
		phone = (EditText) findViewById(R.id.phone);
		
		
		
		final Button button = (Button) findViewById(R.id.regButton);
		button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
                final SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("is_registered", true);
                
            	Name = name.getText().toString();
        		Phone = phone.getText().toString();
        		
                // Perform action on click
            	ParseQuery query = new ParseQuery("NoOfDeliveryBoys");
             	query.whereNotEqualTo("count", -1);
             	
             	query.findInBackground(new FindCallback() {
            	    public void done(List<ParseObject> counts, ParseException e) {
            	        if (e == null) {
            	            if(counts.size() > 0)
            	            {
            	             	id = (Integer) counts.get(0).get("count") ;
            	            	counts.get(0).put("count", id+1);
            	            	counts.get(0).saveInBackground();
            	            	
            	            	Log.d("Name", "Name " + Name );
            		            Log.d("Phone", "Phone " + Phone );

            	            	boy = new ParseObject("DeliveryBoys");
            	            	boy.put("delivery_boy_id", id);
            	            	boy.put("Name", Name);
            	            	boy.put("Phone", Phone);
            	            	boy.saveEventually();
            	            	editor.putInt("id", id);
            	            	editor.commit();
            	            	Intent i = new Intent(getApplicationContext(), MapActivity.class);
            	    			startActivity(i);
            	    			finish();
            	            	
            	            	
            	            }
            	            Log.d("ID", "Retrieved ID " + id );
            	        } else {
            	            Log.d("ID", "Error: " + e.getMessage());
            	        }
            	    }        			
            	});
	            
             
                

            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register2, menu);
		return true;
	}

}
