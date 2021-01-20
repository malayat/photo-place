package ec.solmedia.photoplace.main.contract;

import android.location.Location;

import com.google.android.gms.location.places.Place;

import ec.solmedia.photoplace.entities.MyPlace;

public interface MainBaseRepository {
    void savePlace(Location location, String path);
    void updatePlace(MyPlace myPlace);
    void deletePlace(MyPlace myPlace);
    void getPlaces();

    void savePlaceApiPlace(Place place, Location lastKnowLocation, String path);

    void moveCameraToMarker(MyPlace myPlace);
}
