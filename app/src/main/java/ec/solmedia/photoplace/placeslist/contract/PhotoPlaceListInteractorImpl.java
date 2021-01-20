package ec.solmedia.photoplace.placeslist.contract;

import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.main.contract.MainBaseRepository;

public class PhotoPlaceListInteractorImpl implements PhotoPlaceListInteractor {

    private MainBaseRepository repository;

    public PhotoPlaceListInteractorImpl(MainBaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        repository.getPlaces();
    }

    @Override
    public void executeMoveCamera(MyPlace myPlace) {
        repository.moveCameraToMarker(myPlace);
    }
}
