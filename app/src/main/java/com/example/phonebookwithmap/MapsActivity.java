package com.example.phonebookwithmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MapsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initWidgets();
    }

    private void initWidgets() {
        findViewById(R.id.btnSimpan).setOnClickListener(operasi);
        findViewById(R.id.btnAmbilData).setOnClickListener(operasi);
        findViewById(R.id.btnSemuaData).setOnClickListener(operasi);

        databaseHelper = DatabaseHelper.getInstance(this);
    }

    private final View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSimpan: simpan(); break;
                case R.id.btnAmbilData: ambilData(); break;
                case R.id.btnSemuaData: semuaData(); break;
            }
        }
    };

    private void simpan() {
        MapDialogFragmentDaftar mapDialogFragment = MapDialogFragmentDaftar.newInstance("Tambah Kontak");
        mapDialogFragment.show(getSupportFragmentManager(), "fragment_dialog_register");
    }

    private void ambilData() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.find_dialog, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Cari Kontak");

        final EditText noHpInputDialog = (EditText) dialogView.findViewById(R.id.txte_no_hp);
        final EditText namaInputDialog = (EditText) dialogView.findViewById(R.id.txte_nama);
        final EditText alamatInputDialog = (EditText) dialogView.findViewById(R.id.txte_alamat);

        alertDialogBuilder.setPositiveButton("Cari", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent ambilDataIntent = new Intent(MapsActivity.this, ListDataActivity.class);
                ambilDataIntent.putExtra("no_hp", noHpInputDialog.getText().toString());
                ambilDataIntent.putExtra("nama", namaInputDialog.getText().toString());
                ambilDataIntent.putExtra("alamat", alamatInputDialog.getText().toString());
                startActivity(ambilDataIntent);
            }
        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        noHpInputDialog.setText(null);
        namaInputDialog.setText(null);
        alamatInputDialog.setText(null);
    }

    private void semuaData() {
        Intent ambilDataIntent = new Intent(MapsActivity.this, ListDataActivity.class);
        startActivity(ambilDataIntent);
    }
}