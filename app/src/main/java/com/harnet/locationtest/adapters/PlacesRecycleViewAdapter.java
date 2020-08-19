package com.harnet.locationtest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Place;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Place> mFavoritePlaces = new ArrayList<>();
    private Context mContext;

    public PlacesRecycleViewAdapter(Context context, List<Place> favoritePlaces) {
        mFavoritePlaces = favoritePlaces;
        mContext = context;
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

        // Set the name of the 'NicePlace'
        ((ViewHolder) viewHolder).mName.setText(mFavoritePlaces.get(i).getName());

        // Set the image
        RequestOptions defaultOptions = new RequestOptions()
                .error(R.drawable.ic_launcher_background);
        Glide.with(mContext)
                .setDefaultRequestOptions(defaultOptions)
                .load(mFavoritePlaces.get(i).getImage()) //TODO can be a cause of crash. Image feature haven't implemented yet
                .into(((ViewHolder) viewHolder).mImage);
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
            mImage = itemView.findViewById(R.id.place_ImageView);
            mName = itemView.findViewById(R.id.place_textView);
        }
    }
}