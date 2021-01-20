package ec.solmedia.photoplace.main.contract;

import android.location.Location;

public interface MainRepository {
    void savePlace(Location location, String path);
}
