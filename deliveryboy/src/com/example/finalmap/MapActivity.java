package com.example.finalmap;

import java.util.List;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import android.location.Address;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//import com.last.MyMapActivity;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

public class MapActivity extends Activity {
	 
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	static LatLng dest  =new LatLng(53.558, 9.927), CurLoc;
	private GoogleMap map;
	List<Address> address;
	Geocoder coder;
	Marker dest_marker = null;
	Marker current_marker = null;
	ParseObject delivery;  
	int id; 
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
		id = settings.getInt("id", -1);
        Log.d("id", "Saved ID: " + id);
        
        /*
        if(id==-1)
        {
        	settings.edit().putBoolean("is_registered", false);
        	settings.edit().commit();
        	Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
        }
         */
		
		
		Parse.initialize(this, "0rNEglAgEcFpXGLm8rqPsX73djfXMPxW4kCDl1sc", "Rwbd9wbYXtXOnfTdo8LQkhZKAEbRjtKSc032OPdh"); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);   
	    LocationListener mlocListener = new MyLocationListener();
	    mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
	   	coder = new Geocoder(this);
	   	
	   	try {
			 	// Provide the address of delivery point here
			    address = coder.getFromLocationName("IIIT,  Gachibowli,  Hyderabad", 5);

			    Address location = address.get(0);
			    location.getLatitude();
			    location.getLongitude();
			    
			    dest = new LatLng(location.getLatitude(), location.getLongitude());
			    
			    dest_marker = map.addMarker(new MarkerOptions().position(dest)
				        .title("Destination"));
			    
			 // Move the camera instantly to hamburg with a zoom of 15.
			    map.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 15));

			    // Zoom in, animating the camera.
			    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			 
		  }
			
		  catch (Exception e) {
			// TODO: handle exception
		}
		    
	}

	public class MyLocationListener implements LocationListener
    {

      @Override
      public void onLocationChanged(final Location loc)
      {
        CurLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
        
        ParseQuery query = new ParseQuery("Delivery");
    	query.whereEqualTo("delivery_boy_id", 0);
    	query.findInBackground(new FindCallback() {
    	    public void done(List<ParseObject> latlongList, ParseException e) {
    	        if (e == null) {
    	            Log.d("score", "Retrieved " + latlongList.size() + " scores");
    	            if(latlongList.size() == 0)
    	            {
    	               	delivery = new ParseObject("Delivery");
    	            	delivery.put("delivery_boy_id", 0);
    	             	delivery.put("lat", loc.getLatitude());
    	            	delivery.put("long", loc.getLongitude());
    	            	delivery.saveInBackground();

    	            }
    	            else
    	            {
    	            	latlongList.get(0).put("lat", loc.getLatitude());
    	            	latlongList.get(0).put("long", loc.getLongitude());
    	            	latlongList.get(0).saveInBackground();
    	            }
    	        } else {
    	            Log.d("score", "Error: " + e.getMessage());
    	        }
    	    }

			
    	});
        
        
       
        if(current_marker != null)
        	current_marker.remove();
        current_marker = map.addMarker(new MarkerOptions().position(CurLoc).title("Current"));
      }

      @Override
      public void onProviderDisabled(String provider)
      {
        Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
      }

      @Override
      public void onProviderEnabled(String provider)
      {
        Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras)
      {

      }


    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
