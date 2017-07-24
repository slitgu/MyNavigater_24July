package rmutsv.alisa.yusuf.mynavigater;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private MyManage myManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create SQLite
        createSQLite();

        // Add Maps Controller
        addMapsController();

        //Create ListView
        createListView();


    } // main method

    @Override
    protected void onStart() {
        super.onStart();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean bolGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!bolGPS) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }


    }

    private void createListView() {
        ListView listView = (ListView) findViewById(R.id.livMap);

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.databaseName,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM navigateTABLE", null);
            cursor.moveToFirst();

            final String[] idStrings = new String[cursor.getCount()];
            final String[] nameStrings = new String[cursor.getCount()];
            final String[] dateStrings = new String[cursor.getCount()];
            final String[] distanceStrings = new String[cursor.getCount()];
            final String[] latStrings = new String[cursor.getCount()];
            final String[] lngStrings = new String[cursor.getCount()];

            for (int i = 0; i < cursor.getCount(); i += 1) {

                idStrings[i] = cursor.getString(0);
                nameStrings[i] = cursor.getString(1);
                dateStrings[i] = cursor.getString(2);
                distanceStrings[i] = cursor.getString(3);
                latStrings[i] = cursor.getString(4);
                lngStrings[i] = cursor.getString(5);

                Log.d("23JulyV3", "Name[" + i + "] ==> " + nameStrings[i]);

                cursor.moveToNext();
            }   // for

            cursor.close();

            MyAdapter myAdapter = new MyAdapter(MainActivity.this,
                    nameStrings, dateStrings, distanceStrings);
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", idStrings[i]);
                    intent.putExtra("Name", nameStrings[i]);
                    intent.putExtra("Date", dateStrings[i]);
                    intent.putExtra("Distance", distanceStrings[i]);
                    intent.putExtra("Lat", latStrings[i]);
                    intent.putExtra("Lng", lngStrings[i]);
                    startActivityForResult(intent, 200);


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // createListView

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ToDo
        Log.d("23JulyV3", "ActivityResult Work");
        createListView();

    }

    private void createSQLite() {
        myManage = new MyManage(MainActivity.this);
    }

    private void addMapsController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvAddMaps);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddMapsActivity.class);
                startActivityForResult(intent, 100);

            }
        });
    }
}  // main class
