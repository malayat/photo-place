package ec.solmedia.photoplace.main.events;

import java.util.List;

import ec.solmedia.photoplace.entities.MyPlace;

public class MainEvent {
    private int type;
    private String error;
    private List<MyPlace> myPlaces;
    public static final int SAVE_EVENT = 0;
    public static final int UPDATE_EVENT = 1;
    public static final int DELETE_EVENT = 2;
    public static final int READ_EVENT = 3;
    public static final int ERROR_EVENT = 4;
    public static final int MOVE_EVENT = 5;

    public int getType() {
        return type;
    }

    public List<MyPlace> getMyPlaces() {
        return myPlaces;
    }

    public void setMyPlaces(List<MyPlace> myPlaces) {
        this.myPlaces = myPlaces;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
