package ec.solmedia.photoplace.placesmap.contract;

import ec.solmedia.photoplace.main.contract.MainBaseRepository;

public class PhotoPlaceMapInteractorImpl implements PhotoPlaceMapInteractor {

    MainBaseRepository repository;

    public PhotoPlaceMapInteractorImpl(MainBaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        this.repository.getPlaces();
    }
}
