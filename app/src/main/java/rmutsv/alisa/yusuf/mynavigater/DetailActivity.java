package rmutsv.alisa.yusuf.mynavigater;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String idString, nameString, dateString, distanceString, latString, lngString;
    private TextView nameTextView, dateTextView, distanceTextView;
    private String[] latStrings, lngStrings;
    private String tag = "24JulyV1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get Value From Intent
        getValueFromIntent();

        //Show Text
        showText();

        //Create Array
        createArray();

        //Create Maps Fragment
        createMapsFragment();

        //Back Controller
        backController();

        //Delete Controller
        deleteController();

        //Edit Controller
        editController();


    }   // Main Method

    private void editController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvEdit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSQLite(idString, false);
            }
        });
    }

    private void deleteController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvDelete);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(tag, "Delete ID ==> " + idString);
                deleteSQLite(idString, true);

            }
        });
    }

    private void deleteSQLite(String idString, boolean bolStatus) {

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.databaseName,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete("navigateTABLE", "id" + "=" + idString, null);

        if (bolStatus) {
            //Delete Only
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            setResult(200, intent);
            finish();
        } else {
            //Delete and Add New Map
            Intent intent = new Intent(DetailActivity.this, AddMapsActivity.class);
            intent.putExtra("NameMap", nameString);
            startActivityForResult(intent, 100);
            finish();
        }

    }

    private void backController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createArray() {

        latStrings = latString.split(",");
        lngStrings = lngString.split(",");

        Log.d("23JulyV4", "lat.length ==> " + latStrings.length);
        Log.d("23JulyV4", "lng.length ==> " + lngStrings.length);


    }

    private void showText() {
        nameTextView = (TextView) findViewById(R.id.txtShowName);
        dateTextView = (TextView) findViewById(R.id.textDate);
        distanceTextView = (TextView) findViewById(R.id.txtDistance);

        nameTextView.setText(nameString);
        dateTextView.setText(dateString);
        distanceTextView.setText(String.format("%.2f", Double.parseDouble(distanceString)) + " m.");

    }

    private void getValueFromIntent() {
        idString = getIntent().getStringExtra("id");
        nameString = getIntent().getStringExtra("Name");
        dateString = getIntent().getStringExtra("Date");
        distanceString = getIntent().getStringExtra("Distance");
        latString = getIntent().getStringExtra("Lat");
        lngString = getIntent().getStringExtra("Lng");
    }

    private void createMapsFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Create Map
        LatLng latLng = new LatLng(Double.parseDouble(latStrings[0]),
                Double.parseDouble(lngStrings[0]));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        //Create Marker
        MarkerOptions startMarkerOptions = new MarkerOptions();
        startMarkerOptions.position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
        mMap.addMarker(startMarkerOptions);


        LatLng endLatLng1 = new LatLng(Double.parseDouble(latStrings[latStrings.length - 1]),
                Double.parseDouble(lngStrings[lngStrings.length - 1]));

        MarkerOptions endMarkerOptions = new MarkerOptions();
        endMarkerOptions.position(endLatLng1)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
        mMap.addMarker(endMarkerOptions);


        //Create Polyline

        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < latStrings.length - 1; i += 1) {

            polylineOptions.add(new LatLng(Double.parseDouble(latStrings[i]),
                    Double.parseDouble(lngStrings[i])));

        }   // for

        polylineOptions.width(5).color(Color.RED);
        mMap.addPolyline(polylineOptions);


    }   // onMapReady

}   // Main Class
