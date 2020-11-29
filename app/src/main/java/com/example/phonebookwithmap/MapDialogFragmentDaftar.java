package com.example.phonebookwithmap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapDialogFragmentDaftar extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper databaseHelper;
    public static final String ARG_TITLE = "MapDialogFragment.Title";
    public static final String DEBUG_TAG = "DEBUG_TAG";
    private View dialogView;

    public MapDialogFragmentDaftar() {

    }

    public static MapDialogFragmentDaftar newInstance(String title) {
        MapDialogFragmentDaftar frag = new MapDialogFragmentDaftar();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE, "Form Kontak");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.register_dialog, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle(title);

        final EditText noHpInputDialog = (EditText) dialogView.findViewById(R.id.txte_no_hp);
        final EditText namaInputDialog = (EditText) dialogView.findViewById(R.id.txte_nama);
        final EditText alamatInputDialog = (EditText) dialogView.findViewById(R.id.txte_alamat);
        final TextView latitudeInputDialog = (TextView) dialogView.findViewById(R.id.txtv_latitude);
        final TextView longitudeInputDialog = (TextView) dialogView.findViewById(R.id.txtv_longitude);
        final Button btnCariInputDialog = (Button) dialogView.findViewById(R.id.btn_cari);
        btnCariInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Geocoder geocoder = new Geocoder(getActivity());

                    try {
                        List<Address> daftar = geocoder.getFromLocationName(alamatInputDialog.getText().toString(), 1);
                        Log.d(DEBUG_TAG, "Hasil: " + daftar.toString());
                        Address alamat = daftar.get(0);
                        String strAlamat = alamat.getAddressLine(0);
                        Double latitudeCari = alamat.getLatitude();
                        Double longitudeCari = alamat.getLongitude();
                        LatLng lokasi = new LatLng(latitudeCari, longitudeCari);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 15.0f));
                        latitudeInputDialog.setText(String.valueOf(latitudeCari));
                        longitudeInputDialog.setText(String.valueOf(longitudeCari));
                    } catch (Exception ie) {
                        Toast.makeText(getActivity(), "Alamat tidak ditemukan", Toast.LENGTH_LONG).show();
                        Log.d("DEBUG_TAG", "ERROR: " + ie.getMessage());
                    }
            }
        });

        alertDialogBuilder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Person person = new Person();
                person.setNo_hp(noHpInputDialog.getText().toString());
                person.setNama(namaInputDialog.getText().toString());
                person.setAlamat(alamatInputDialog.getText().toString());
                person.setLatitude(Double.parseDouble(latitudeInputDialog.getText().toString()));
                person.setLongitude(Double.parseDouble(longitudeInputDialog.getText().toString()));
                person.setSoundex(Soundex.getCode(namaInputDialog.getText().toString()));

                databaseHelper.simpanData(person);
                Toast.makeText(getActivity(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onPause() {
        final FragmentManager fragManager = this.getFragmentManager();
        final Fragment fragment = fragManager.findFragmentById(R.id.map1);
        if(fragment != null){
            fragManager.beginTransaction().remove(fragment).commit();
            mMap = null;
            dialogView = null;
        }
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng its = new LatLng(-7.28,112.79);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(its, 10));

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (dialogView != null) {
                    LatLng latLng = googleMap.getCameraPosition().target;
                    final TextView latitudeInputDialog = (TextView) dialogView.findViewById(R.id.txtv_latitude);
                    final TextView longitudeInputDialog = (TextView) dialogView.findViewById(R.id.txtv_longitude);
                    latitudeInputDialog.setText(String.valueOf(latLng.latitude));
                    longitudeInputDialog.setText(String.valueOf(latLng.longitude));
                }
            }
        });
    }
}
