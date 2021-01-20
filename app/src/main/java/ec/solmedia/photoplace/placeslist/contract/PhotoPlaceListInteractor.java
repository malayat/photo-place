package ec.solmedia.photoplace.placeslist.contract;

import ec.solmedia.photoplace.entities.MyPlace;

public interface PhotoPlaceListInteractor {
    void execute();

    void executeMoveCamera(MyPlace myPlace);
}
