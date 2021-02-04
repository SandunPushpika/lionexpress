package com.ourdevelops.ourjek.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.downloader.Progress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.constants.BaseApp;
import com.ourdevelops.ourjek.constants.Constants;
import com.ourdevelops.ourjek.gmap.directions.Directions;
import com.ourdevelops.ourjek.gmap.directions.Route;
import com.ourdevelops.ourjek.item.ItemPesananItem;
import com.ourdevelops.ourjek.json.DetailRequestJson;
import com.ourdevelops.ourjek.json.DetailTransResponseJson;
import com.ourdevelops.ourjek.json.LokasiDriverRequest;
import com.ourdevelops.ourjek.json.LokasiDriverResponse;
import com.ourdevelops.ourjek.json.fcm.CancelBookRequestJson;
import com.ourdevelops.ourjek.json.fcm.CancelBookResponseJson;
import com.ourdevelops.ourjek.json.fcm.DriverResponse;
import com.ourdevelops.ourjek.json.fcm.FCMMessage;
import com.ourdevelops.ourjek.models.DriverModel;
import com.ourdevelops.ourjek.models.FiturModel;
import com.ourdevelops.ourjek.models.LokasiDriverModel;
import com.ourdevelops.ourjek.models.TransaksiModel;
import com.ourdevelops.ourjek.models.User;
import com.ourdevelops.ourjek.utils.NetworkManager;
import com.ourdevelops.ourjek.utils.PicassoTrustAll;
import com.ourdevelops.ourjek.utils.Utility;
import com.ourdevelops.ourjek.utils.api.FCMHelper;
import com.ourdevelops.ourjek.utils.api.MapDirectionAPI;
import com.ourdevelops.ourjek.utils.api.ServiceGenerator;
import com.ourdevelops.ourjek.utils.api.service.BookService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ourdevelops.ourjek.json.fcm.FCMType.ORDER;
import static com.ourdevelops.ourjek.utils.MapsUtils.getBearing;
import static com.ourdevelops.ourjek.utils.api.service.MessagingService.BROADCAST_ORDER;

/**
 * Created by Ourdevelops Team on 10/26/2019.
 */

