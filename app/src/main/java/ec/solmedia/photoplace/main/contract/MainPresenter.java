package ec.solmedia.photoplace.main.contract;

import android.location.Location;

import com.google.android.gms.location.places.Place;

import ec.solmedia.photoplace.main.events.MainEvent;


public interface MainPresenter {
    void onCreate();
    void onDestroy();

    void savePlace(Location location, String path);
    void savePlaceApiPlace(Place place, Location lastKnowLocation, String path);
    void onEventMainThread(MainEvent event);
}
