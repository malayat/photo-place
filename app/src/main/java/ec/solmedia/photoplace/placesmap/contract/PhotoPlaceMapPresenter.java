package ec.solmedia.photoplace.placesmap.contract;

import ec.solmedia.photoplace.main.events.MainEvent;


public interface PhotoPlaceMapPresenter {
    void onCreate();
    void onDestroy();

    void getMarkersPlaces();

    void onEventMainThread(MainEvent event);
}
