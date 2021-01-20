package ec.solmedia.photoplace;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.facebook.FacebookSdk;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import ec.solmedia.photoplace.libs.di.LibsModule;
import ec.solmedia.photoplace.login.di.DaggerLoginComponent;
import ec.solmedia.photoplace.login.di.LoginComponent;
import ec.solmedia.photoplace.main.di.DaggerMainComponent;
import ec.solmedia.photoplace.main.di.MainComponent;
import ec.solmedia.photoplace.main.di.MainModule;
import ec.solmedia.photoplace.main.ui.MainActivity;
import ec.solmedia.photoplace.main.ui.MainView;
import ec.solmedia.photoplace.placesdetail.di.DaggerPhotoPlaceDetailComponent;
import ec.solmedia.photoplace.placesdetail.di.PhotoPlaceDetailComponent;
import ec.solmedia.photoplace.placesdetail.di.PhotoPlaceDetailModule;
import ec.solmedia.photoplace.placesdetail.ui.PhotoPhotoPlaceDetailActivity;
import ec.solmedia.photoplace.placesdetail.ui.PhotoPlaceDetailView;
import ec.solmedia.photoplace.placeslist.adapter.OnItemClickListener;
import ec.solmedia.photoplace.placeslist.di.DaggerPhotoPlaceListComponent;
import ec.solmedia.photoplace.placeslist.di.PhotoPlaceListComponent;
import ec.solmedia.photoplace.placeslist.di.PhotoPlaceListModule;
import ec.solmedia.photoplace.placeslist.ui.PhotoPlaceListView;
import ec.solmedia.photoplace.placesmap.di.DaggerPhotoPlaceMapComponent;
import ec.solmedia.photoplace.placesmap.di.PhotoPlaceMapComponent;
import ec.solmedia.photoplace.placesmap.di.PhotoPlaceMapModule;
import ec.solmedia.photoplace.placesmap.ui.PhotoPlacesMapView;

public class PhotoPlaceApp extends Application {

    public static final String SHARED_PREFERENCES_NAME = "UserPrefs";
    public static final String PREF_TRACKING = "tracking";
    public static final String PREF_MAP_STYLE = "typesMaps";

    public static final String EMAIL_KEY = "email";
    public static final String NAME_KEY = "name";
    public static final String ID_KEY = "id";

    private PhotoPlaceAppModule photoPlaceAppModule;
    private LibsModule libsModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initFacebook();
        initModules();
        initDB();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBTearDown();
    }

    private void initModules() {
        this.photoPlaceAppModule = new PhotoPlaceAppModule(this);
        this.libsModule = new LibsModule();
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(this);
    }

    private void initDB() {
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    public LoginComponent getLoginComponent() {
        return DaggerLoginComponent
                .builder()
                .photoPlaceAppModule(photoPlaceAppModule)
                .build();
    }

    public MainComponent getMainComponent(MainActivity activity, MainView view, String[] titles,
                                          Fragment[] fragments, FragmentManager fragmentManager) {
        return DaggerMainComponent
                .builder()
                .photoPlaceAppModule(photoPlaceAppModule)
                .libsModule(libsModule)
                .mainModule(new MainModule(activity, view, titles, fragments, fragmentManager))
                .build();
    }

    public PhotoPlaceListComponent getPhotoPlaceListComponent(Fragment fragment, PhotoPlaceListView view,
                                                              OnItemClickListener onItemClickListener) {
        return DaggerPhotoPlaceListComponent
                .builder()
                .photoPlaceAppModule(photoPlaceAppModule)
                .libsModule(libsModule)
                .photoPlaceListModule(new PhotoPlaceListModule(fragment, view, onItemClickListener))
                .build();
    }

    public PhotoPlaceMapComponent getPhotoPlaceMapComponent(Fragment fragment, PhotoPlacesMapView view) {
        return DaggerPhotoPlaceMapComponent
                .builder()
                .photoPlaceAppModule(photoPlaceAppModule)
                .libsModule(libsModule)
                .photoPlaceMapModule(new PhotoPlaceMapModule(fragment, view))
                .build();
    }

    public PhotoPlaceDetailComponent getPhotoPlaceDetailComponent(PhotoPhotoPlaceDetailActivity activity, PhotoPlaceDetailView view) {
        return DaggerPhotoPlaceDetailComponent
                .builder()
                .photoPlaceAppModule(photoPlaceAppModule)
                .libsModule(libsModule)
                .photoPlaceDetailModule(new PhotoPlaceDetailModule(activity, view))
                .build();
    }

}
