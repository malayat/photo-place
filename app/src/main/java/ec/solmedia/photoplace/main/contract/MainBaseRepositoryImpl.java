package ec.solmedia.photoplace.main.contract;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.main.events.MainEvent;

public abstract class MainBaseRepositoryImpl implements MainBaseRepository {
    private EventBus eventBus;
    private Util utils;
    public static final String TAG = "MainBaseRepositoryImpl";

    public MainBaseRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public MainBaseRepositoryImpl(EventBus eventBus, Util utils) {
        this.eventBus = eventBus;
        this.utils = utils;
    }

    @Override
    public void savePlace(Location location, String path) {
        String strLocation = "";
        Map<String, String> namesLocation = new HashMap<>();

        try {
            strLocation = utils.getAddressFromLocation(
                    location.getLatitude(), location.getLongitude());
            namesLocation = utils.getNamesLocations(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            strLocation = "Lat: " + location.getLatitude() + " Lon: " + location.getLongitude();
            namesLocation.put("cityName", "" + location.getLatitude());
            namesLocation.put("postalCode", ", " + location.getLongitude());
            Log.e(TAG, e.getLocalizedMessage());
        }

        final MyPlace myPlace = new MyPlace();
        myPlace.setName(String.format("%s, %s", namesLocation.get("cityName"), namesLocation.get("postalCode")));
        myPlace.setAddress(strLocation);
        myPlace.setPhoto(path);
        Log.d(TAG, myPlace.getPhoto());
        myPlace.setDate(System.currentTimeMillis());
        myPlace.setLatitude(location.getLatitude());
        myPlace.setLongitude(location.getLongitude());

        myPlace.save();
        post(MainEvent.SAVE_EVENT, null);
    }

    @Override
    public void savePlaceApiPlace(Place place, Location lastKnowLocation, String path) {
        final MyPlace myPlace = new MyPlace();
        myPlace.setPlaceId(place.getId());
        myPlace.setName(place.getName().toString());
        myPlace.setAddress(place.getAddress().toString());
        myPlace.setPhoto(path);
        myPlace.setPhone(place.getPhoneNumber() != null ? place.getPhoneNumber().toString() : null);
        myPlace.setRaiting(place.getRating());
        myPlace.setUrl(place.getWebsiteUri() != null ? place.getWebsiteUri().toString() : null);
        myPlace.setDate(System.currentTimeMillis());
        myPlace.setLatitude(lastKnowLocation.getLatitude());
        myPlace.setLongitude(lastKnowLocation.getLongitude());

        myPlace.save();
        post(MainEvent.SAVE_EVENT, null);
    }

    @Override
    public void updatePlace(MyPlace myPlace) {

    }

    @Override
    public void deletePlace(MyPlace myPlace) {
        myPlace.delete();
        post(MainEvent.DELETE_EVENT, Arrays.asList(myPlace));
    }

    @Override
    public void getPlaces() {
        ModelQueriable<MyPlace> model = SQLite.select().from(MyPlace.class);
        FlowCursorList<MyPlace> storedPlaces = new FlowCursorList<>(true, model);
        post(MainEvent.READ_EVENT, storedPlaces.getAll().isEmpty() ? null : storedPlaces.getAll());
    }

    @Override
    public void moveCameraToMarker(MyPlace myPlace) {
        post(MainEvent.MOVE_EVENT, Arrays.asList(myPlace));
    }

    protected abstract void post(int type, List<MyPlace> myPlaces);

    protected void postEvents(int type, List<MyPlace> myPlaces, String error) {
        MainEvent event = new MainEvent();
        event.setType(type);
        event.setError(error);
        event.setMyPlaces(myPlaces);
        eventBus.post(event);
    }
}
