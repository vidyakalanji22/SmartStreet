package msd.com.smartstreet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static double LON_DEG_PER_KM = 0.012682308180089;
    final static double LAT_DEG_PER_KM = 0.009009009009009;
    final static double[] SEARCH_RANGES = {10, 50, 800, -1}; //city, region, state, everywhere
    //Declaring the required variables
    private static final int DIALOG_REQUEST = 9001;
    private GoogleMap mMap;
    private GoogleApiClient mLocationClient;
    private Marker marker;
    LatLng latLng;
    String url = "";
    String KEY = "AIzaSyAt3bLiEttEamUajWgvFnP7LQdzxFMIxwc";
    Button mHome;

    /**
     * Calculating the Latitude values
     *
     * @param lat
     * @param dx
     * @return latitude
     */

    private static double translateLat(double lat, double dx) {
        if (lat > 0)
            return (lat + dx * LAT_DEG_PER_KM);
        else
            return (lat - dx * LAT_DEG_PER_KM);
    }

    /**
     * Calculating the Longitude values
     *
     * @param lon
     * @param dy
     * @return longitude
     */
    private static double translateLon(double lon, double dy) {
        if (lon > 0)
            return (lon + dy * LON_DEG_PER_KM);
        else
            return (lon - dy * LON_DEG_PER_KM);

    }

    /**
     * Called on creation of the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mHome = (Button) findViewById(R.id.homeButton);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Getting the location client for getting the current location
        mLocationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mLocationClient.connect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//Pointing the current location
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }
    }

    /**
     * This method is called once the search button is clicked in the map view activity
     *
     * @param view
     */
    public void onSearch(View view) {

        List<Address> addresses = null;

        //Clear the previous markers
        mMap.clear();

        //checking if the map is initiated and all the services are started for using the map
        if (servicesOK() && initMap()) {
            try {
                Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
                if (currentLocation == null) {
                    Toast.makeText(this, "Couldn't connect", Toast.LENGTH_SHORT).show();
                } else {
                    double lat = currentLocation.getLatitude();
                    double lon = currentLocation.getLongitude();
                     latLng = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("You are here!!"));
                    CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
                    mMap.moveCamera(update);
                    Geocoder geocoder = new Geocoder(this);

                    EditText enteredLocation = (EditText) findViewById(R.id.locationEntered);
                    String searchLocation = enteredLocation.getText().toString();

                    int i = 0;
                    try {
                        //loop through SEARCH_RANGES until addresses are returned
                        do {
                            //if range is -1, call  getFromLocationName() without bounding box
                            if (SEARCH_RANGES[i] != -1) {

                                //calculate bounding box
                                double lowerLeftLat = translateLat(lat, -SEARCH_RANGES[i]);
                                double lowerLeftLong = translateLon(lon, SEARCH_RANGES[i]);
                                double upperRightLat = translateLat(lat, SEARCH_RANGES[i]);
                                double upperRightLong = translateLon(lon, -SEARCH_RANGES[i]);

                                addresses = geocoder.getFromLocationName(searchLocation, 20, lowerLeftLat, lowerLeftLong, upperRightLat, upperRightLong);

                            } else {
                                //trying unbounded call with 20 result
                                addresses = geocoder.getFromLocationName(searchLocation, 20);
                            }
                            i++;

                        }
                        while ((addresses == null || addresses.size() == 0) && i < SEARCH_RANGES.length);
                    } catch (IOException e) {
                        Log.i(this.getClass().getSimpleName(), "Gecoder lookup failed! " + e.getMessage());
                    }

                    //Adding the address to a string buffer for displaying on the map
                    StringBuffer addressBuf = null;
                    for (Address address : addresses) {
                        addressBuf = new StringBuffer();

                        if (address.getAddressLine(0) != null)
                            addressBuf.append(address.getAddressLine(0));
                        addressBuf.append(" ");
                        if (address.getAddressLine(1) != null)
                            addressBuf.append(address.getAddressLine(1));
                        addressBuf.append(" ");
                        if (address.getAddressLine(2) != null)
                            addressBuf.append(address.getAddressLine(2));
                        addressBuf.append(" ");
                        if (address.getPhone() != null) addressBuf.append(address.getPhone());

                        final LatLng latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
                        marker = mMap.addMarker(new MarkerOptions().position(latLng1).title(addressBuf.toString())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                        );

                        //Setting custom info window for the map
                        if (mMap != null) {
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    View v = getLayoutInflater().inflate(R.layout.info_window, null);
                                    TextView tvLocality = (TextView) v.findViewById(R.id.tvLocality);

                                    LatLng latLngV = marker.getPosition();
                                    tvLocality.setText(marker.getTitle());
                                    String output = "json";
                                     url ="https://maps.googleapis.com/maps/api/directions/" +output+"?origin=";
                                    url += latLng.latitude + "," +latLng.longitude;
                                    url += "&destination=" +latLngV.latitude + "," +latLngV.longitude;
                                    url += "&mode=driving";
                                    url+= KEY;
                                    ConnectivityManager connMgr = (ConnectivityManager)
                                            getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo nInfo = connMgr.getActiveNetworkInfo();
                                    if(nInfo !=null && nInfo.isConnected()){
                                        new DownloadWebpageTask().execute(url);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Direction error", Toast.LENGTH_SHORT).show();
                                    }
                                    return v;
                                }
                            });


                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    try {
                                        downloadUrl(url);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    }


                    mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                }

            } catch (SecurityException e) {

            }
        }
    }

    /**
     * Checking if all the services are ok
     * @return true if service is fine else false
     */
    public boolean servicesOK() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this, DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.error_connect_to_services), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Initiating the map
     *
     * @return true if the map is initiated else false
     */
    private boolean initMap() {
        if (mMap == null) {
            final SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();

        }
        return (mMap != null);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{

        String response = "";
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();
            JSONArray dataArray =  new JSONObject(data).getJSONArray("routes").getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONArray("steps");

            for(int i=0; i<dataArray.length();i++){
                response+= "<hr COLOR=\"yellow\">";
                response+= "<h5>"+dataArray.getJSONObject(i).getString("html_instructions") + "<h5><br>";
                response+= dataArray.getJSONObject(i).getJSONObject("distance").getString("text") + " ";
                response+= "("+dataArray.getJSONObject(i).getJSONObject("duration").getString("text") + ")";
            }


        }catch(Exception e){
            Log.d(" ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return response;
    }


    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);

        }
    }
}
