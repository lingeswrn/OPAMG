package in.opamg.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import global.Variables;
import in.opamg.app.DatabaseConnection.DatabaseHandler;

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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        db = new DatabaseHandler(this);

        projectId = getIntent().getStringExtra("PROJECT_ID");
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
        Location locationss = locationManager.getLastKnownLocation(bestProvider);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if( markerCheck ){
                    MarkerOptions markerOptions = new MarkerOptions();
                    float zoomLevel = (float) 16.00;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, zoomLevel));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                    markerOptions.position(point);
                    markerOptions.title("move");
                    markerOptions.draggable(true);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
                    mMap.addMarker(markerOptions);
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
            try {
                JSONObject LatLngJson = LatLng.getJSONObject(i);
                getLat = Double.parseDouble(LatLngJson.getString("latitude"));
                getLong = Double.parseDouble(LatLngJson.getString("longitude"));
                Log.e("getLat", String.valueOf(getLat));
                Log.e("getLong", String.valueOf(getLong));
                createMarker(getLat, getLong, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    protected Marker createMarker(double latitude, double longitude, int i) {
        Marker added = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(String.valueOf(i + 1)));
        added.showInfoWindow();
        return added;
    }


}
