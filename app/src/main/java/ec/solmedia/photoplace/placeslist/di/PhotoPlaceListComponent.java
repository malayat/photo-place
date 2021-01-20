package ec.solmedia.photoplace.placeslist.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.solmedia.photoplace.PhotoPlaceAppModule;
import ec.solmedia.photoplace.libs.di.LibsModule;
import ec.solmedia.photoplace.placeslist.ui.PlacesListFragment;

@Singleton
@Component(modules = {PhotoPlaceListModule.class, LibsModule.class, PhotoPlaceAppModule.class})
public interface PhotoPlaceListComponent {
    void inject(PlacesListFragment fragment);
}
