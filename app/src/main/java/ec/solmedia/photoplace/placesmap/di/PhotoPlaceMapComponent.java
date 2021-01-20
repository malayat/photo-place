package ec.solmedia.photoplace.placesmap.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.solmedia.photoplace.PhotoPlaceAppModule;
import ec.solmedia.photoplace.libs.di.LibsModule;
import ec.solmedia.photoplace.placesmap.ui.PhotoPlacesMapFragment;


@Singleton
@Component(modules = {PhotoPlaceMapModule.class, LibsModule.class, PhotoPlaceAppModule.class})
public interface PhotoPlaceMapComponent {
    void inject(PhotoPlacesMapFragment fragment);
}
