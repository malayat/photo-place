package ec.solmedia.photoplace.main.contract;

import android.location.Location;

import com.google.android.gms.location.places.Place;

public class SaveInteractorImpl implements SaveInteractor {

    private MainBaseRepository repository;

    public SaveInteractorImpl(MainBaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Location location, String path) {
        repository.savePlace(location, path);
    }

    @Override
    public void executeApiPlace(Place place, Location lastKnowLocation, String path) {
        repository.savePlaceApiPlace(place, lastKnowLocation, path);
    }
}