public class ProgressActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap gMap;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int REQUEST_PERMISSION_CALL = 992;
    private GoogleApiClient googleApiClient;
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private Marker driverMarker;
    Bundle orderBundle;
    private boolean isCancelable = true;
    String idtrans, iddriver, response;
    String regdriver, fitur, imagedriver,tokenmerchant;
    private int markerCount;
    String latdriver = "";
    String londriver = "";
    String complete,icondriver,gethome;
    private Handler handler;
    Timer timer = new Timer();
    private Runnable updateDriverRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LokasiDriverRequest param = new LokasiDriverRequest();
                        final BookService service = ServiceGenerator.createService(BookService.class, "admin", "12345");
                        param.setId(iddriver);
                        service.liatLokasiDriver(param).enqueue(new Callback<LokasiDriverResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<LokasiDriverResponse> call, @NonNull Response<LokasiDriverResponse> response) {
                                if (response.isSuccessful()) {
                                    final LokasiDriverModel latlang = Objects.requireNonNull(response.body()).getData().get(0);
                                    final LatLng location = new LatLng(latlang.getLatitude(), latlang.getLongitude());
                                    updateDriverMarker(location);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<LokasiDriverResponse> call, @NonNull Throwable t) {


                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (NetworkManager.isConnectToInternet(ProgressActivity.this)) {
                                try {
                                    LokasiDriverRequest param = new LokasiDriverRequest();
                                    final BookService service = ServiceGenerator.createService(BookService.class, "admin", "12345");
                                    param.setId(iddriver);
                                    service.liatLokasiDriver(param).enqueue(new Callback<LokasiDriverResponse>() {
                                        @Override
                                        public void onResponse(@NonNull Call<LokasiDriverResponse> call, @NonNull Response<LokasiDriverResponse> response) {
                                            if (response.isSuccessful()) {
                                                final LokasiDriverModel latlang = Objects.requireNonNull(response.body()).getData().get(0);
                                                final LatLng location = new LatLng(latlang.getLatitude(), latlang.getLongitude());
                                                updateDriverMarker(location);
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<LokasiDriverResponse> call, @NonNull Throwable t) {


                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 0, 4000);
                }
            }).start();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDriverLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDriverLocationUpdate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDriverLocationUpdate();
        timer.cancel();
            super.onBackPressed();
            if (complete.equals("1")) {
                Intent intent = new Intent(ProgressActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                finish();
            }
    }

    private void stopDriverLocationUpdate() {
        handler.removeCallbacks(updateDriverRunnable);

    }

    TextView textprogress, status,time,costtext;
    ImageView phone, chat;
    TextView produk, sendername, receivername;
    Button senderphone, receiverphone;
    TextView textnotif, priceText;
    TextView diskon, cost, distanceText, fiturtext, destinationText, pickUpText;
    ImageView foto, image, backbtn;
    TextView layanandesk, layanan,totaltext;
    LinearLayout llpayment, bottomsheet, setDestinationContainer, setPickUpContainer, llchat, lldestination, lldistance, lldetailsend, llmerchantdetail;
    RelativeLayout rlnotif, rlprogress;
    Button orderButton;
    RecyclerView rvmerchantnear;
    ItemPesananItem itemPesananItem;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        realm = Realm.getDefaultInstance();
        handler = new Handler();
        fitur = "0";
        icondriver = "0";
        gethome = "0";
        Intent intent = getIntent();
        iddriver = intent.getStringExtra("id_driver");
        idtrans = intent.getStringExtra("id_transaksi");
        response = intent.getStringExtra("response");
        if (intent.getStringExtra("complete") == null) {
            complete = "false";
        } else {
            complete = intent.getStringExtra("complete");
        }
        receiverphone = findViewById(R.id.receiverphone);
        senderphone = findViewById(R.id.senderphone);
        receivername = findViewById(R.id.receivername);
        sendername = findViewById(R.id.sendername);
        produk = findViewById(R.id.produk);
        lldetailsend = findViewById(R.id.senddetail);
        lldistance = findViewById(R.id.lldistance);
        lldestination = findViewById(R.id.lldestination);
        status = findViewById(R.id.status);
        chat = findViewById(R.id.chat);
        phone = findViewById(R.id.phonenumber);
        setPickUpContainer = findViewById(R.id.pickUpContainer);
        setDestinationContainer = findViewById(R.id.destinationContainer);
        bottomsheet = findViewById(R.id.bottom_sheet);
        llpayment = findViewById(R.id.llpayment);
        layanan = findViewById(R.id.layanan);
        layanandesk = findViewById(R.id.layanandes);
        backbtn = findViewById(R.id.back_btn);
        llchat = findViewById(R.id.llchat);
        image = findViewById(R.id.image);
        foto = findViewById(R.id.background);
        pickUpText = findViewById(R.id.pickUpText);
        destinationText = findViewById(R.id.destinationText);
        fiturtext = findViewById(R.id.fitur);
        distanceText = findViewById(R.id.distance);
        cost = findViewById(R.id.cost);
        diskon = findViewById(R.id.diskon);
        priceText = findViewById(R.id.price);
        orderButton = findViewById(R.id.order);
        rlnotif = findViewById(R.id.rlnotif);
        textnotif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        textprogress = findViewById(R.id.textprogress);
        llmerchantdetail = findViewById(R.id.merchantdetail);
        time = findViewById(R.id.time);
        costtext = findViewById(R.id.cost_text);
        rvmerchantnear = findViewById(R.id.merchantnear);
        totaltext = findViewById(R.id.totaltext);

        status.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        setPickUpContainer.setVisibility(View.GONE);
        setDestinationContainer.setVisibility(View.GONE);
        llpayment.setVisibility(View.GONE);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        orderButton.setText(getString(R.string.text_cancel));
        orderButton.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_red));

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgressActivity.this, R.style.DialogStyle);
                    alertDialogBuilder.setTitle("Cancel order");
                    alertDialogBuilder.setMessage("Do you want to cancel this order?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancelOrder();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    notif("You cannot cancel the order, the trip has already begun!");
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        rlprogress.setVisibility(View.VISIBLE);
        textprogress.setText(getString(R.string.waiting_pleaseWait));

    }

    private void getData(final String idtrans, final String iddriver) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdDriver(iddriver);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<DetailTransResponseJson> call, @NonNull Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {

                    final TransaksiModel transaksi = Objects.requireNonNull(responsedata.body()).getData().get(0);
                    FiturModel designedFitur = realm.where(FiturModel.class).equalTo("idFitur", Integer.valueOf(transaksi.getOrderFitur())).findFirst();
                    icondriver = Objects.requireNonNull(designedFitur).getIcon_driver();
                    gethome = designedFitur.getHome();
                    DriverModel driver = responsedata.body().getDriver().get(0);
                    parsedata(transaksi, driver, designedFitur);
                    regdriver = driver.getRegId();
                    tokenmerchant = transaksi.getToken_merchant();
                    imagedriver = Constants.IMAGESDRIVER + driver.getFoto();

                    if (transaksi.isPakaiWallet()) {
                        totaltext.setText("Total (Wallet)");
                    } else {
                        totaltext.setText("Total (Cash)");
                    }
                    if (pickUpMarker != null) pickUpMarker.remove();
                    pickUpMarker = gMap.addMarker(new MarkerOptions()
                            .position(pickUpLatLng)
                            .title("Pick Up")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));


                    if (destinationMarker != null) destinationMarker.remove();
                    destinationMarker = gMap.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title("Destination")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
                    updateLastLocation();

                    fitur = transaksi.getOrderFitur();

                    if (!designedFitur.getHome().equals("3")) {
                        if (designedFitur.getHome().equals("4")) {
                            llmerchantdetail.setVisibility(View.VISIBLE);
                            Utility.currencyTXT(fiturtext,String.valueOf(transaksi.getHarga()),ProgressActivity.this);
                            lldistance.setVisibility(View.GONE);
                            time.setText("Delivery Fee");
                            costtext.setText("Order Cost");

                            itemPesananItem = new ItemPesananItem(responsedata.body().getItem(), R.layout.item_pesanan);
                            rvmerchantnear.setAdapter(itemPesananItem);

                        } else if (designedFitur.getHome().equals("2")) {
                            requestRoute();
                            lldetailsend.setVisibility(View.VISIBLE);
                            produk.setText(transaksi.getNamaBarang());
                            sendername.setText(transaksi.namaPengirim);
                            receivername.setText(transaksi.namaPenerima);

                            senderphone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgressActivity.this, R.style.DialogStyle);
                                    alertDialogBuilder.setTitle("Call Driver");
                                    alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    if (ActivityCompat.checkSelfPermission(ProgressActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(ProgressActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                        return;
                                                    }

                                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                    callIntent.setData(Uri.parse("tel:" + transaksi.teleponPengirim));
                                                    startActivity(callIntent);
                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                }
                            });

                            receiverphone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgressActivity.this, R.style.DialogStyle);
                                    alertDialogBuilder.setTitle("Call Driver");
                                    alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    if (ActivityCompat.checkSelfPermission(ProgressActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(ProgressActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                        return;
                                                    }

                                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                    callIntent.setData(Uri.parse("tel:" + transaksi.teleponPenerima));
                                                    startActivity(callIntent);
                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                }
                            });

                        } else {
                            requestRoute();
                        }
                    } else {
                        lldestination.setVisibility(View.GONE);
                        lldistance.setVisibility(View.GONE);
                        fiturtext.setText(transaksi.getEstimasi());
                    }
                    if (transaksi.status == 4 && transaksi.getRate().isEmpty()) {
                        Intent intent = new Intent(ProgressActivity.this, RateActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("id_driver", iddriver);
                        intent.putExtra("id_transaksi", idtrans);
                        intent.putExtra("response", response);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<DetailTransResponseJson> call, @NonNull Throwable t) {

            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void parsedata(TransaksiModel request, final DriverModel driver, FiturModel fiturmodel) {
        final User loginUser = BaseApp.getInstance(ProgressActivity.this).getLoginUser();
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());

        PicassoTrustAll.getInstance(this)
                .load(Constants.IMAGESDRIVER + driver.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(foto);

        layanandesk.setText(driver.getNomor_kendaraan()+" "+getString(R.string.text_with_bullet)+" "+driver.getTipe());

        if (!response.equals("2")) {
            if (!response.equals("3")) {
                if (response.equals("4")) {
                    isCancelable = false;
                    llchat.setVisibility(View.GONE);
                    orderButton.setVisibility(View.GONE);
                    status.setText(getString(R.string.notification_finish));
                } else if (response.equals("5")) {
                    isCancelable = false;
                    llchat.setVisibility(View.GONE);
                    orderButton.setVisibility(View.GONE);
                    status.setText(getString(R.string.notification_cancel));
                }
            } else {
                llchat.setVisibility(View.VISIBLE);
                isCancelable = false;
                orderButton.setVisibility(View.GONE);
                if (fiturmodel.getHome().equals("4")) {
                    status.setText("driver delivers the order");
                } else {
                    status.setText(getString(R.string.notification_start));
                }
            }
        } else {
            llchat.setVisibility(View.VISIBLE);
            if (fiturmodel.getHome().equals("4")) {
                status.setText("the driver is buying an order");
            } else {
                status.setText(getString(R.string.notification_accept));
            }
        }
        layanan.setText(driver.getNamaDriver());
        if (fiturmodel.getHome().equals("4")) {
            pickUpText.setText(request.getNama_merchant());
            Utility.currencyTXT(cost, String.valueOf(request.getTotal_biaya()), this);
        } else {
            pickUpText.setText(request.getAlamatAsal());
            Utility.currencyTXT(cost, String.valueOf(request.getHarga()), this);
        }
        destinationText.setText(request.getAlamatTujuan());

        Utility.currencyTXT(diskon, request.getKreditPromo(), this);
        Utility.currencyTXT(priceText, String.valueOf(request.getBiaya_akhir()), this);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProgressActivity.this, R.style.DialogStyle);
                alertDialogBuilder.setTitle("Call Driver");
                alertDialogBuilder.setMessage("You want to call driver (" + driver.getNoTelepon() + ")?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (ActivityCompat.checkSelfPermission(ProgressActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ProgressActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                    return;
                                }

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + driver.getNoTelepon()));
                                startActivity(callIntent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgressActivity.this, ChatActivity.class);
                intent.putExtra("senderid", loginUser.getId());
                intent.putExtra("receiverid", driver.getId());
                intent.putExtra("tokendriver", loginUser.getToken());
                intent.putExtra("tokenku", driver.getRegId());
                intent.putExtra("name", driver.getNamaDriver());
                intent.putExtra("pic", Constants.IMAGESDRIVER+driver.getFoto());
                startActivity(intent);
            }
        });
    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).cancel(0);
    }

    private void cancelOrder() {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(ProgressActivity.this).getLoginUser();
        CancelBookRequestJson requestcancel = new CancelBookRequestJson();
        requestcancel.id = loginUser.getId();
        requestcancel.id_transaksi = idtrans;

        BookService service = ServiceGenerator.createService(BookService.class, loginUser.getEmail(), loginUser.getPassword());
        service.cancelOrder(requestcancel).enqueue(new Callback<CancelBookResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<CancelBookResponseJson> call, @NonNull Response<CancelBookResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).mesage.equals("canceled")) {
                        rlprogress.setVisibility(View.GONE);
                        fcmcancel();
                        fcmcancelmerchant();
                        notif("Order Canceled!");
                        finish();
                    } else {
                        notif("Failed!");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelBookResponseJson> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void fcmcancel() {
        DriverResponse response = new DriverResponse();
        response.type = ORDER;
        response.setIdTransaksi(idtrans);
        response.setResponse(DriverResponse.REJECT);

        FCMMessage message = new FCMMessage();
        message.setTo(regdriver);
        message.setData(response);


        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void fcmcancelmerchant() {
        DriverResponse response = new DriverResponse();
        response.type = ORDER;
        response.setIdTransaksi(idtrans);
        response.setResponse(String.valueOf(Constants.CANCEL));

        FCMMessage message = new FCMMessage();
        message.setTo(tokenmerchant);
        message.setData(response);


        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("", "Can't find style. Error: ", e);
        }
        getData(idtrans, iddriver);
    }

    private void startDriverLocationUpdate() {
        handler = new Handler();
        handler.postDelayed(updateDriverRunnable, 4000);
    }

    private void updateDriverMarker(LatLng latLng) {
        latdriver = String.valueOf(latLng.latitude);
        londriver = String.valueOf(latLng.longitude);
        Location mCurrentLocation = new Location(LocationManager.NETWORK_PROVIDER);
        mCurrentLocation.setLatitude(Double.parseDouble(latdriver));
        mCurrentLocation.setLongitude(Double.parseDouble(londriver));

        if (markerCount == 1) {
            animateMarker(mCurrentLocation, driverMarker);
        } else if (markerCount == 0) {
            if (driverMarker != null) driverMarker.remove();
            if (icondriver.equals("1")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drivermap)));
            } else if (icondriver.equals("2")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carmap)));
            } else if (icondriver.equals("3")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.truck)));
            } else if (icondriver.equals("4")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery)));
            } else if (icondriver.equals("5")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hatchback)));
            } else if (icondriver.equals("6")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.suv)));
            } else if (icondriver.equals("7")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.van)));
            } else if (icondriver.equals("8")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle)));
            } else if (icondriver.equals("9")) {
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bajaj)));
            }
            markerCount = 1;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }

    }

    public void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
            if (!startPosition.equals(endPosition)) {
                Log.e("lat", destination.getLatitude() + " " + destination.getLongitude());
                Log.e("destination", destination.getBearing() + "");

                final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(500); // duration 1 second
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        try {
                            float v = animation.getAnimatedFraction();
                            LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                            marker.setPosition(newPosition);
                            marker.setAnchor(0.5f, 0.5f);

                            marker.setRotation(getBearing(startPosition, newPosition));
                        } catch (Exception ex) {
                            // I don't care atm..
                        }
                    }
                });
                valueAnimator.start();
            }

        }
    }

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private okhttp3.Callback updateRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
                final String json = Objects.requireNonNull(response.body()).string();
                final long distance = MapDirectionAPI.getDistance(ProgressActivity.this, json);
                final String time = MapDirectionAPI.getTimeDistance(ProgressActivity.this, json);
                if (distance >= 0) {
                    ProgressActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateLineDestination(json);
                            float km = ((float) (distance)) / 1000f;
                            String format = String.format(Locale.US, "%.1f", km);
                            distanceText.setText(format);
                            fiturtext.setText(time);
                        }
                    });
                }
            }
        }
    };

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        gMap.setMyLocationEnabled(true);

        if (pickUpLatLng != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    pickUpLatLng, 15f)
            );

            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
    }

    private void requestRoute() {
        if (pickUpLatLng != null && destinationLatLng != null) {
            MapDirectionAPI.getDirection(pickUpLatLng, destinationLatLng).enqueue(updateRouteCallback);
        }
    }

    private void updateLineDestination(String json) {
        Directions directions = new Directions(ProgressActivity.this);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(ProgressActivity.this, R.color.colorgradient))
                        .width(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("SetTextI18n")
    private void orderHandler(int code) {
        switch (code) {
            case Constants.REJECT:
                isCancelable = false;
                orderButton.setVisibility(View.GONE);
                break;
            case Constants.CANCEL:
                isCancelable = false;
                orderButton.setVisibility(View.GONE);
                llchat.setVisibility(View.GONE);
                status.setText(getString(R.string.notification_cancel));
                break;
            case Constants.ACCEPT:
                llchat.setVisibility(View.VISIBLE);
                if (gethome.equals("4")) {
                    status.setText("the driver is buying an order");
                } else {
                    status.setText(getString(R.string.notification_accept));
                }
                break;
            case Constants.START:
                llchat.setVisibility(View.VISIBLE);
                isCancelable = false;
                orderButton.setVisibility(View.GONE);
                if (gethome.equals("4")) {
                    status.setText("driver delivers the order");
                } else {
                    status.setText(getString(R.string.notification_start));
                }
                break;
            case Constants.FINISH:
                isCancelable = false;
                llchat.setVisibility(View.GONE);
                orderButton.setVisibility(View.GONE);
                status.setText(getString(R.string.notification_finish));
                getData(idtrans, iddriver);

                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            orderBundle = intent.getExtras();
            orderHandler(Objects.requireNonNull(orderBundle).getInt("code"));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (response.equals("2") || response.equals("3") && !fitur.equals("0")) {
            startDriverLocationUpdate();
        }
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ORDER));
        removeNotif();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ORDER));
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
        super.onStop();

        unregisterReceiver(broadcastReceiver);
        if (response.equals("5") || response.equals("4")) {
            stopDriverLocationUpdate();
        }
    }


    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final DriverResponse response) {
        Log.e("IN PROGRESS", response.getResponse() + " " + response.getId() + " " + response.getIdTransaksi());
        if (!complete.equals("true")) {
            orderHandler(Integer.parseInt(response.getResponse()));
            DriverResponse responses = new DriverResponse();
            responses.setId("");
            responses.setIdTransaksi("");
            responses.setResponse("");
            EventBus.getDefault().postSticky(responses);
        }

    }
}
