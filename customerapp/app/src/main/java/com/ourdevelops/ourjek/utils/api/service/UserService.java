package com.ourdevelops.ourjek.utils.api.service;

import com.ourdevelops.ourjek.json.AllMerchantByNearResponseJson;
import com.ourdevelops.ourjek.json.AllMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.AllTransResponseJson;
import com.ourdevelops.ourjek.json.BankResponseJson;
import com.ourdevelops.ourjek.json.BeritaDetailRequestJson;
import com.ourdevelops.ourjek.json.BeritaDetailResponseJson;
import com.ourdevelops.ourjek.json.ChangePassRequestJson;
import com.ourdevelops.ourjek.json.DetailRequestJson;
import com.ourdevelops.ourjek.json.EditprofileRequestJson;
import com.ourdevelops.ourjek.json.GetAllMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.GetFiturResponseJson;
import com.ourdevelops.ourjek.json.GetHomeRequestJson;
import com.ourdevelops.ourjek.json.GetHomeResponseJson;
import com.ourdevelops.ourjek.json.GetMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.LoginRequestJson;
import com.ourdevelops.ourjek.json.LoginResponseJson;
import com.ourdevelops.ourjek.json.MerchantByCatResponseJson;
import com.ourdevelops.ourjek.json.MerchantByIdResponseJson;
import com.ourdevelops.ourjek.json.MerchantByNearResponseJson;
import com.ourdevelops.ourjek.json.MerchantbyIdRequestJson;
import com.ourdevelops.ourjek.json.PrivacyRequestJson;
import com.ourdevelops.ourjek.json.PrivacyResponseJson;
import com.ourdevelops.ourjek.json.PromoRequestJson;
import com.ourdevelops.ourjek.json.PromoResponseJson;
import com.ourdevelops.ourjek.json.RateRequestJson;
import com.ourdevelops.ourjek.json.RateResponseJson;
import com.ourdevelops.ourjek.json.RegisterRequestJson;
import com.ourdevelops.ourjek.json.RegisterResponseJson;
import com.ourdevelops.ourjek.json.ResponseJson;
import com.ourdevelops.ourjek.json.SearchMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.TopupRequestJson;
import com.ourdevelops.ourjek.json.TopupResponseJson;
import com.ourdevelops.ourjek.json.WalletRequestJson;
import com.ourdevelops.ourjek.json.WalletResponseJson;
import com.ourdevelops.ourjek.json.WithdrawRequestJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public interface UserService {

    @POST("pelanggan/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("pelanggan/kodepromo")
    Call<PromoResponseJson> promocode(@Body PromoRequestJson param);

    @POST("pelanggan/listkodepromo")
    Call<PromoResponseJson> listpromocode(@Body PromoRequestJson param);

    @POST("pelanggan/list_bank")
    Call<BankResponseJson> listbank(@Body WithdrawRequestJson param);

    @POST("pelanggan/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("pelanggan/register_user")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

    @GET("pelanggan/detail_fitur")
    Call<GetFiturResponseJson> getFitur();

    @POST("pelanggan/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("pelanggan/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("pelanggan/home")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);

    @POST("pelanggan/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("pelanggan/withdraw")
    Call<ResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("pelanggan/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("pelanggan/rate_driver")
    Call<RateResponseJson> rateDriver(@Body RateRequestJson param);

    @POST("pelanggan/edit_profile")
    Call<RegisterResponseJson> editProfile(@Body EditprofileRequestJson param);

    @POST("pelanggan/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);

    @POST("pelanggan/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);

    @POST("pelanggan/detail_berita")
    Call<BeritaDetailResponseJson> beritadetail(@Body BeritaDetailRequestJson param);

    @POST("pelanggan/all_berita")
    Call<BeritaDetailResponseJson> allberita(@Body BeritaDetailRequestJson param);

    @POST("pelanggan/merchantbykategoripromo")
    Call<MerchantByCatResponseJson> getmerchanbycat(@Body GetMerchantbyCatRequestJson param);

    @POST("pelanggan/merchantbykategori")
    Call<MerchantByNearResponseJson> getmerchanbynear(@Body GetMerchantbyCatRequestJson param);

    @POST("pelanggan/allmerchantbykategori")
    Call<AllMerchantByNearResponseJson> getallmerchanbynear(@Body GetAllMerchantbyCatRequestJson param);

    @POST("pelanggan/itembykategori")
    Call<MerchantByIdResponseJson> getitembycat(@Body GetAllMerchantbyCatRequestJson param);

    @POST("pelanggan/searchmerchant")
    Call<AllMerchantByNearResponseJson> searchmerchant(@Body SearchMerchantbyCatRequestJson param);

    @POST("pelanggan/allmerchant")
    Call<AllMerchantByNearResponseJson> allmerchant(@Body AllMerchantbyCatRequestJson param);

    @POST("pelanggan/merchantbyid")
    Call<MerchantByIdResponseJson> merchantbyid(@Body MerchantbyIdRequestJson param);


}
