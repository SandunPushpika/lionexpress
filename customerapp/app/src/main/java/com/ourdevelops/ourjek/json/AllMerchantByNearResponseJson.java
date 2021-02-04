package com.ourdevelops.ourjek.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ourdevelops.ourjek.models.CatMerchantModel;
import com.ourdevelops.ourjek.models.MerchantNearModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class AllMerchantByNearResponseJson {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("allmerchantnearby")
    @Expose
    private List<MerchantNearModel> data = new ArrayList<>();

    @SerializedName("kategorymerchant")
    @Expose
    private List<CatMerchantModel> kategori = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MerchantNearModel> getData() {
        return data;
    }

    public void setData(List<MerchantNearModel> data) {
        this.data = data;
    }

    public List<CatMerchantModel> getKategori() {
        return kategori;
    }

    public void setKategori(List<CatMerchantModel> kategori) {
        this.kategori = kategori;
    }
}
