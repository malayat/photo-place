package ec.solmedia.photoplace.placeslist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.solmedia.photoplace.R;
import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.ImageLoader;


public class PhotoPlaceListAdapter extends RecyclerView.Adapter<PhotoPlaceListAdapter.ViewHolder> {

    private Util utils;
    private List<MyPlace> myPlaces;
    ImageLoader imageLoader;
    OnItemClickListener onItemClickListener;

    public PhotoPlaceListAdapter(Util utils, List<MyPlace> myPlaces, ImageLoader imageLoader, OnItemClickListener onItemClickListener) {
        this.utils = utils;
        this.myPlaces = myPlaces;
        this.imageLoader = imageLoader;
        this.onItemClickListener = onItemClickListener;
    }

    public void setMyPlaces(List<MyPlace> myPlaces) {
        this.myPlaces = myPlaces;
        notifyDataSetChanged();
    }

    public void removePlace(MyPlace myPlace) {
        myPlaces.remove(myPlace);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.fragment_row_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyPlace currentMyPlace = myPlaces.get(position);
        holder.setOnItemClickLister(currentMyPlace, this.onItemClickListener);

        holder.txtPlaceName.setText(currentMyPlace.getName());
        imageLoader.load(holder.imgMain, currentMyPlace.getPhoto());
        holder.txtPlaceAddress.setText(currentMyPlace.getAddress());
    }

    @Override
    public int getItemCount() {
        return myPlaces.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtPlaceName)
        TextView txtPlaceName;
        @BindView(R.id.imgMain)
        ImageView imgMain;
        @BindView(R.id.txtPlaceAddress)
        TextView txtPlaceAddress;
        @BindView(R.id.imgShare)
        ImageButton btnImgShare;
        @BindView(R.id.imgDelete)
        ImageButton btnImgDelete;
        @BindView(R.id.imgGoogleMaps)
        ImageButton btnImgGoogleMaps;
        @BindView(R.id.imgOpenMaps)
        ImageButton btnImgOpenMaps;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnItemClickLister(final MyPlace myPlace, final OnItemClickListener listener) {
            imgMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onImageClick(myPlace);
                }
            });

            txtPlaceAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAddressClick(myPlace);
                }
            });

            btnImgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onShareClick(myPlace, imgMain);
                }
            });

            btnImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDeleteClick(myPlace);
                }
            });

            btnImgGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGoogleMapsClick(myPlace);
                }
            });

            btnImgOpenMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAddressClick(myPlace);
                }
            });

        }
    }
}
