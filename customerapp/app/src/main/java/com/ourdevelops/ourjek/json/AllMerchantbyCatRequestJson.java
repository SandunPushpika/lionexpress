package com.ourdevelops.ourjek.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class AllMerchantbyCatRequestJson {

    @SerializedName("latitude")
    @Expose
    private String lat;

    @SerializedName("longitude")
    @Expose
    private String lon;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("no_telepon")
    @Expose
    private String phone;

    @SerializedName("fitur")
    @Expose
    private String fitur;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKategori() {
        return fitur;
    }

    public void setKategori(String fitur) {
        this.fitur = fitur;
    }
}
