package com.example.phonebookwithmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "phoneBookDatabase";
    private static final int DATABASE_VERSION = 1;

    // Error Tag
    private static final String ERROR_TAG = "ERROR_TAG";

    // Nama tabel
    private static final String TABLE_KONTAKS = "kontaks";

    // Kolom tabel mahasiswas
    private static final String KEY_KONTAKS_ID = "id";
    private static final String KEY_KONTAKS_NO_HP = "no_hp";
    private static final String KEY_KONTAKS_NAMA = "nama";
    private static final String KEY_KONTAKS_ALAMAT = "alamat";
    private static final String KEY_KONTAKS_LATITUDE = "latitude";
    private static final String KEY_KONTAKS_LONGITUDE = "longitude";
    private static final String KEY_KONTAKS_SOUNDEX = "soundex";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KONTAKS_TABLE = "CREATE TABLE " + TABLE_KONTAKS +
                "(" +
                KEY_KONTAKS_ID + " INTEGER PRIMARY KEY," +
                KEY_KONTAKS_NO_HP + " TEXT," +
                KEY_KONTAKS_NAMA + " TEXT," +
                KEY_KONTAKS_ALAMAT + " TEXT," +
                KEY_KONTAKS_LATITUDE + " DOUBLE," +
                KEY_KONTAKS_LONGITUDE + " DOUBLE," +
                KEY_KONTAKS_SOUNDEX + " TEXT" +
                ");";
        db.execSQL(CREATE_KONTAKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void simpanData(Person person) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_KONTAKS_NO_HP, person.getNo_hp());
            contentValues.put(KEY_KONTAKS_NAMA, person.getNama());
            contentValues.put(KEY_KONTAKS_ALAMAT, person.getAlamat());
            contentValues.put(KEY_KONTAKS_LATITUDE, person.getLatitude());
            contentValues.put(KEY_KONTAKS_LONGITUDE, person.getLongitude());
            contentValues.put(KEY_KONTAKS_SOUNDEX, person.getSoundex());

            db.insertOrThrow(TABLE_KONTAKS, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(ERROR_TAG, "ERROR DB: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Person> ambilSemuaData() {
        ArrayList<Person> persons = new ArrayList<>();

        String query = String.format("SELECT * FROM %s", TABLE_KONTAKS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Person newPerson = new Person(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KONTAKS_ID)));
                    newPerson.setNo_hp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_NO_HP)));
                    newPerson.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_NAMA)));
                    newPerson.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_ALAMAT)));
                    newPerson.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_KONTAKS_LATITUDE)));
                    newPerson.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_KONTAKS_LONGITUDE)));
                    newPerson.setSoundex(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_SOUNDEX)));

                    persons.add(newPerson);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, "ERROR DB: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return persons;
    }

    public ArrayList<Person> cariData(Person person) {
        ArrayList<Person> persons = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE %s LIKE ? AND (%s LIKE ? OR %s = ?) AND %s LIKE ?",
                TABLE_KONTAKS,
                KEY_KONTAKS_NO_HP,
                KEY_KONTAKS_NAMA,
                KEY_KONTAKS_SOUNDEX,
                KEY_KONTAKS_ALAMAT);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] { "%" + person.getNo_hp() + "%",
                "%" + person.getNama() + "%",
                person.getSoundex(),
                "%" + person.getAlamat() + "%"
        });
        try {
            if (cursor.moveToFirst()) {
                do {
                    Person newPerson = new Person(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KONTAKS_ID)));
                    newPerson.setNo_hp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_NO_HP)));
                    newPerson.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_NAMA)));
                    newPerson.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_ALAMAT)));
                    newPerson.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_KONTAKS_LATITUDE)));
                    newPerson.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_KONTAKS_LONGITUDE)));
                    newPerson.setSoundex(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KONTAKS_SOUNDEX)));

                    persons.add(newPerson);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, "ERROR DB: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return persons;
    }

    public void editData(Person person) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_KONTAKS_NO_HP, person.getNo_hp());
            contentValues.put(KEY_KONTAKS_NAMA, person.getNama());
            contentValues.put(KEY_KONTAKS_ALAMAT, person.getAlamat());
            contentValues.put(KEY_KONTAKS_LATITUDE, person.getLatitude());
            contentValues.put(KEY_KONTAKS_LONGITUDE, person.getLongitude());
            contentValues.put(KEY_KONTAKS_SOUNDEX, person.getSoundex());

            db.update(TABLE_KONTAKS, contentValues, KEY_KONTAKS_ID + " = ?",
                    new String[] { String.valueOf(person.getId()) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(ERROR_TAG, "ERROR DB: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void hapusData(Person person) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_KONTAKS, KEY_KONTAKS_ID + " = ?", new String[] { String.valueOf(person.getId()) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(ERROR_TAG, "ERROR DB: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}