package ec.solmedia.photoplace.main.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.solmedia.photoplace.PhotoPlaceAppModule;
import ec.solmedia.photoplace.libs.di.LibsModule;
import ec.solmedia.photoplace.main.ui.MainActivity;

@Singleton
@Component(modules = {MainModule.class, LibsModule.class, PhotoPlaceAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
