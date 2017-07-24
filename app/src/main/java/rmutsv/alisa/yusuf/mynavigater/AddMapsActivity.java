package rmutsv.alisa.yusuf.mynavigater;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private double latADouble = 7.087465, lngADouble = 100.245960;
    private boolean statusABoolean = false, locationABoolean = false;
    private double aDouble;
    private double distanceADouble = 0;
    private String tag = "21julyV1", tag2 = "22JulyV1";
    private ArrayList<String> latStringArrayList;
    private ArrayList<String> lngStringArrayList;
    private String nameMapString, dateString, distanceString, latString, lngString;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maps);

        // Setup Constant
        setupConstant();

        // Create Maps Fragment
        createMapsFragment();

        // Start Controller
        startController();

        // Stop Controller
        stopController();

        //Refresh Controller
        refreshController();

        //Get Current Time
        getCurrentTime();

        //Save Controller
        saveController();

        //Show Name Map
        showNameMap();

        //Back Controller
        backController();


    }   // Main Method

    private void backController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showNameMap() {
        try {

            String strNameMap = getIntent().getStringExtra("NameMap");
            Log.d("24JulyV1", "strNameMap ==> " + strNameMap);
            EditText editText = (EditText) findViewById(R.id.edtName);
            editText.setText(strNameMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvSave);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText = (EditText) findViewById(R.id.edtName);
                nameMapString = editText.getText().toString().trim();

                //Find Distanct String
                distanceString = Double.toString(distanceADouble);

                //Find latString, lngString
                latString = mySubString(latStringArrayList);
                lngString = mySubString(lngStringArrayList);

                //Show Log
                String tag = "23JulyV2";
                Log.d(tag, "Name ==> " + nameMapString);
                Log.d(tag, "Date ==> " + dateString);
                Log.d(tag, "Distance ==> " + distanceString);
                Log.d(tag, "latString ==> " + latString);
                Log.d(tag, "lngString ==> " + lngString);


                if (nameMapString.length() == 0) {
                    Toast.makeText(AddMapsActivity.this,
                            "Please Fill Name", Toast.LENGTH_SHORT).show();
                } else {

                    MyManage myManage = new MyManage(AddMapsActivity.this);
                    myManage.addValuToSQLite(nameMapString, dateString, distanceString,
                            latString, lngString);

                    Intent intent = new Intent(AddMapsActivity.this, MainActivity.class);
                    setResult(100, intent);
                    finish();

                }



            }
        });
    }

    private String mySubString(ArrayList<String> stringArrayList) {

        String result = null;

        result = stringArrayList.toString();
        int i = result.length() - 1;
        result = result.substring(1, i);

        return result;
    }

    private void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateString = dateFormat.format(calendar.getTime());
        TextView textView = (TextView) findViewById(R.id.textDate);
        textView.setText(dateString);

    }

    private void refreshController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvRefresh);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("23JulyV1", "Click Refresh");

                //Refresh Location
                refreshLocation();

                try {
                    createCenterMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    //นี่คือ เมทอด ที่หาระยะ ระหว่างจุด
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;


        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }




    private void stopController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvStop);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusABoolean = false;
                calculateAllPoint();

                Log.d("23JulyV1", "ArrayListString ==> " + latStringArrayList.toString());

            }
        });
    }

    private void calculateAllPoint() {

        String tag = "22JulyV2";


        try {

            String[] latStrings = new String[latStringArrayList.size()];
            String[] lngStrings = new String[lngStringArrayList.size()];

            latStrings = latStringArrayList.toArray(new String[0]);
            lngStrings = lngStringArrayList.toArray(new String[0]);


            for (int i=0; i<latStrings.length - 1; i+=1) {


                distanceADouble = distanceADouble + distance(Double.parseDouble(latStrings[i]),
                        Double.parseDouble(lngStrings[i]),
                        Double.parseDouble(latStrings[i+1]),
                        Double.parseDouble(lngStrings[i+1]));

                Log.d(tag, "Distance in For ==> " + distanceADouble);

            }   // for

            Log.d(tag, "Distance ==> " + distanceADouble);
            TextView textView = (TextView) findViewById(R.id.txtDistance);
            textView.setText(Double.toString(distanceADouble) + " m.");


        } catch (Exception e) {
            Log.d(tag, "e ==> " + e.toString());
        }

    }   // Calculate

    private void startController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvStart);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusABoolean = true;
                createMarker();

                myLoop();
            }
        });
    }

    private void myLoop() {

        if (statusABoolean) {

            //ToDo
            refreshLocation();

            //Add Array
            latStringArrayList.add(Double.toString(latADouble));
            lngStringArrayList.add(Double.toString(lngADouble));
            Log.d(tag, "latStringArray.size ==> " + latStringArrayList.size());

            //Delay
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    locationABoolean = false;
                    myLoop();
                }
            }, 1000);

        }   // if
    }   // myLoop

    @Override
    protected void onResume() {
        super.onResume();

        //Refresh Location
        refreshLocation();

        try {
            createCenterMap();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void refreshLocation() {
        locationManager.removeUpdates(locationListener);

        // For NetWork
        Location netWork = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (netWork != null) {
            latADouble = netWork.getLatitude();
            lngADouble = netWork.getLongitude();
        }

        // For GPS
        Location gps = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gps != null) {
            latADouble = gps.getLatitude();
            lngADouble = gps.getLongitude();
        }

        // Show Log
        showLog();
    }

    private void showLog() {

        Log.d(tag, "Lat ==>" + latADouble);
        Log.d(tag, "Lnt ==>"+lngADouble);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
        }

        return location;
    }


    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (latADouble == location.getLatitude()) {
                Log.d(tag, "Stable");
            } else {
                Log.d(tag, "Moved");
                if (statusABoolean) {
                    createMarker();
                }
            }


            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();
            Log.d(tag, "Location Change");

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void createMarker() {

        try {

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(latADouble, lngADouble))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
            mMap.addMarker(markerOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // createMarker

    private void setupConstant() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        latStringArrayList = new ArrayList<String>();
        lngStringArrayList = new ArrayList<String>();

    }

    private void createMapsFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create Center Map
        createCenterMap();


    }   // onMapReady

    private void createCenterMap() {
        LatLng latLng = new LatLng(latADouble, lngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}    // Main Class