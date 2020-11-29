package com.example.phonebookwithmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ListDataActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Person> personList;
    private DatabaseHelper databaseHelper;
    public static final int REQUEST_ACTIVITY_RECOGNITION = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACTIVITY_RECOGNITION);
        }

        initWidgets();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission berhasil dilakukan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission gagal dilakukan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initWidgets() {
        findViewById(R.id.buttonKeluar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        databaseHelper = DatabaseHelper.getInstance(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Person person = new Person();
            person.setNo_hp((String) extras.getString("no_hp"));
            person.setNama((String) extras.getString("nama"));
            person.setAlamat((String) extras.getString("alamat"));
            if (!person.getNama().equals("")) {
                person.setSoundex(Soundex.getCode(extras.getString("nama")));
            } else {
                person.setSoundex("");
            }

            personList = databaseHelper.cariData(person);
            Log.d("INTENT_TAG", "Cari Data: " + personList.size());
        } else {
            personList = databaseHelper.ambilSemuaData();
            Log.d("INTENT_TAG", "Semua Data: " + personList.size());
        }

        // Custom array adapter
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(this, this, R.layout.custom_listview, personList);
        listView.setAdapter(customArrayAdapter);
    }
}