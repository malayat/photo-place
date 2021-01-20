package ec.solmedia.photoplace.placesmap.di;

import android.support.v4.app.Fragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.solmedia.photoplace.libs.GlideImageLoader;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.libs.base.ImageLoader;
import ec.solmedia.photoplace.main.contract.MainBaseRepository;
import ec.solmedia.photoplace.placesmap.contract.PhotoPlaceMapInteractor;
import ec.solmedia.photoplace.placesmap.contract.PhotoPlaceMapInteractorImpl;
import ec.solmedia.photoplace.placesmap.contract.PhotoPlaceMapPresenter;
import ec.solmedia.photoplace.placesmap.contract.PhotoPlaceMapPresenterImpl;
import ec.solmedia.photoplace.placesmap.contract.PhotoPlaceMapRepositoryImpl;
import ec.solmedia.photoplace.placesmap.ui.PhotoPlacesMapView;

@Module
public class PhotoPlaceMapModule {
    private Fragment fragment;
    PhotoPlacesMapView view;

    public PhotoPlaceMapModule(Fragment fragment, PhotoPlacesMapView view) {
        this.fragment = fragment;
        this.view = view;
    }

    @Provides
    @Singleton
    PhotoPlacesMapView providesPhotoContentView() {
        return this.view;
    }

    @Provides @Singleton
    PhotoPlaceMapPresenter providesPhotoContentPresenter(EventBus eventBus, PhotoPlaceMapInteractor interactor) {
        return new PhotoPlaceMapPresenterImpl(eventBus, this.view, interactor);
    }

    @Provides @Singleton
    PhotoPlaceMapInteractor providesPhotoPlaceMapInteractor(MainBaseRepository repository) {
        return new PhotoPlaceMapInteractorImpl(repository);
    }

    @Provides @Singleton
    MainBaseRepository providesPhotoMapRepository(EventBus eventBus) {
        return new PhotoPlaceMapRepositoryImpl(eventBus);
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(Fragment fragment) {
        GlideImageLoader imageLoader = new GlideImageLoader();
        if (fragment != null) {
            imageLoader.setLoaderContextFragement(fragment);
        }
        return imageLoader;
    }

    @Provides
    @Singleton
    Fragment provideFragment() {
        return this.fragment;
    }
}
