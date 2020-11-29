package com.example.phonebookwithmap;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDialogFragmentJarak extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String ARG_TITLE = "MapDialogFragment.Title";
    public static final String ARG_LATITUDE = "MapDialogFragment.Latitude";
    public static final String ARG_LONGITUDE = "MapDialogFragment.Longitude";
    public static final String ARG_ALAMAT = "MapDialogFragment.Alamat";
    public static final String DEBUG_TAG = "DEBUG_TAG";
    public static final int REQUEST_ACTIVITY_RECOGNITION = 23;
    private View dialogView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker markerAku, markerKamu;
    private LatLng lokasiKamu;
    private String alamatKamu;

    public MapDialogFragmentJarak() {

    }

    public static MapDialogFragmentJarak newInstance(String title, String alamat, Double latitude, Double longitude) {
        MapDialogFragmentJarak frag = new MapDialogFragmentJarak();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putString(ARG_ALAMAT, alamat);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLokasiListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20, locationListener);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class MyLokasiListener implements LocationListener {

        private TextView textViewLat, textViewLong;

        @Override
        public void onLocationChanged(@NonNull Location location) {
            LatLng lokasiAku = new LatLng(location.getLatitude(), location.getLongitude());
            markerAku.setPosition(lokasiAku);
            setJarak();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE, "Jarak");
        Double latitudeKamu = getArguments().getDouble(ARG_LATITUDE, 0.0);
        Double longitudeKamu = getArguments().getDouble(ARG_LONGITUDE, 0.0);
        alamatKamu = getArguments().getString(ARG_ALAMAT, "Kosong");
        lokasiKamu = new LatLng(latitudeKamu, longitudeKamu);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.jarak_dialog, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
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
        final Fragment fragment = fragManager.findFragmentById(R.id.map2);
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
        LatLng its = new LatLng(-7.28,112.79);
        markerAku = mMap.addMarker(new MarkerOptions().position(its).title("Aku di sini"));
        markerKamu = mMap.addMarker(new MarkerOptions().position(lokasiKamu).title(alamatKamu));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiKamu, 10.0f));
    }

    private void setJarak() {
        if (dialogView != null) {
            LatLng lokasiAku = markerAku.getPosition();
            LatLng lokasiKamu = markerKamu.getPosition();

            float hasilJarak = hitungJarak(lokasiAku.latitude, lokasiAku.longitude, lokasiKamu.latitude, lokasiKamu.longitude);
            final TextView txtJarak = (TextView) dialogView.findViewById(R.id.txtv_jarak);
            txtJarak.setText(hasilJarak + " km");
        }
    }

    private float hitungJarak(double latAsal, double lngAsal, double latTujuan, double lngTujuan) {
        Location asal = new Location("Asal");
        Location tujuan = new Location("Tujuan");
        asal.setLatitude(latAsal);
        asal.setLongitude(lngAsal);
        tujuan.setLatitude(latTujuan);
        tujuan.setLongitude(lngTujuan);

        float hasilJarak = (float) asal.distanceTo(tujuan) / 1000;
        return hasilJarak;
    }
}
