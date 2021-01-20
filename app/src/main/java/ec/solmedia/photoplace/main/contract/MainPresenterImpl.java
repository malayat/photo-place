package ec.solmedia.photoplace.main.contract;

import android.location.Location;

import com.google.android.gms.location.places.Place;

import org.greenrobot.eventbus.Subscribe;

import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.main.events.MainEvent;
import ec.solmedia.photoplace.main.ui.MainView;

public class MainPresenterImpl implements MainPresenter {

    MainView view;
    EventBus eventBus;
    SaveInteractor saveInteractor;

    public MainPresenterImpl(MainView view, EventBus eventBus, SaveInteractor saveInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.saveInteractor = saveInteractor;
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
    public void savePlace(Location location, String path) {
        saveInteractor.execute(location, path);
    }

    @Override
    public void savePlaceApiPlace(Place place, Location lastKnowLocation, String path) {
        saveInteractor.executeApiPlace(place, lastKnowLocation, path);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case MainEvent.SAVE_EVENT:
                    view.onSavePlace();
                    break;
                case MainEvent.ERROR_EVENT:
                    view.onSaveError();
                    break;
            }
        }
    }
}
