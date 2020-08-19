package com.harnet.locationtest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.views.QRFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Place> mFavoritePlaces;
    private Context mContext;
    private QRFragment qrFragment;

    public PlacesRecycleViewAdapter(Context context, List<Place> favoritePlaces, QRFragment qrFragment) {
        mFavoritePlaces = favoritePlaces;
        mContext = context;
        this.qrFragment = qrFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //TODO short click send to Google Map and show the place. Long allow to edit
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        // Set the name
        ((ViewHolder) viewHolder).mName.setText(mFavoritePlaces.get(i).getName());
        //redirection to the place
        ((ViewHolder) viewHolder).mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mFavoritePlaces.get(i).getName(), Toast.LENGTH_LONG).show();
                try {
                    qrFragment.redirectToMaps(new LatLng(mFavoritePlaces.get(i).getLat(), mFavoritePlaces.get(i).getLng()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set the image
        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_launcher_background);
        Glide.with(mContext)
                .setDefaultRequestOptions(defaultOptions)
                .load(mFavoritePlaces.get(i).getImage()) //TODO can be a cause of crash. Image feature haven't implemented yet
                .into(((ViewHolder) viewHolder).mImage);
        ((ViewHolder) viewHolder).mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mFavoritePlaces.get(i).getName(), Toast.LENGTH_LONG).show();
                try {
                    qrFragment.redirectToMaps(new LatLng(mFavoritePlaces.get(i).getLat(), mFavoritePlaces.get(i).getLng()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavoritePlaces.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mImage;
        private TextView mName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mName = itemView.findViewById(R.id.image_name);
        }
    }
}