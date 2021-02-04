package com.ourdevelops.ourjek.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class TransaksiModel extends RealmObject implements Serializable{

    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("id_pelanggan")
    private String idPelanggan;

    @Expose
    @SerializedName("id_driver")
    private String idDriver;

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
    @SerializedName("waktu_order")
    private Date waktuOrder;

    @Expose
    @SerializedName("waktu_selesai")
    private Date waktuSelesai;

    @Expose
    @SerializedName("alamat_asal")
    private String alamatAsal;

    @Expose
    @SerializedName("alamat_tujuan")
    private String alamatTujuan;

    @Expose
    @SerializedName("kode_promo")
    private String kodePromo;

    @Expose
    @SerializedName("kredit_promo")
    private String kreditPromo;

    @Expose
    @SerializedName("pakai_wallet")
    private boolean pakaiWallet;

    @Expose
    @SerializedName("rate")
    private String rate;

    @Expose
    @SerializedName("status")
    public int status;

    @Expose
    @SerializedName("estimasi_time")
    private String estimasi;

    @Expose
    @SerializedName("nama_pengirim")
    public String namaPengirim;

    @Expose
    @SerializedName("telepon_pengirim")
    public String teleponPengirim;

    @Expose
    @SerializedName("nama_penerima")
    public String namaPenerima;

    @Expose
    @SerializedName("telepon_penerima")
    public String teleponPenerima;

    @Expose
    @SerializedName("nama_barang")
    public String namaBarang;

    @Expose
    @SerializedName("biaya_akhir")
    public String biaya_akhir;

    @Expose
    @SerializedName("total_biaya")
    public String total_biaya;

    @Expose
    @SerializedName("nama_merchant")
    public String nama_merchant;

    @Expose
    @SerializedName("token_merchant")
    public String token_merchant;

    @Expose
    @SerializedName("id_trans_merchant")
    public String idtransmerchant;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
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

    public Date getWaktuOrder() {
        return waktuOrder;
    }

    public void setWaktuOrder(Date waktuOrder) {
        this.waktuOrder = waktuOrder;
    }

    public Date getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Date waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
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

    public String getKodePromo() {
        return kodePromo;
    }

    public void setKodePromo(String kodePromo) {
        this.kodePromo = kodePromo;
    }

    public String getKreditPromo() {
        return kreditPromo;
    }

    public void setKreditPromo(String kreditPromo) {
        this.kreditPromo = kreditPromo;
    }

    public boolean isPakaiWallet() {
        return pakaiWallet;
    }

    public void setPakaiWallet(boolean pakaiWallet) {
        this.pakaiWallet = pakaiWallet;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }

    public String getNamaPengirim() {
        return namaPengirim;
    }

    public void setNamaPengirim(String namaPengirim) {
        this.namaPengirim = namaPengirim;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public void setNamaPenerima(String namaPenerima) {
        this.namaPenerima = namaPenerima;
    }

    public String getTeleponPengirim() {
        return teleponPengirim;
    }

    public void setTeleponPengirim(String teleponPengirim) {
        this.teleponPengirim = teleponPengirim;
    }

    public String getTeleponPenerima() {
        return teleponPengirim;
    }

    public void setTeleponPenerima(String teleponPenerima) {
        this.teleponPenerima = teleponPenerima;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getBiaya_akhir() {
        return biaya_akhir;
    }

    public void setBiaya_akhir(String biaya_akhir) {
        this.biaya_akhir = biaya_akhir;
    }

    public String getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(String total_biaya) {
        this.total_biaya = total_biaya;
    }

    public String getNama_merchant() {
        return nama_merchant;
    }

    public void setNama_merchant(String nama_merchant) {
        this.nama_merchant = nama_merchant;
    }

    public String getToken_merchant() {
        return token_merchant;
    }

    public void setToken_merchant(String token_merchant) {
        this.token_merchant = token_merchant;
    }

    public String getIdtransmerchant() {
        return idtransmerchant;
    }

    public void setIdtransmerchant(String idtransmerchant) {
        this.idtransmerchant = idtransmerchant;
    }
}
