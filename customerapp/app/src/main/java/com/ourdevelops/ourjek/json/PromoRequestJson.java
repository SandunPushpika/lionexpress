package com.ourdevelops.ourjek.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 11/28/2019.
 */

public class PromoRequestJson {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("fitur")
    @Expose
    private String fitur;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFitur() {
        return fitur;
    }

    public void setFitur(String fitur) {
        this.fitur = fitur;
    }
}
