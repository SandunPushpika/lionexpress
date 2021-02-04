package com.ourdevelops.ourjek.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import com.ourdevelops.ourjek.activity.MainActivity;
import com.ourdevelops.ourjek.constants.BaseApp;
import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.activity.IntroActivity;
import com.ourdevelops.ourjek.constants.Constants;
import com.ourdevelops.ourjek.models.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class EnableLlocationFragment extends Fragment {


    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_enablelocation, container, false);
        context = getContext();


        sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);

        Button enableLocation = getView.findViewById(R.id.enable_location_btn);
        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();

            }
        });


        return getView;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            Animation anim = MoveAnimation.create(MoveAnimation.LEFT, true, 300);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    GPSStatus();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            return anim;

        } else {
            return MoveAnimation.create(MoveAnimation.RIGHT, false, 300);
        }
    }


    private void getLocationPermission() {

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetCurrentlocation();
            } else {
                Toast.makeText(context, "Please Grant permission", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void GPSStatus() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus) {
            Toast.makeText(context, "On Location in High Accuracy", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2);
        } else {
            GetCurrentlocation();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            GPSStatus();
        }
    }

    private void GetCurrentlocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            getLocationPermission();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(String.valueOf(Constants.LATITUDE), "" + location.getLatitude());
                            editor.putString(String.valueOf(Constants.LONGITUDE), "" + location.getLongitude());
                            editor.apply();
                            Constants.LATITUDE = location.getLatitude();
                            Constants.LONGITUDE = location.getLongitude();
                            try {
                                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    addresses.size();
                                    String address = addresses.get(0).getAddressLine(0);
                                    Constants.LOCATION = String.valueOf(address);
                                } else {
                                    Constants.LOCATION = "not Availeble";
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            GoToNext_Activty();

                        } else {

                            if (sharedPreferences.getString(String.valueOf(Constants.LATITUDE), "").equals("") || sharedPreferences.getString(String.valueOf(Constants.LONGITUDE), "").equals("")) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(String.valueOf(Constants.LATITUDE), "33.738045");
                                editor.putString(String.valueOf(Constants.LONGITUDE), "73.084488");
                                editor.apply();

                            }

                            GoToNext_Activty();
                        }
                    }
                });
    }


    private void GoToNext_Activty() {
        final User user = BaseApp.getInstance(context).getLoginUser();
        if (user != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        } else {
            Intent intent = new Intent(getActivity(), IntroActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }

    }


}

