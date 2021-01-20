package ec.solmedia.photoplace.placeslist.contract;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.main.events.MainEvent;

public interface PhotoPlaceListPresenter {
    void onCreate();
    void onDestroy();

    void getPlaces();
    void removePlace(MyPlace myPlace);

    void moveCameraToMarker(MyPlace myPlace);

    void onEventMainThread(MainEvent event);


}
