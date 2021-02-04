package com.ourdevelops.ourjek.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.constants.BaseApp;
import com.ourdevelops.ourjek.constants.Constants;
import com.ourdevelops.ourjek.constants.VersionChecker;
import com.ourdevelops.ourjek.fragment.FavouriteFragment;
import com.ourdevelops.ourjek.fragment.HistoryFragment;
import com.ourdevelops.ourjek.fragment.HomeFragment;
import com.ourdevelops.ourjek.fragment.MessageFragment;
import com.ourdevelops.ourjek.fragment.ProfileFragment;
import com.ourdevelops.ourjek.models.User;
import com.ourdevelops.ourjek.utils.api.ServiceGenerator;
import com.ourdevelops.ourjek.utils.api.service.UserService;
import com.ourdevelops.ourjek.models.FiturModel;
import com.ourdevelops.ourjek.json.GetFiturResponseJson;


import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    long mBackPressed;


    public static String apikey;

    LinearLayout mAdViewLayout;

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;
    private FragmentManager fragmentManager;
    BottomNavigationView navigation;
    int previousSelect = 0;


    public static MainActivity getInstance() {
        return mainActivity;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Menu menu = navigation.getMenu();
            menu.findItem(R.id.home).setIcon(R.drawable.ic_home);
            menu.findItem(R.id.order).setIcon(R.drawable.ic_transaksi);
            menu.findItem(R.id.favourite).setIcon(R.drawable.ic_favourites);
            menu.findItem(R.id.chat).setIcon(R.drawable.ic_pesan);
            menu.findItem(R.id.profile).setIcon(R.drawable.ic_profil);
            switch (item.getItemId()) {
                case R.id.home:
                    HomeFragment homeFragment = new HomeFragment();
                    navigationItemSelected(0);
                    item.setIcon(R.drawable.ic_home_s);
                    loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                    return true;
                case R.id.order:
                    HistoryFragment listFragment = new HistoryFragment();
                    navigationItemSelected(1);
                    item.setIcon(R.drawable.ic_transaksi_s);
                    loadFrag(listFragment, getString(R.string.menu_home), fragmentManager);
                    return true;
                case R.id.favourite:
                    FavouriteFragment favFragment = new FavouriteFragment();
                    navigationItemSelected(1);
                    item.setIcon(R.drawable.ic_favourite);
                    loadFrag(favFragment, getString(R.string.menu_favourite), fragmentManager);
                    return true;
                case R.id.chat:
                    MessageFragment pesanFragment = new MessageFragment();
                    navigationItemSelected(2);
                    item.setIcon(R.drawable.ic_pesan_s);
                    loadFrag(pesanFragment, getString(R.string.menu_home), fragmentManager);
                    return true;
                case R.id.profile:
                    ProfileFragment profilFragment = new ProfileFragment();
                    navigationItemSelected(3);
                    item.setIcon(R.drawable.ic_profil_s);
                    loadFrag(profilFragment, getString(R.string.menu_home), fragmentManager);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdViewLayout = findViewById(R.id.adView);
        fragmentManager = getSupportFragmentManager();
        navigation = findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        Menu menu = navigation.getMenu();
        menu.findItem(R.id.home).setIcon(R.drawable.ic_home_s);
        HomeFragment homeFragment = new HomeFragment();
        loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        Constants.TOKEN = loginUser.getToken();
        Constants.USERID = loginUser.getId();
        apikey = getString(R.string.google_maps_key);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Constants.versionname = Objects.requireNonNull(packageInfo).versionName;


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Check_version();
        update();
    }

    public void Check_version() {
        VersionChecker versionChecker = new VersionChecker(this);
        versionChecker.execute();
    }

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                clickDone();

            }
        } else {
            super.onBackPressed();
        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this, R.style.DialogStyle)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.Container, f1, name);
        ft.commit();
    }

    public void navigationItemSelected(int position) {
        previousSelect = position;
    }

    private void update() {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        UserService userService = ServiceGenerator.createService(UserService.class,
                loginUser.getEmail(), loginUser.getPassword());
        userService.getFitur().enqueue(new Callback<GetFiturResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<GetFiturResponseJson> call, @NonNull Response<GetFiturResponseJson> response) {
                if (response.isSuccessful()) {
                    Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                    realm.beginTransaction();
                    realm.delete(FiturModel.class);
                    realm.copyToRealm(Objects.requireNonNull(response.body()).getData());
                    realm.commitTransaction();

                    Constants.CURRENCY = response.body().getCurrencyModel();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetFiturResponseJson> call, @NonNull Throwable t) {

            }
        });
    }


}
