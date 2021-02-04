package com.ourdevelops.ourjek.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ourdevelops.ourjek.models.PesananMerchant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class ItemRequestJson implements Serializable {

    @Expose
    @SerializedName("id_pelanggan")
    private String idPelanggan;

    @Expose
    @SerializedName("order_fitur")
    private String orderFitur;

    @Expose
    @SerializedName("start_latitude")
    private double startLatitude;

    @Expose
    @SerializedName("start_longitude")
    private double startLongitude;

    @Expose
    @SerializedName("end_latitude")
    private double endLatitude;

    @Expose
    @SerializedName("end_longitude")
    private double endLongitude;

    @Expose
    @SerializedName("jarak")
    private double jarak;

    @Expose
    @SerializedName("harga")
    private long harga;

    @Expose
    @SerializedName("alamat_asal")
    private String alamatAsal;

    @Expose
    @SerializedName("alamat_tujuan")
    private String alamatTujuan;

    @Expose
    @SerializedName("pakai_wallet")
    private int pakaiWallet;

    @Expose
    @SerializedName("kredit_promo")
    private String kreditpromo;

    @Expose
    @SerializedName("estimasi")
    private String estimasi;

    @Expose
    @SerializedName("id_resto")
    private String idResto;

    @Expose
    @SerializedName("total_biaya_belanja")
    private long totalBiayaBelanja;

    @Expose
    @SerializedName("catatan")
    private String catatan;

    @Expose
    @SerializedName("pesanan")
    private List<PesananMerchant> pesanan = new ArrayList<>();

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getOrderFitur() {
        return orderFitur;
    }

    public void setOrderFitur(String orderFitur) {
        this.orderFitur = orderFitur;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public String getAlamatAsal() {
        return alamatAsal;
    }

    public void setAlamatAsal(String alamatAsal) {
        this.alamatAsal = alamatAsal;
    }

    public String getAlamatTujuan() {
        return alamatTujuan;
    }

    public void setAlamatTujuan(String alamatTujuan) {
        this.alamatTujuan = alamatTujuan;
    }

    public int isPakaiWallet() {
        return pakaiWallet;
    }

    public void setPakaiWallet(int pakaiMpay) {
        this.pakaiWallet = pakaiMpay;
    }

    public String getKreditpromo() {
        return kreditpromo;
    }

    public void setKreditpromo(String kreditpromo) {
        this.kreditpromo = kreditpromo;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }

    public String getIdResto() {
        return idResto;
    }

    public void setIdResto(String idResto) {
        this.idResto = idResto;
    }

    public long getTotalBiayaBelanja() {
        return totalBiayaBelanja;
    }

    public void setTotalBiayaBelanja(long totalBiayaBelanja) {
        this.totalBiayaBelanja = totalBiayaBelanja;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public List<PesananMerchant> getPesanan() {
        return pesanan;
    }

    public void setPesanan(List<PesananMerchant> pesanan) {
        this.pesanan = pesanan;
    }

}
