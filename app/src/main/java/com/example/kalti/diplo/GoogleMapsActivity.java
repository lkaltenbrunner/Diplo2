package com.example.kalti.diplo;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kalti.diplo.Model_Classes.PlaceInformation;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapActivity";
    //Google-map permissions
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //Map Zoom
    private static final float DEFAULT_ZOOM = 15;
    //Constanten
    public static final String GEO_LOCATION = "geoLocation";
    private static final String SELECTED_STYLE = "selected_style";

    //Variablen
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    protected GeoDataClient mGeoDataClient;
    private static  final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168), new LatLng(71,136));
    private int mSelectedStyleId = R.string.style_label_default;
    public String layoutStyle = "Default";
    public MapStyleOptions style;
    private static final int PLACE_PICKER_REQUEST = 1;
    private PlaceInformation mPlace;
    private Marker mMarker;



    //Widget
    private AutoCompleteTextView editTextSearch;
    private ImageView mGps;
    private ImageView mplacePicker;
    private ImageView mInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);


        mInfo = (ImageView) findViewById(R.id.place_info);
        mplacePicker = (ImageView) findViewById(R.id.placePicker);
        editTextSearch = (AutoCompleteTextView) findViewById(R.id.mapSearchText);
        mGps = (ImageView)findViewById(R.id.ic_gps);
        mGps.bringToFront();

        mGps.setClickable(true);
        mGeoDataClient = Places.getGeoDataClient(this, null);

        checkPermission();

    }





    //Event-Handler => durch welche Aktion wird nach einer "Addresse" auf der Karte gesucht => Trigger-Event
    private void initialize(){


        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient ,LAT_LNG_BOUNDS,null);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this,this).build();
        editTextSearch.setAdapter(placeAutocompleteAdapter);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int EditorActionID, KeyEvent keyEvent) {
                if(EditorActionID == EditorInfo.IME_ACTION_SEARCH
                        || EditorActionID == EditorInfo.IME_ACTION_DONE
                        ||keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        ||keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        ){

                    geoLocate();

                }

                return false;

            }

        });

        mGps.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getLocation();
           }
        });
        mplacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(intentBuilder.build(GoogleMapsActivity.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked place info");
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{
                        Log.d(TAG, "onClick: place info: " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
                }
            }
        });

        hideKeyboard();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                //String toastMsg = String.format("Place: %s", place.getName());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                PendingResult<PlaceBuffer> pendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient,place.getId());
                pendingResult.setResultCallback(UpdatePlaceDetailsCallback);


            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//"Addresse" eingeben und dieser auf der Karte anzeigen
    private void geoLocate(){
        Log.d(TAG, "geoLocate()");

        String searchString = editTextSearch.getText().toString();

        Geocoder geocoder = new Geocoder(GoogleMapsActivity.this);
        List<Address> addresses = new ArrayList<>();

        try{
            addresses = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate(): IOException: " + e.getMessage());
        }

        if(addresses.size() > 0){
            Address address = addresses.get(0);
            Toast.makeText(this, addresses.get(0).toString(),Toast.LENGTH_SHORT);
            Log.d(TAG,"geoLocate" + address.toString());


            try{
                moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
            }catch (NullPointerException e){
                Log.e(TAG, "onComplete: NullPointerException: " +e.getMessage() );
            }

        }
    }

//Aktuelle Position auf der Karte finden => beim start der App
    public void getLocation() {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {

                Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        Location clocation = (Location) task.getResult();

                        try{
                            moveCamera(new LatLng(clocation.getLatitude(), clocation.getLongitude()), DEFAULT_ZOOM, "Current Location");
                        }catch (NullPointerException e){
                            Log.e(TAG, "onComplete: NullPointerException: " +e.getMessage() );
                        }


                    }
                });
            }
        } catch (SecurityException e) {

        }

    }

    //Camera "Map-Position" je nach "Event", verändern. (Start der App, Suche nach einer "Addresse")
    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + "; lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("Current Location")){
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(markerOptions);
        }

    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInformation placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new InfoWindowAdapter(GoogleMapsActivity.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideKeyboard();
    }

    //Verstecken des SoftKeyboards nach der eingabe einer Addresse
    private void hideKeyboard(){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
    }

    //User nach Berächtigung abfragen => darf die App auf den Standort des Users zugreifen, etc.?
    private void checkPermission() {
        Log.d(TAG, "checkPermission: checking location Permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called onRequestPermissionsResult");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission denied");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //Init map

                    initMap();
                }
            }
        }
    }

    //Map-Fragment "initiallisieren"
    private void initMap() {
        Log.d(TAG, "initMap: init your map");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(GoogleMapsActivity.this);
    }

    //Erstellen der Map
    @Override
    public void onMapReady(GoogleMap googleMap) {


        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        setSelectedStyle();




        if (mLocationPermissionsGranted) {
            getLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            initialize();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            Intent switchActivityToSettings = new Intent(this, SettingsActivity.class);
            startActivity(switchActivityToSettings);

            //showStylesDialog();
        }
        return true;
    }


//Überprüfen welcher LayoutStyle in den Settings ausgewählt wurde
    private void setSelectedStyle() {
        Bundle bundle = getIntent().getExtras();



        if(bundle != null){

            layoutStyle = bundle.getString("STYLEVALUE");

        MapStyleOptions style;
        switch (layoutStyle) {
            case "Retro":

                //Layout der Map basierend auf ein JSON-File
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro);
                break;
            case "Night":

                //Layout der Map basierend auf ein JSON-File
                style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
                break;
            case "Default":

                style = null;
                break;
            default:
                return;
        }
        mMap.setMapStyle(style);

    }
    }

    private AdapterView.OnItemClickListener AutocompleteClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideKeyboard();

            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeBufferPendingResult.setResultCallback(UpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> UpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "Place query did not complete successfully "+ places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            try{
            mPlace = new PlaceInformation();
            mPlace.setName(place.getName().toString());
            mPlace.setAddress(place.getAddress().toString());
            mPlace.setPhoneNumber(place.getPhoneNumber().toString());
            mPlace.setId(place.getId());
            mPlace.setWebsiteUri(place.getWebsiteUri());
            mPlace.setLatlng(place.getLatLng());
            mPlace.setRating(place.getRating());



                Log.d(TAG, "onResult: UpdatePlaceDetailsCallback: "+ mPlace.toString());

            }catch (NullPointerException e){
                Log.d(TAG, "NullPointerException "+ e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
            places.release();

        }
    };
}
