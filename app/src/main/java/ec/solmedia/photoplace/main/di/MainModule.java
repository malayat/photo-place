package ec.solmedia.photoplace.main.di;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ec.solmedia.photoplace.libs.GlideImageLoader;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.EventBus;
import ec.solmedia.photoplace.libs.base.ImageLoader;
import ec.solmedia.photoplace.main.adapters.MainSectionsPagerAdapter;
import ec.solmedia.photoplace.main.contract.MainBaseRepository;
import ec.solmedia.photoplace.main.contract.MainPresenter;
import ec.solmedia.photoplace.main.contract.MainPresenterImpl;
import ec.solmedia.photoplace.main.contract.MainRepositoryImpl;
import ec.solmedia.photoplace.main.contract.SaveInteractor;
import ec.solmedia.photoplace.main.contract.SaveInteractorImpl;
import ec.solmedia.photoplace.main.ui.MainView;


@Module
public class MainModule {
    private Activity activity;
    private MainView view;
    private String[] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public MainModule(Activity activity, MainView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        this.activity = activity;
        this.view = view;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(EventBus eventBus, SaveInteractor saveInteractor) {
        return new MainPresenterImpl(this.view, eventBus, saveInteractor);
    }

    @Provides
    @Singleton
    SaveInteractor providesSaveInteractor(MainBaseRepository repository) {
        return new SaveInteractorImpl(repository);
    }

    @Provides
    @Singleton
    MainBaseRepository providesMainRepository(EventBus eventBus, Util utils) {
        return new MainRepositoryImpl(eventBus, utils);
    }

    @Provides
    @Singleton
    MainSectionsPagerAdapter providesAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        return new MainSectionsPagerAdapter(fm, fragments, titles);
    }

    @Provides
    @Singleton
    Activity providesActivity() {
        return this.activity;
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

    @Provides
    @Singleton
    FragmentManager providesAdapterFragmentManager() {
        return this.fragmentManager;
    }

    @Provides
    @Singleton
    Fragment[] providesFragmentArrayForAdapter() {
        return this.fragments;
    }

    @Provides
    @Singleton
    String[] providesStringArrayForAdapter() {
        return this.titles;
    }
}
