package ec.solmedia.photoplace.placesmap.ui;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ec.solmedia.photoplace.entities.MyPlace;

public interface PhotoPlacesMapView {
    void setPlacesMarkers(List<MyPlace> myPlaces);
    void removePlaceMarker(MyPlace myPlace);

    void moveCameraToMarker(MyPlace myPlace);
    //void notificationSave();
    //void onPlacesError(String error);
}
