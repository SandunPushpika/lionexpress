package com.ourdevelops.ourjek.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class KodePromoModel {
    @Expose
    @SerializedName("nama_promo")
    private String namapromo;

    @Expose
    @SerializedName("kode_promo")
    private String kodepromo;

    @Expose
    @SerializedName("expired")
    private Date expired;

    @Expose
    @SerializedName("image_promo")
    private String imagepromo;


    public String getNamapromo() {
        return namapromo;
    }

    public void setNamapromo(String namapromo) {
        this.namapromo = namapromo;
    }


    public String getImagepromo() {
        return imagepromo;
    }

    public void setImagepromo(String imagepromo) {
        this.imagepromo = imagepromo;
    }

    public String getKodepromo() {
        return kodepromo;
    }

    public void setKodepromo(String kodepromo) {
        this.kodepromo = kodepromo;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
