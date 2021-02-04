package com.ourdevelops.ourjek.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ourdevelops.ourjek.R;
import com.ourdevelops.ourjek.activity.ProgressActivity;
import com.ourdevelops.ourjek.constants.Constants;
import com.ourdevelops.ourjek.models.AllTransaksiModel;
import com.ourdevelops.ourjek.utils.PicassoTrustAll;
import com.ourdevelops.ourjek.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by otacodes on 3/24/2019.
 */

public class HistoryItem extends RecyclerView.Adapter<HistoryItem.ItemRowHolder> {

    private List<AllTransaksiModel> dataList;
    private Context mContext;
    private int rowLayout;


    public HistoryItem(Context context, List<AllTransaksiModel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;

    }


    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final AllTransaksiModel singleItem = dataList.get(position);
        holder.text.setText("Order " + singleItem.getFitur());

        Utility.currencyTXT(holder.nominal, singleItem.getBiayaakhir(), mContext);
        holder.keterangan.setText(singleItem.getStatustransaksi());


        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String finalDate = timeFormat.format(singleItem.getWaktuOrder());
        holder.tanggal.setText(finalDate);

        holder.address.setText(singleItem.getAlamatTujuan());
        holder.rating.setText(singleItem.getRate());

        PicassoTrustAll.getInstance(mContext)
                .load(Constants.IMAGESFITUR + singleItem.getIcon())
                .into(holder.images);

        if (singleItem.status == 4 && singleItem.getRate().isEmpty()) {
            holder.keterangan.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.colorgradient));
            holder.background.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rect));
            holder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ProgressActivity.class);
                    i.putExtra("id_driver", singleItem.getIdDriver());
                    i.putExtra("id_transaksi", singleItem.getIdTransaksi());
                    i.putExtra("complete", "true");
                    i.putExtra("response", String.valueOf(singleItem.status));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(i);

                }
            });
        } else if (singleItem.status == 5) {
            holder.keterangan.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.background.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rect_red));
            holder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ProgressActivity.class);
                    i.putExtra("id_driver", singleItem.getIdDriver());
                    i.putExtra("id_transaksi", singleItem.getIdTransaksi());
                    i.putExtra("complete", "true");
                    i.putExtra("response", String.valueOf(singleItem.status));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(i);

                }
            });
        } else {
            holder.keterangan.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.colorgradient));
            holder.background.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rect));
            holder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ProgressActivity.class);
                    i.putExtra("id_driver", singleItem.getIdDriver());
                    i.putExtra("id_transaksi", singleItem.getIdTransaksi());
                    i.putExtra("response", String.valueOf(singleItem.status));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(i);

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    static class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView text, tanggal, nominal, keterangan, rating, address;
        ImageView background, images;
        RelativeLayout itemlayout;

        ItemRowHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            images = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            tanggal = itemView.findViewById(R.id.texttanggal);
            nominal = itemView.findViewById(R.id.price);
            keterangan = itemView.findViewById(R.id.textket);
            itemlayout = itemView.findViewById(R.id.mainlayout);
            address = itemView.findViewById(R.id.address);
            rating = itemView.findViewById(R.id.rate);
        }
    }


}
