package ec.solmedia.photoplace.libs.di;

import android.content.Context;
import android.location.Geocoder;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.solmedia.photoplace.libs.GreenRobotEventBus;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.EventBus;

@Module
public class LibsModule {

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new GreenRobotEventBus();
    }

    @Provides
    @Singleton
    Util providesUtil(Geocoder geocoder) {
        return new Util(geocoder);
    }

    @Provides
    @Singleton
    Geocoder providesGeocoder(Context context) {
        return new Geocoder(context, Locale.getDefault());
    }

}
