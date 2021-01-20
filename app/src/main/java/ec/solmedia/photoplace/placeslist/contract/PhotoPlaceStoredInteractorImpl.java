package ec.solmedia.photoplace.placeslist.contract;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.main.contract.MainBaseRepository;


public class PhotoPlaceStoredInteractorImpl implements PhotoPlaceStoredInteractor {

    private MainBaseRepository repository;

    public PhotoPlaceStoredInteractorImpl(MainBaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executeDelete(MyPlace myPlace) {
        repository.deletePlace(myPlace);
    }
}
