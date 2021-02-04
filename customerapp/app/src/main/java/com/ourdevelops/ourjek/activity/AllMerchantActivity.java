package com.ourdevelops.ourjek.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.constants.BaseApp;
import com.ourdevelops.ourjek.item.AllMerchantNearItem;
import com.ourdevelops.ourjek.item.CatMerchantNearItem;
import com.ourdevelops.ourjek.json.AllMerchantByNearResponseJson;
import com.ourdevelops.ourjek.json.AllMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.GetAllMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.json.SearchMerchantbyCatRequestJson;
import com.ourdevelops.ourjek.models.CatMerchantModel;
import com.ourdevelops.ourjek.models.MerchantNearModel;
import com.ourdevelops.ourjek.models.User;
import com.ourdevelops.ourjek.utils.api.MapDirectionAPI;
import com.ourdevelops.ourjek.utils.api.ServiceGenerator;
import com.ourdevelops.ourjek.utils.api.service.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AllMerchantActivity extends AppCompatActivity {
    ImageView backbtn;
    TextView address;
    EditText search;
    ShimmerFrameLayout shimmerchantnear;
    RecyclerView rvcatmerchantnear, rvmerchantnear;
    AllMerchantNearItem merchantNearItem;
    CatMerchantNearItem catMerchantNearItem;
    List<MerchantNearModel> clicknear;
    LinearLayout llmerchantnear, shimlistnear, shimlistcatnear;
    RelativeLayout nodatanear;
    int fiturId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_merchant);

        Intent intent = getIntent();
        fiturId = intent.getIntExtra("FiturKey", -1);

        backbtn = findViewById(R.id.Goback);
        address = findViewById(R.id.address);
        shimmerchantnear = findViewById(R.id.shimmerchantnear);
        rvcatmerchantnear = findViewById(R.id.catmerchantnear);
        rvmerchantnear = findViewById(R.id.merchantnear);
        llmerchantnear = findViewById(R.id.llmerchantnear);
        shimlistnear = findViewById(R.id.shimlistnear);
        shimlistcatnear = findViewById(R.id.shimlistcatnear);
        nodatanear = findViewById(R.id.rlnodata);
        search = findViewById(R.id.searchtext);


        address.setSelected(true);
        rvcatmerchantnear.setHasFixedSize(true);
        rvcatmerchantnear.setNestedScrollingEnabled(false);
        rvcatmerchantnear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    LatLng centerPos = new LatLng(location.getLatitude(), location.getLongitude());
                    requestAddress(centerPos, address);
                    getdata(location);
                    search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (clicknear != null) {
                                clicknear.clear();
                            }
                            shimlistnear.setVisibility(View.VISIBLE);
                            shimmerchantnear.setVisibility(View.VISIBLE);
                            shimlistcatnear.setVisibility(View.GONE);
                            rvmerchantnear.setVisibility(View.GONE);
                            nodatanear.setVisibility(View.GONE);
                            shimmerchantnear.startShimmerAnimation();
                            String sSearch = search.getText().toString().trim();
                            if (TextUtils.isEmpty(sSearch)) {
                                getmerchntbycatnear(location, "1");
                            } else {
                                searchmerchant(location, search.getText().toString());
                            }
                            return false;
                        }

                    });
                }
            }
        });

        shimmershow();
    }

    private void shimmershow() {
        rvmerchantnear.setVisibility(View.GONE);
        shimmerchantnear.startShimmerAnimation();
    }

    private void shimmertutup() {
        rvcatmerchantnear.setVisibility(View.VISIBLE);
        rvmerchantnear.setVisibility(View.VISIBLE);
        shimmerchantnear.stopShimmerAnimation();
        shimmerchantnear.setVisibility(View.GONE);
    }

    private void requestAddress(LatLng latlang, final TextView textView) {
        if (latlang != null) {
            MapDirectionAPI.getAddress(latlang).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull final okhttp3.Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String json = Objects.requireNonNull(response.body()).string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject Jobject = new JSONObject(json);
                                    JSONArray Jarray = Jobject.getJSONArray("results");
                                    JSONObject userdata = Jarray.getJSONObject(0);
                                    String address = userdata.getString("formatted_address");
                                    textView.setText(address);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void getdata(final Location location) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AllMerchantbyCatRequestJson param = new AllMerchantbyCatRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        param.setKategori(String.valueOf(fiturId));
        userService.allmerchant(param).enqueue(new Callback<AllMerchantByNearResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AllMerchantByNearResponseJson> call, @NonNull Response<AllMerchantByNearResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        shimmertutup();

                        if (response.body().getData().isEmpty()) {
                            rvmerchantnear.setVisibility(View.GONE);
                            nodatanear.setVisibility(View.VISIBLE);
                        } else {
                            clicknear = response.body().getData();
                            merchantNearItem = new AllMerchantNearItem(AllMerchantActivity.this, clicknear, R.layout.item_merchant_list);
                            rvmerchantnear.setAdapter(merchantNearItem);

                            catMerchantNearItem = new CatMerchantNearItem(AllMerchantActivity.this, response.body().getKategori(), R.layout.item_cat_merchant, new CatMerchantNearItem.OnItemClickListener() {
                                @Override
                                public void onItemClick(final CatMerchantModel item) {
                                    clicknear.clear();
                                    shimlistnear.setVisibility(View.VISIBLE);
                                    shimmerchantnear.setVisibility(View.VISIBLE);
                                    shimlistcatnear.setVisibility(View.GONE);
                                    rvmerchantnear.setVisibility(View.GONE);
                                    nodatanear.setVisibility(View.GONE);
                                    shimmerchantnear.startShimmerAnimation();
                                    FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(AllMerchantActivity.this);
                                    mFusedLocation.getLastLocation().addOnSuccessListener(AllMerchantActivity.this, new OnSuccessListener<Location>() {
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
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllMerchantByNearResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void getmerchntbycatnear(final Location location, String cat) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetAllMerchantbyCatRequestJson param = new GetAllMerchantbyCatRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        param.setKategori(cat);
        param.setFitur(String.valueOf(fiturId));
        userService.getallmerchanbynear(param).enqueue(new Callback<AllMerchantByNearResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AllMerchantByNearResponseJson> call, @NonNull Response<AllMerchantByNearResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        clicknear = response.body().getData();
                        shimmerchantnear.setVisibility(View.GONE);
                        rvmerchantnear.setVisibility(View.VISIBLE);
                        shimmerchantnear.stopShimmerAnimation();
                        if (response.body().getData().isEmpty()) {
                            nodatanear.setVisibility(View.VISIBLE);
                        } else {
                            nodatanear.setVisibility(View.GONE);
                            merchantNearItem = new AllMerchantNearItem(AllMerchantActivity.this, clicknear, R.layout.item_merchant_list);
                            rvmerchantnear.setAdapter(merchantNearItem);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllMerchantByNearResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void searchmerchant(final Location location, String like) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        UserService userService = ServiceGenerator.createService(
                UserService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        SearchMerchantbyCatRequestJson param = new SearchMerchantbyCatRequestJson();
        param.setId(loginUser.getId());
        param.setLat(String.valueOf(location.getLatitude()));
        param.setLon(String.valueOf(location.getLongitude()));
        param.setPhone(loginUser.getNoTelepon());
        param.setFitur(String.valueOf(fiturId));
        param.setLike(like);
        userService.searchmerchant(param).enqueue(new Callback<AllMerchantByNearResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AllMerchantByNearResponseJson> call, @NonNull Response<AllMerchantByNearResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        clicknear = response.body().getData();
                        shimmerchantnear.setVisibility(View.GONE);
                        rvmerchantnear.setVisibility(View.VISIBLE);
                        shimmerchantnear.stopShimmerAnimation();
                        if (response.body().getData().isEmpty()) {
                            nodatanear.setVisibility(View.VISIBLE);
                        } else {
                            nodatanear.setVisibility(View.GONE);
                            merchantNearItem = new AllMerchantNearItem(AllMerchantActivity.this, clicknear, R.layout.item_merchant_list);
                            rvmerchantnear.setAdapter(merchantNearItem);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllMerchantByNearResponseJson> call, @NonNull Throwable t) {

            }
        });
    }
}
