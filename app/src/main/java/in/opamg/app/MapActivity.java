package in.opamg.app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;
import in.opamg.app.Models.Project;

public class MapActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private Location mLastLocation;
    private String provider;
    double latitude, longitude;
    LatLng latLng;
    int locationCount = 0;
    Boolean checkLocation;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    String projectId;
    MarkerOptions markerOptions;
    DatabaseHandler db;
    Boolean markerCheck = true;
    Marker addedMarker;
    Double getLatitude, getLongitude;
    ImageView syncBtn, backBtn;
    ProgressDialog pd;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        db = new DatabaseHandler(this);

        projectId = getIntent().getStringExtra("PROJECT_ID");
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        syncBtn = (ImageView) findViewById(R.id.syncBtn);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
            dialog.setMessage(MapActivity.this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(MapActivity.this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MapActivity.this.startActivity(myIntent);
                    //get gps
                }
            });

            dialog.show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            // onLocationChanged(location);
        } else {
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        int s = synWithServer();
                        if( s == 1 )
                            Toast.makeText(MapActivity.this, "Updated to server successfully!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MapActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    JSONArray all = db.getAllMeasurementByProjectId(Variables.PROJECT_ID);
                    if( all.length() > 0 ){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                        builder.setMessage("Are you sure? If yes, all data delete from here. Data will store in Server.").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }else {
                        Toast.makeText(MapActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MapActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                MapActivity.this.finish();
            }
        });
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);
//        latituteField.setText(String.valueOf(latitude));
//        longitudeField.setText(String.valueOf(longitude));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        checkLocation = true;
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        checkLocation = false;
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        getLatitude = marker.getPosition().latitude;
        getLongitude = marker.getPosition().longitude;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },      LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            LatLng myPosition;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            myPosition = new LatLng(latitude, longitude);


            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
            mMap.animateCamera(yourLocation);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if( markerCheck ){
                    MarkerOptions markerOptions = new MarkerOptions();
                    float zoomLevel = (float) 21.00;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, zoomLevel));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                    markerOptions.position(point);
                    markerOptions.title("move");
                    markerOptions.draggable(true);
                    markerOptions.anchor(0.5f, 0.5f);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin));
                    mMap.addMarker(markerOptions);
                    getLatitude = point.latitude;
                    getLongitude = point.longitude;
                    markerCheck = false;
                }
               // mMap.clear();


            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if( marker.getTitle().toString().equals("move")){
                    Intent intent = new Intent(MapActivity.this, AddMeasurement.class);
                    intent.putExtra("Latitude", ""+getLatitude );
                    intent.putExtra("Longitude", ""+getLongitude );
                    //intent.putExtra("ProjectId", projectId);
                    startActivity(intent);
                }

                return false;
            }
        });

        JSONArray LatLng = db.getLatLng( Variables.PROJECT_ID );
        Log.e("LatLng", String.valueOf(LatLng));
        for(int i = 0; i < LatLng.length(); i++) {
            Double getLat = null, getLong = null;
            String layercode = "";

            try {
                JSONObject LatLngJson = LatLng.getJSONObject(i);
                if( !LatLngJson.getString("latitude").equalsIgnoreCase("") && !LatLngJson.getString("longitude").equalsIgnoreCase("")){
                    getLat = Double.parseDouble(LatLngJson.getString("latitude"));
                    getLong = Double.parseDouble(LatLngJson.getString("longitude"));
                    layercode = LatLngJson.getString("layer_code");
                    Log.e("getLat", String.valueOf(getLat));
                    Log.e("getLong", String.valueOf(getLong));
                    createMarker(getLat, getLong, i, layercode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    protected Marker createMarker(double latitude, double longitude, int i, String layercodes) {
        Marker added = null;
        if (layercodes.equalsIgnoreCase("OCC")){
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_occ))
                    .title(String.valueOf(i + 1)));
        }else if (layercodes.equalsIgnoreCase("BS")){
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bs))
                    .title(String.valueOf(i + 1)));
        }else if (layercodes.equalsIgnoreCase("FS")){
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fs))
                    .title(String.valueOf(i + 1)));
        }else if (layercodes.equalsIgnoreCase("BM")){
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bm))
                    .title(String.valueOf(i + 1)));
        }else if (layercodes.equalsIgnoreCase("TBM")){
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tbm))
                    .title(String.valueOf(i + 1)));
        }else if (layercodes.equalsIgnoreCase("CH")){
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ch))
                    .title(String.valueOf(i + 1)));
        }else {
            added = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_re))
                    .title(String.valueOf(i + 1)));
        }
        
        added.showInfoWindow();
        return added;
    }

    public int synWithServer(){
        final int[] returnValue = {0};
        JSONArray all = db.getAllMeasurementByProjectId(Variables.PROJECT_ID);

        Log.e("All", String.valueOf(all));

        RequestQueue queue = Volley.newRequestQueue(MapActivity.this);

        JSONObject param = new JSONObject();

        try {
            param.put("data", all);
            pd = new ProgressDialog(MapActivity.this);
            pd.setMessage("Storing Data to server...");
            pd.setCancelable(false);
            pd.show();
        } catch (JSONException ex) {

        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, Variables.API_URL + Variables.MEASUREMENT, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObj) {
                        pd.dismiss();

                        if (jsonObj != null) {

                            try {
                                String code = jsonObj.getString("code");

                                if (code.equalsIgnoreCase("200")) {
                                    returnValue[0] = 1;
                                    db.deleteMeasurement(Variables.PROJECT_ID);
                                }
                                else
                                {
                                    Toast.makeText(MapActivity.this, "somthing", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(MapActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
        return returnValue[0];
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
