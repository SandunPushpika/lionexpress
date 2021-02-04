package com.ourdevelops.ourjek.fragment;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.activity.AllBeritaActivity;
import com.ourdevelops.ourjek.activity.IntroActivity;
import com.ourdevelops.ourjek.activity.PromoActivity;
import com.ourdevelops.ourjek.activity.TopupSaldoActivity;
import com.ourdevelops.ourjek.activity.WalletActivity;
import com.ourdevelops.ourjek.activity.WithdrawActivity;
import com.ourdevelops.ourjek.constants.BaseApp;
import com.ourdevelops.ourjek.constants.Constants;
import com.ourdevelops.ourjek.item.AllFiturItem;
import com.ourdevelops.ourjek.item.BeritaItem;
import com.ourdevelops.ourjek.item.CatMerchantItem;
import com.ourdevelops.ourjek.item.CatMerchantNearItem;
import com.ourdevelops.ourjek.item.FiturItem;
import com.ourdevelops.ourjek.item.MerchantItem;
import com.ourdevelops.ourjek.item.MerchantNearItem;
import com.ourdevelops.ourjek.item.RatingItem;
import com.ourdevelops.ourjek.item.SliderItem;
import com.ourdevelops.ourjek.json.GetHomeRequestJson;
import com.ourdevelops.ourjek.json.GetHomeResponseJson;
import com.ourdevelops.ourjek.json.GetMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.MerchantByCatResponseJson;
import com.ourdevelops.ourjek.json.MerchantByNearResponseJson;
import com.ourdevelops.ourjek.models.AllFiturModel;
import com.ourdevelops.ourjek.models.CatMerchantModel;
import com.ourdevelops.ourjek.models.FiturDataModel;
import com.ourdevelops.ourjek.models.FiturModel;
import com.ourdevelops.ourjek.models.MerchantModel;
import com.ourdevelops.ourjek.models.MerchantNearModel;
import com.ourdevelops.ourjek.models.PayuModel;
import com.ourdevelops.ourjek.models.User;
import com.ourdevelops.ourjek.utils.Log;
import com.ourdevelops.ourjek.utils.SettingPreference;
import com.ourdevelops.ourjek.utils.Utility;
import com.ourdevelops.ourjek.utils.api.ServiceGenerator;
import com.ourdevelops.ourjek.utils.api.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.realm.Realm;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private Context context;
    private ViewPager viewPager, rvreview;
    private SliderItem adapter;
    private Integer[] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private CircleIndicator circleIndicator, circleIndicatorreview;
    private RecyclerView rvCategory, rvberita,rvmerchant,rvcatmerchantpromo,rvcatmerchantnear,rvmerchantnear;
    private LinearLayout llslider, promoslider, llrating, llberita,llmerchant, llmerchantnear,shimlistpromo,shimlistcatpromo,shimlistnear,shimlistcatnear;
    private FiturItem fiturItem;
    private RatingItem ratingItem;
    private BeritaItem beritaItem;
    private MerchantItem merchantItem;
    private MerchantNearItem merchantNearItem;
    private CatMerchantNearItem catMerchantNearItem;
    private CatMerchantItem catMerchantItem;
    private ShimmerFrameLayout mShimmerCat, shimerPromo, shimerreview, shimberita,shimmerchantpromo,getShimmerchantnear;
    private TextView saldo;
    private TextView nodatapromo;
    private TextView nodatanear;
    private SettingPreference sp;
    private List<MerchantModel> click;
    private List<MerchantNearModel> clicknear;
    private ArrayList<FiturDataModel> fiturlist;
    private List<FiturModel> fiturdata;
    private List<AllFiturModel> allfiturdata;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        View bottom_sheet = getView.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        viewPager = getView.findViewById(R.id.viewPager);
        circleIndicator = getView.findViewById(R.id.indicator_unselected_background);
        circleIndicatorreview = getView.findViewById(R.id.indicator_unselected_background_review);
        viewPager = getView.findViewById(R.id.viewPager);
        rvCategory = getView.findViewById(R.id.category);
        rvreview = getView.findViewById(R.id.viewPagerreview);
        rvberita = getView.findViewById(R.id.berita);
        rvmerchant = getView.findViewById(R.id.merchantpromo);
        rvcatmerchantpromo = getView.findViewById(R.id.catmerchantpromo);
        rvcatmerchantnear = getView.findViewById(R.id.catmerchantnear);
        promoslider = getView.findViewById(R.id.rlslider);
        llslider = getView.findViewById(R.id.promoslider);
        saldo = getView.findViewById(R.id.saldo);
        RelativeLayout topup = getView.findViewById(R.id.topup);
        RelativeLayout withdraw = getView.findViewById(R.id.withdraw);
        RelativeLayout detail = getView.findViewById(R.id.detail);
        llberita = getView.findViewById(R.id.llnews);
        llmerchant = getView.findViewById(R.id.llmerchantpromo);
        llmerchantnear = getView.findViewById(R.id.llmerchantnear);
        llrating = getView.findViewById(R.id.llrating);
        TextView showall = getView.findViewById(R.id.showall);
        shimlistpromo = getView.findViewById(R.id.shimlistpromo);
        shimlistnear = getView.findViewById(R.id.shimlistnear);
        nodatapromo = getView.findViewById(R.id.nodatapromo);
        shimlistcatpromo = getView.findViewById(R.id.shimlistcatpromo);
        shimlistcatnear = getView.findViewById(R.id.shimlistcatnear);
        rvcatmerchantnear = getView.findViewById(R.id.catmerchantnear);
        rvmerchantnear = getView.findViewById(R.id.merchantnear);
        nodatanear = getView.findViewById(R.id.nodatanear);
        sp = new SettingPreference(context);
        RelativeLayout promo = getView.findViewById(R.id.promo);

        fiturlist = new ArrayList<>();

        mShimmerCat = getView.findViewById(R.id.shimmercat);
        shimerPromo = getView.findViewById(R.id.shimmepromo);
        shimerreview = getView.findViewById(R.id.shimreview);
        shimberita = getView.findViewById(R.id.shimberita);
        shimmerchantpromo = getView.findViewById(R.id.shimmerchantpromo);
        getShimmerchantnear = getView.findViewById(R.id.shimmerchantnear);

        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));


        rvberita.setHasFixedSize(true);
        rvberita.setNestedScrollingEnabled(false);
        rvberita.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvmerchant.setHasFixedSize(true);
        rvmerchant.setNestedScrollingEnabled(false);
        rvmerchant.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvcatmerchantnear.setHasFixedSize(true);
        rvcatmerchantnear.setNestedScrollingEnabled(false);
        rvcatmerchantnear.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rvcatmerchantpromo.setHasFixedSize(true);
        rvcatmerchantpromo.setNestedScrollingEnabled(false);
        rvcatmerchantpromo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));




        Integer[] colors_temp = {
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.transparent)
        };

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TopupSaldoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PromoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WithdrawActivity.class);
                i.putExtra("type", "withdraw");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AllBeritaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WalletActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rvreview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (ratingItem.getCount() - 1) && position < (colors.length - 1)) {
                    rvreview.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    rvreview.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocation.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    gethome(location);
                    Constants.LATITUDE = location.getLatitude();
                    Constants.LONGITUDE = location.getLongitude();
                    Log.e("BEARING:", String.valueOf(location.getBearing()));
                }
            }
        });

        colors = colors_temp;
        shimmershow();

        return getView;
    }


    private void shimmershow() {
        rvCategory.setVisibility(View.GONE);
        rvreview.setVisibility(View.GONE);
        rvberita.setVisibility(View.GONE);
        rvmerchant.setVisibility(View.GONE);
        rvmerchantnear.setVisibility(View.GONE);
        rvcatmerchantpromo.setVisibility(View.GONE);
        shimmerchantpromo.startShimmerAnimation();
        getShimmerchantnear.startShimmerAnimation();
        shimberita.startShimmerAnimation();
        mShimmerCat.startShimmerAnimation();
        shimerreview.startShimmerAnimation();
        shimerPromo.startShimmerAnimation();
        saldo.setVisibility(View.GONE);
    }

    private void shimmertutup() {
        rvreview.setVisibility(View.VISIBLE);
        rvCategory.setVisibility(View.VISIBLE);
        rvberita.setVisibility(View.VISIBLE);
        rvmerchant.setVisibility(View.VISIBLE);
        rvcatmerchantpromo.setVisibility(View.VISIBLE);
        rvcatmerchantnear.setVisibility(View.VISIBLE);
        rvmerchantnear.setVisibility(View.VISIBLE);
        shimberita.stopShimmerAnimation();
        shimberita.setVisibility(View.GONE);
        shimmerchantpromo.stopShimmerAnimation();
        shimmerchantpromo.setVisibility(View.GONE);
        mShimmerCat.setVisibility(View.GONE);
        mShimmerCat.stopShimmerAnimation();
        shimerreview.setVisibility(View.GONE);
        shimerreview.stopShimmerAnimation();
        shimerPromo.setVisibility(View.GONE);
        shimerPromo.stopShimmerAnimation();
        getShimmerchantnear.stopShimmerAnimation();
        getShimmerchantnear.setVisibility(View.GONE);

        saldo.setVisibility(View.VISIBLE);
    }

    private void gethome(final Location location) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetHomeRequestJson param = new GetHomeRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        userService.home(param).enqueue(new Callback<GetHomeResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<GetHomeResponseJson> call, @NonNull Response<GetHomeResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        shimmertutup();
                        PayuModel payu = response.body().getPayu().get(0);
                        sp.updateCurrency(response.body().getCurrency());
                        sp.updateabout(response.body().getAboutus());
                        sp.updateemail(response.body().getEmail());
                        sp.updatephone(response.body().getPhone());
                        sp.updateweb(response.body().getWebsite());
                        sp.updatePaypal(response.body().getPaypalkey());
                        sp.updatepaypalmode(response.body().getPaypalmode());
                        sp.updatepaypalactive(response.body().getPaypalactive());
                        sp.updatestripeactive(response.body().getStripeactive());
                        sp.updatecurrencytext(response.body().getCurrencytext());
                        sp.updatePayudebug(payu.getPayudebug());
                        sp.updatePayumerchantid(payu.getPayuid());
                        sp.updatePayusalt(payu.getPayusalt());
                        sp.updatePayumerchantkey(payu.getPayukey());
                        sp.updatePayuActive(payu.getActive());

                        Utility.currencyTXT(saldo, response.body().getSaldo(), context);

                        if (response.body().getSlider().isEmpty()) {
                            llslider.setVisibility(View.GONE);
                        } else {
                            promoslider.setVisibility(View.VISIBLE);
                            adapter = new SliderItem(response.body().getSlider(), getActivity());
                            viewPager.setAdapter(adapter);
                            circleIndicator.setViewPager(viewPager);
                            viewPager.setPadding(50, 0, 50, 0);
                        }
                        fiturdata = response.body().getFitur();
                        allfiturdata = response.body().getAllfitur();
                        for (int i = 0; i < fiturdata.size(); i++) {
                            FiturDataModel fiturmodel = new FiturDataModel();
                            fiturmodel.setIdFitur(fiturdata.get(i).getIdFitur());
                            fiturmodel.setFitur(fiturdata.get(i).getFitur());
                            fiturmodel.setIcon(fiturdata.get(i).getIcon());
                            fiturmodel.setHome(fiturdata.get(i).getHome());
                            fiturlist.add(fiturmodel);
                        }

                        if (fiturdata.size() > 6) {
                            FiturDataModel fiturmodel = new FiturDataModel();
                            fiturmodel.setIdFitur(100);
                            fiturmodel.setFitur("More");
                            fiturmodel.setHome("0");
                            fiturlist.add(fiturmodel);
                        }

                        fiturItem = new FiturItem(getActivity(), fiturlist, R.layout.item_fitur, new FiturItem.OnItemClickListener() {
                            @Override
                            public void onItemClick(FiturDataModel item) {
                                sheetlist();
                            }
                        });
                        rvCategory.setAdapter(fiturItem);
                        if (response.body().getRating().isEmpty()) {
                            llrating.setVisibility(View.GONE);
                        } else {

                            ratingItem = new RatingItem(response.body().getRating(), context);
                            rvreview.setAdapter(ratingItem);
                            circleIndicatorreview.setViewPager(rvreview);
                            rvreview.setPadding(50, 0, 50, 0);
                        }
                        if (response.body().getBerita().isEmpty()) {
                            llberita.setVisibility(View.GONE);
                        } else {
                            beritaItem = new BeritaItem(getActivity(), response.body().getBerita(), R.layout.item_grid);
                            rvberita.setAdapter(beritaItem);
                        }


                        if (response.body().getMerchantpromo().isEmpty()) {
                            llmerchant.setVisibility(View.GONE);
                        } else {

                            click = response.body().getMerchantpromo();
                            merchantItem = new MerchantItem(getActivity(), click, R.layout.item_merchant);
                            rvmerchant.setAdapter(merchantItem);
                            catMerchantItem = new CatMerchantItem(getActivity(), response.body().getCatmerchant(), R.layout.item_cat_merchant, new CatMerchantItem.OnItemClickListener() {
                                @Override
                                public void onItemClick(final CatMerchantModel item) {

                                    click.clear();
                                    shimlistpromo.setVisibility(View.VISIBLE);
                                    shimmerchantpromo.setVisibility(View.VISIBLE);
                                    shimlistcatpromo.setVisibility(View.GONE);
                                    rvmerchant.setVisibility(View.GONE);
                                    nodatapromo.setVisibility(View.GONE);
                                    shimmerchantpromo.startShimmerAnimation();
                                    FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(context);
                                    mFusedLocation.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                getmerchntbycatpromo(location, item.getId_kategori_merchant());
                                            }
                                        }
                                    });

                                }
                            });
                            rvcatmerchantpromo.setAdapter(catMerchantItem);

                        }

                        if (response.body().getMerchantnear().isEmpty()) {
                            llmerchantnear.setVisibility(View.GONE);
                        } else {
                            clicknear = response.body().getMerchantnear();
                            merchantNearItem = new MerchantNearItem(getActivity(), clicknear, R.layout.item_merchant);
                            rvmerchantnear.setAdapter(merchantNearItem);

                            catMerchantNearItem = new CatMerchantNearItem(getActivity(), response.body().getCatmerchant(), R.layout.item_cat_merchant, new CatMerchantNearItem.OnItemClickListener() {
                                @Override
                                public void onItemClick(final CatMerchantModel item) {
                                    clicknear.clear();
                                    shimlistnear.setVisibility(View.VISIBLE);
                                    getShimmerchantnear.setVisibility(View.VISIBLE);
                                    shimlistcatnear.setVisibility(View.GONE);
                                    rvmerchantnear.setVisibility(View.GONE);
                                    nodatanear.setVisibility(View.GONE);
                                    getShimmerchantnear.startShimmerAnimation();
                                    FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(context);
                                    mFusedLocation.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                getmerchntbycatnear(location, item.getId_kategori_merchant());
                                            }
                                        }
                                    });
                                }
                            });

                            rvcatmerchantnear.setAdapter(catMerchantNearItem);
                        }
                        User user = response.body().getData().get(0);
                        saveUser(user);
                        if (HomeFragment.this.getActivity() != null) {
                            Realm realm = BaseApp.getInstance(HomeFragment.this.getActivity()).getRealmInstance();
                            User loginUser = BaseApp.getInstance(HomeFragment.this.getActivity()).getLoginUser();
                            realm.beginTransaction();
                            loginUser.setWalletSaldo(Long.parseLong(response.body().getSaldo()));
                            realm.commitTransaction();
                        }
                    } else {
                        Realm realm = BaseApp.getInstance(context).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
                        BaseApp.getInstance(context).setLoginUser(null);
                        startActivity(new Intent(context, IntroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        requireActivity().finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetHomeResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void getmerchntbycatpromo(final Location location, String cat) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetMerchantbyCatRequestJson param = new GetMerchantbyCatRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        param.setKategori(cat);
        userService.getmerchanbycat(param).enqueue(new Callback<MerchantByCatResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<MerchantByCatResponseJson> call, @NonNull Response<MerchantByCatResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        click = response.body().getData();
                        shimmerchantpromo.setVisibility(View.GONE);
                        rvmerchant.setVisibility(View.VISIBLE);
                        shimmerchantpromo.stopShimmerAnimation();
                        if (response.body().getData().isEmpty()) {
                            nodatapromo.setVisibility(View.VISIBLE);
                            rvmerchant.setVisibility(View.GONE);
                        } else {
                            nodatapromo.setVisibility(View.GONE);
                            merchantItem = new MerchantItem(getActivity(), click, R.layout.item_merchant);
                            rvmerchant.setAdapter(merchantItem);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MerchantByCatResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void getmerchntbycatnear(final Location location, String cat) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetMerchantbyCatRequestJson param = new GetMerchantbyCatRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        param.setKategori(cat);
        userService.getmerchanbynear(param).enqueue(new Callback<MerchantByNearResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<MerchantByNearResponseJson> call, @NonNull Response<MerchantByNearResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        clicknear = response.body().getData();
                        getShimmerchantnear.setVisibility(View.GONE);
                        rvmerchantnear.setVisibility(View.VISIBLE);
                        getShimmerchantnear.stopShimmerAnimation();
                        if (response.body().getData().isEmpty()) {
                            nodatanear.setVisibility(View.VISIBLE);
                            rvmerchantnear.setVisibility(View.GONE);
                        } else {
                            nodatanear.setVisibility(View.GONE);
                            merchantNearItem = new MerchantNearItem(getActivity(), clicknear, R.layout.item_merchant);
                            rvmerchantnear.setAdapter(merchantNearItem);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MerchantByNearResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        Utility.currencyTXT(saldo, String.valueOf(loginUser.getWalletSaldo()), context);

    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(context).setLoginUser(user);
    }

    private void sheetlist() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        @SuppressLint("InflateParams") final View mDialog = getLayoutInflater().inflate(R.layout.sheet_category, null);
        RecyclerView view = mDialog.findViewById(R.id.category);

        view.setHasFixedSize(true);
        view.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        AllFiturItem allfiturItem = new AllFiturItem(getActivity(), allfiturdata, R.layout.item_fitur);
        view.setAdapter(allfiturItem);

        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(mDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(mBottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }


}
