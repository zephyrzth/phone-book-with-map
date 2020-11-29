package com.example.phonebookwithmap;

public class Person {
    private int id;
    private String no_hp, nama, alamat, soundex;
    private Double latitude, longitude;

    public Person(int id) {
        this.id = id;
    }
    public Person() {

    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public void setSoundex(String soundex) {
        this.soundex = soundex;
    }

    public int getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public String getNo_hp() {
        return no_hp;
    }
    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public String getAlamat() {
        return alamat;
    }
    public String getSoundex() {
        return soundex;
    }
}
