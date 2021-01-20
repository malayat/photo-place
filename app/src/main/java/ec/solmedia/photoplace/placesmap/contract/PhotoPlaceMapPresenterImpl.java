package ec.solmedia.photoplace.placesmap.contract;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.Subscribe;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.main.events.MainEvent;
import ec.solmedia.photoplace.placesmap.ui.PhotoPlacesMapView;


public class PhotoPlaceMapPresenterImpl implements PhotoPlaceMapPresenter {
    private EventBus eventBus;
    private PhotoPlacesMapView view;
    private PhotoPlaceMapInteractor interactor;

    public PhotoPlaceMapPresenterImpl(EventBus eventBus, PhotoPlacesMapView view, PhotoPlaceMapInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void getMarkersPlaces() {
        interactor.execute();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case MainEvent.DELETE_EVENT:
                    MyPlace myPlace = event.getMyPlaces().get(0);
                    view.removePlaceMarker(myPlace);
                    break;
                case MainEvent.READ_EVENT:
                    if (event.getMyPlaces() != null) {
                        Log.e("MAPPRESENTER", "getMyPlaces del map presenter");
                        view.setPlacesMarkers(event.getMyPlaces());
                    }
                    break;
                case MainEvent.MOVE_EVENT:
                    if(event.getMyPlaces() != null) {
                        MyPlace myPlaceMove = event.getMyPlaces().get(0);
                        LatLng location = new LatLng(myPlaceMove.getLatitude(), myPlaceMove.getLongitude());
                        view.moveCameraToMarker(myPlaceMove);

                    }
                    break;
            }
        }
    }
}
