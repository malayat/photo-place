package ec.solmedia.photoplace.login.di;

import javax.inject.Singleton;

import dagger.Component;
import ec.solmedia.photoplace.PhotoPlaceAppModule;
import ec.solmedia.photoplace.login.ui.LoginActivity;

@Singleton
@Component(modules = {PhotoPlaceAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
