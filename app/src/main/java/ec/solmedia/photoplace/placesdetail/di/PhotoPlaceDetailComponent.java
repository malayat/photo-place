package ec.solmedia.photoplace.placesdetail.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.solmedia.photoplace.PhotoPlaceAppModule;
import ec.solmedia.photoplace.libs.di.LibsModule;
import ec.solmedia.photoplace.placesdetail.ui.PhotoPhotoPlaceDetailActivity;


/**
 * Created by alejo on 18/10/16.
 */
@Singleton
@Component(modules = {PhotoPlaceDetailModule.class, PhotoPlaceAppModule.class, LibsModule.class})
public interface PhotoPlaceDetailComponent {
    void inject(PhotoPhotoPlaceDetailActivity activity);
}
