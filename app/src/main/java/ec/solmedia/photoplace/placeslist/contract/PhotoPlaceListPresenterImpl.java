package ec.solmedia.photoplace.placeslist.contract;

import org.greenrobot.eventbus.Subscribe;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.main.events.MainEvent;
import ec.solmedia.photoplace.placeslist.ui.PhotoPlaceListView;

public class PhotoPlaceListPresenterImpl implements PhotoPlaceListPresenter {
    private EventBus eventBus;
    private PhotoPlaceListView view;
    private PhotoPlaceListInteractor interactor;
    private PhotoPlaceStoredInteractor storedInteractor;

    public PhotoPlaceListPresenterImpl(EventBus eventBus, PhotoPlaceListView view,
                                       PhotoPlaceListInteractor interactor,
                                       PhotoPlaceStoredInteractor storedInteractor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
        this.storedInteractor = storedInteractor;
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
    public void getPlaces() {
        if (view != null) {
            view.hideList();
            view.showProgress();
        }
        interactor.execute();
    }

    @Override
    public void removePlace(MyPlace myPlace) {
        storedInteractor.executeDelete(myPlace);
    }

    @Override
    public void moveCameraToMarker(MyPlace myPlace) {
        interactor.executeMoveCamera(myPlace);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (view != null) {
            if (view != null) {
                view.hideProgress();
                view.showList();
            }

            switch (event.getType()) {
                case MainEvent.SAVE_EVENT:
                    view.notificationSave();
                    break;
                case MainEvent.ERROR_EVENT:
                    //TODO: change error event
                    break;
                case MainEvent.UPDATE_EVENT:
                    break;
                case MainEvent.DELETE_EVENT:
                    MyPlace myPlace = event.getMyPlaces().get(0);
                    view.removePlace(myPlace);
                    break;
                case MainEvent.READ_EVENT:
                    if (event.getMyPlaces() != null) {
                        view.setPlaces(event.getMyPlaces());
                    }
                    break;
            }
        }
    }
}
