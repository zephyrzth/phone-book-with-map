package com.example.phonebookwithmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Person> {

    private ArrayList<Person> persons;
    private Context context;
    private FragmentActivity activity;
    private int resource;
    private DatabaseHelper databaseHelper;

    private static class ViewHolder {
        TextView no_hp;
        TextView nama;
        TextView alamat;
        Button buttonJarak;
        Button buttonEdit;
        Button buttonDelete;
    }

    public CustomArrayAdapter(@NonNull Context context, @NonNull FragmentActivity activity, int resource, @NonNull ArrayList<Person> persons) {
        super(context, resource, persons);
        this.context = context;
        this.resource = resource;
        this.persons = persons;
        this.activity = activity;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, null, false);

            viewHolder = new ViewHolder();
            viewHolder.no_hp = (TextView) convertView.findViewById(R.id.itemNoHp);
            viewHolder.nama = (TextView) convertView.findViewById(R.id.itemNama);
            viewHolder.alamat = (TextView) convertView.findViewById(R.id.itemAlamat);
            viewHolder.buttonJarak = (Button) convertView.findViewById(R.id.itemButtonJarak);
//            viewHolder.buttonEdit = (Button) convertView.findViewById(R.id.itemButtonEdit);
            viewHolder.buttonDelete = (Button) convertView.findViewById(R.id.itemButtonDelete);
            viewHolder.buttonJarak = (Button) convertView.findViewById(R.id.itemButtonJarak);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Person person = persons.get(position);

        viewHolder.no_hp.setText(person.getNo_hp());
        viewHolder.nama.setText(person.getNama());
        viewHolder.alamat.setText(person.getAlamat());
//        viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editItem(position);
//            }
//        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
        viewHolder.buttonJarak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jarakItem(position);
            }
        });

        return convertView;
    }

    private void jarakItem(final int position) {
        final Person person = persons.get(position);
        MapDialogFragmentJarak mapDialogFragment = MapDialogFragmentJarak.newInstance("Jarak Anda dengan Dia",
                person.getAlamat(), person.getLatitude(), person.getLongitude());
        mapDialogFragment.show(activity.getSupportFragmentManager(), "fragment_dialog_jarak");
    }

//    private void editItem(final int position) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        final View dialogView = LayoutInflater.from(context).inflate(R.layout.register_dialog, null);
//        alertDialogBuilder.setView(dialogView);
//        alertDialogBuilder.setTitle("Edit Kontak");
//
//        final EditText noHpInputDialog = (EditText) dialogView.findViewById(R.id.txte_no_hp);
//        final EditText namaInputDialog = (EditText) dialogView.findViewById(R.id.txte_nama);
//        final EditText alamatInputDialog = (EditText) dialogView.findViewById(R.id.txte_alamat);
//        final TextView latitudeInputDialog = (TextView) dialogView.findViewById(R.id.txtv_latitude);
//        final TextView longitudeInputDialog = (TextView) dialogView.findViewById(R.id.txtv_longitude);
//
//        final Person person = persons.get(position);
//        noHpInputDialog.setText(person.getNo_hp());
//        namaInputDialog.setText(person.getNama());
//        alamatInputDialog.setText(person.getAlamat());
//        latitudeInputDialog.setText(String.valueOf(person.getLatitude()));
//        longitudeInputDialog.setText(String.valueOf(person.getLongitude()));
//
//        alertDialogBuilder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                person.setNo_hp(noHpInputDialog.getText().toString());
//                person.setNama(namaInputDialog.getText().toString());
//                person.setAlamat(alamatInputDialog.getText().toString());
//                person.setLatitude(Double.parseDouble(latitudeInputDialog.getText().toString()));
//                person.setLongitude(Double.parseDouble(longitudeInputDialog.getText().toString()));
//                person.setSoundex(Soundex.getCode(namaInputDialog.getText().toString()));
//
//                databaseHelper.editData(person);
//                notifyDataSetChanged();
//            }
//        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

    private void deleteItem(final int position) {
        final Person person = persons.get(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Apakah Anda yakin ingin menghapus data ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.hapusData(person);
                persons.remove(position);
                notifyDataSetChanged();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
