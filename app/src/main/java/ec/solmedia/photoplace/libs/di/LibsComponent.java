package ec.solmedia.photoplace.libs.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.solmedia.photoplace.PhotoPlaceAppModule;

@Singleton
@Component(modules = {LibsModule.class, PhotoPlaceAppModule.class})
public interface LibsComponent {
}
