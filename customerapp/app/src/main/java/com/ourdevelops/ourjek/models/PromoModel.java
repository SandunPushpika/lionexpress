package com.ourdevelops.ourjek.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoModel {
    @Expose
    @SerializedName("foto")
    private String foto;

    @Expose
    @SerializedName("fitur_promosi")
    private int fitur_promosi;

    @Expose
    @SerializedName("link_promosi")
    private String link_promosi;

    @Expose
    @SerializedName("type_promosi")
    private String type_promosi;

    @Expose
    @SerializedName("icon")
    private String icon;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getFiturpromosi() {
        return fitur_promosi;
    }

    public void setFiturpromosi(int fitur_promosi) {
        this.fitur_promosi = fitur_promosi;
    }

    public String getLinkpromosi() {
        return link_promosi;
    }

    public void setLink_promosi(String link_promosi) {
        this.link_promosi = link_promosi;
    }

    public String getTypepromosi() {
        return type_promosi;
    }

    public void setTypepromosi(String type_promosi) {
        this.type_promosi = type_promosi;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
