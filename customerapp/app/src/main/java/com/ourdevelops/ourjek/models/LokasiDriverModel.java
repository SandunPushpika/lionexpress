package com.ourdevelops.ourjek.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 24/02/2019.
 */

public class LokasiDriverModel {

    @SerializedName("id_driver")
    @Expose
    private String idDriver;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;

    public String getIdDriver() {
        return idDriver;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
