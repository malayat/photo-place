package ec.solmedia.photoplace.main.contract;

import android.location.Location;

import com.google.android.gms.location.places.Place;

public interface SaveInteractor {
    void execute(Location location, String path);
    void executeApiPlace(Place place, Location lastKnowLocation, String path);
}
