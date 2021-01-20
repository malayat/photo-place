package ec.solmedia.photoplace.placeslist.ui;

import java.util.List;

import ec.solmedia.photoplace.entities.MyPlace;

public interface PhotoPlaceListView {
    void showList();
    void hideList();
    void showProgress();
    void hideProgress();

    void setPlaces(List<MyPlace> data);
    void notificationSave();
    void removePlace(MyPlace myPlace);
}
