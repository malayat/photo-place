package ec.solmedia.photoplace.placeslist.adapter;

import android.widget.ImageView;

import ec.solmedia.photoplace.entities.MyPlace;

public interface OnItemClickListener {
    void onAddressClick(MyPlace myPlace);
    void onShareClick(MyPlace myPlace, ImageView img);
    void onDeleteClick(MyPlace myPlace);
    void onImageClick(MyPlace myPlace);
    void onGoogleMapsClick(MyPlace myPlace);
    //void onFavClick(MyPlace place);
    //void onEditClick(MyPlace place);
}
