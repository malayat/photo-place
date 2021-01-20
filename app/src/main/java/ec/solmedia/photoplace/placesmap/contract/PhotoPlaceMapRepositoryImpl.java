package ec.solmedia.photoplace.placesmap.contract;

import java.util.List;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.main.contract.MainBaseRepositoryImpl;

public class PhotoPlaceMapRepositoryImpl extends MainBaseRepositoryImpl {


    public PhotoPlaceMapRepositoryImpl(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void post(int type, List<MyPlace> myPlaces) {
        postEvents(type, myPlaces, null);
    }

}
