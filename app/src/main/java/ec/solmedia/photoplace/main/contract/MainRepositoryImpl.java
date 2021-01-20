package ec.solmedia.photoplace.main.contract;

import java.util.List;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.EventBus;

public class MainRepositoryImpl extends MainBaseRepositoryImpl {

    public MainRepositoryImpl(EventBus eventBus, Util utils) {
        super(eventBus, utils);
    }

    @Override
    protected void post(int type, List<MyPlace> myPlaces) {
        postEvents(type, null, null);
    }
}
