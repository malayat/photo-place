package ec.solmedia.photoplace.placeslist.di;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.GlideImageLoader;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.libs.base.ImageLoader;
import ec.solmedia.photoplace.main.contract.MainBaseRepository;
import ec.solmedia.photoplace.placeslist.adapter.OnItemClickListener;
import ec.solmedia.photoplace.placeslist.adapter.PhotoPlaceListAdapter;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceListInteractor;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceListInteractorImpl;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceListPresenter;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceListPresenterImpl;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceListRepositoryImpl;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceStoredInteractor;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceStoredInteractorImpl;
import ec.solmedia.photoplace.placeslist.ui.PhotoPlaceListView;

@Module
public class PhotoPlaceListModule {
    private Fragment fragment;
    private PhotoPlaceListView view;
    private OnItemClickListener onItemClickListener;

    public PhotoPlaceListModule(Fragment fragment, PhotoPlaceListView view, OnItemClickListener onItemClickListener) {
        this.fragment = fragment;
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    PhotoPlaceListView providesPhotoListView() {
        return this.view;
    }

    @Provides
    @Singleton
    PhotoPlaceListPresenter providesPhotoPlaceListPresenter(EventBus eventBus,
                                                            PhotoPlaceListView view,
                                                            PhotoPlaceListInteractor interactor,
                                                            PhotoPlaceStoredInteractor storedInteractor) {
        return new PhotoPlaceListPresenterImpl(eventBus, view, interactor, storedInteractor);
    }

    @Provides
    @Singleton
    PhotoPlaceListInteractor providesPhotoPlaceListInteractor(MainBaseRepository repository) {
        return new PhotoPlaceListInteractorImpl(repository);
    }

    @Provides
    @Singleton
    PhotoPlaceStoredInteractor providesPhotoPlaceStoredInteractor(MainBaseRepository repository) {
        return new PhotoPlaceStoredInteractorImpl(repository);
    }

    @Provides
    @Singleton
    MainBaseRepository providesPhotoListRepository(EventBus eventBus) {
        return new PhotoPlaceListRepositoryImpl(eventBus);
    }

    @Provides
    @Singleton
    PhotoPlaceListAdapter providesPhotoListAdapter(Util utils, List<MyPlace> myPlaces,
                                                   ImageLoader imageLoader) {
        return new PhotoPlaceListAdapter(utils, myPlaces, imageLoader, this.onItemClickListener);
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

    @Provides
    @Singleton
    OnItemClickListener providesOnItemClickListener() {
        return this.onItemClickListener;
    }

    @Provides
    @Singleton
    List<MyPlace> providesPlacesList() {
        return new ArrayList<>();
    }

}
