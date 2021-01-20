package ec.solmedia.photoplace.placeslist.contract;

import java.util.List;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.main.contract.MainBaseRepositoryImpl;

public class PhotoPlaceListRepositoryImpl extends MainBaseRepositoryImpl {

    public PhotoPlaceListRepositoryImpl(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected void post(int type, List<MyPlace> myPlaces) {
        postEvents(type, myPlaces, null);
    }


}
