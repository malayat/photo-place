package ec.solmedia.photoplace.placesdetail.di;

import android.app.Activity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.solmedia.photoplace.libs.GlideImageLoader;
import ec.solmedia.photoplace.libs.base.ImageLoader;
import ec.solmedia.photoplace.placesdetail.ui.PhotoPlaceDetailView;

/**
 * Created by alejo on 19/10/16.
 */
@Module
public class PhotoPlaceDetailModule {

    private Activity activity;
    private PhotoPlaceDetailView view;

    public PhotoPlaceDetailModule(Activity activity, PhotoPlaceDetailView view) {
        this.activity = activity;
        this.view = view;
    }

    @Provides
    @Singleton
    PhotoPlaceDetailView providesPhotoPlaceDetailView() {
        return this.view;
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader() {
        GlideImageLoader imageLoader = new GlideImageLoader();
        if (activity != null) {
            imageLoader.setLoaderContextActivity(activity);
        }
        return imageLoader;
    }
}
