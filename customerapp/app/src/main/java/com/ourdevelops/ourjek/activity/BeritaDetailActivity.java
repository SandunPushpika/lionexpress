package com.ourdevelops.ourjek.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.constants.BaseApp;
import com.ourdevelops.ourjek.constants.Constants;
import com.ourdevelops.ourjek.json.BeritaDetailRequestJson;
import com.ourdevelops.ourjek.json.BeritaDetailResponseJson;
import com.ourdevelops.ourjek.models.BeritaModel;
import com.ourdevelops.ourjek.models.User;
import com.ourdevelops.ourjek.utils.DatabaseHelper;
import com.ourdevelops.ourjek.utils.PicassoTrustAll;
import com.ourdevelops.ourjek.utils.api.ServiceGenerator;
import com.ourdevelops.ourjek.utils.api.service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by otacodes on 3/26/2019.
 */

public class BeritaDetailActivity extends AppCompatActivity {

    TextView title, tanggal, kategori;
    String Id;
    WebView description;
    ImageView backButton, images, favourite;
    RelativeLayout rlprogress;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beritadetail);
        databaseHelper = new DatabaseHelper(this);
        Intent i = getIntent();
        Id = i.getStringExtra("id");
        title = findViewById(R.id.title);
        tanggal = findViewById(R.id.tanggal);

        images = findViewById(R.id.image);
        backButton = findViewById(R.id.back_btn);
        kategori = findViewById(R.id.kategori);
        description = findViewById(R.id.description);
        rlprogress = findViewById(R.id.rlprogress);
        favourite = findViewById(R.id.favourite);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

    }

    private void getData() {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        UserService service = ServiceGenerator.createService(UserService.class, loginUser.getEmail(), loginUser.getPassword());
        BeritaDetailRequestJson param = new BeritaDetailRequestJson();
        param.setId(Id);

        service.beritadetail(param).enqueue(new Callback<BeritaDetailResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<BeritaDetailResponseJson> call, @NonNull Response<BeritaDetailResponseJson> response) {
                if (response.isSuccessful()) {
                    rlprogress.setVisibility(View.GONE);
                    final BeritaModel berita = Objects.requireNonNull(response.body()).getData().get(0);
                    title.setText(berita.getTitle());

                    if (!berita.getFotoberita().isEmpty()) {
                        PicassoTrustAll.getInstance(BeritaDetailActivity.this)
                                .load(Constants.IMAGESBERITA + berita.getFotoberita())
                                .resize(250,250)
                                .into(images);
                    }
                    Date myDate = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
                    try {
                        myDate = dateFormat.parse(berita.getCreatedberita());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    String finalDate = timeFormat.format(Objects.requireNonNull(myDate));
                    tanggal.setText(finalDate);

                    String mimeType = "text/html";
                    String encoding = "utf-8";
                    String htmlText = berita.getContent();

                    String text = "<html dir=" + "><head>"
                            + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/NeoSans_Pro_Regular.ttf\")}body{font-family: MyFont;color: #000000;text-align:justify;line-height:1.2}"
                            + "</style></head>"
                            + "<body>"
                            + htmlText
                            + "</body></html>";
                    description.loadDataWithBaseURL(null, text, mimeType, encoding, null);
                    kategori.setText(berita.getKategori());

                    final User loginUser = BaseApp.getInstance(BeritaDetailActivity.this).getLoginUser();
                    favourite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ContentValues fav = new ContentValues();
                            if (databaseHelper.getFavouriteById(String.valueOf(berita.getIdberita()))) {
                                databaseHelper.removeFavouriteById(String.valueOf(berita.getIdberita()));
                                favourite.setColorFilter(getResources().getColor(R.color.gray));
                                Toast.makeText(BeritaDetailActivity.this, "Remove To Favourite", Toast.LENGTH_SHORT).show();
                            } else {
                                fav.put(DatabaseHelper.KEY_ID, berita.getIdberita());
                                fav.put(DatabaseHelper.KEY_USERID, loginUser.getId());
                                fav.put(DatabaseHelper.KEY_TITLE, berita.getTitle());
                                fav.put(DatabaseHelper.KEY_CONTENT, berita.getContent());
                                fav.put(DatabaseHelper.KEY_KATEGORI, berita.getKategori());
                                fav.put(DatabaseHelper.KEY_IMAGE, berita.getFotoberita());
                                databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                                favourite.setColorFilter(getResources().getColor(R.color.red));
                                Toast.makeText(BeritaDetailActivity.this, "Add To Favourite", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    if (databaseHelper.getFavouriteById(String.valueOf(berita.getIdberita()))) {
                        favourite.setColorFilter(getResources().getColor(R.color.red));
                    } else {
                        favourite.setColorFilter(getResources().getColor(R.color.gray));
                    }
                }


            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<BeritaDetailResponseJson> call, @NonNull Throwable t) {

            }
        });

    }

}
