package ve.com.abicelis.androidcodetestalejandrobicelis.injection.application;

import dagger.Component;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.DataManager;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.local.SharedPreferenceHelper;
import ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter.PresenterComponent;
import ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter.PresenterModule;

/**
 * Created by abicelis on 9/9/2017.
 */

@ApplicationScope
@Component(
        modules = {
                ApplicationModule.class,
                RemoteModule.class,
                LocalModule.class
        }
)
public interface ApplicationComponent {
    void inject(AndroidCodeTestApplication target);





    // Services injected by ApplicationComponent should be:
    // Global dagger services, which should be instantiated only once per app lifecycle
    // A service to be injected into Application object
    // Services required by more than one sub-component of ApplicationComponent
    DataManager dataManager();
    SharedPreferenceHelper sharedPreferenceHelper();





    // An instance of a PresenterComponent can be instantiated from
    // this ApplicationComponent (Since PresenterComponent is an @SubComponent of ApplicationComponent)
    // while supplying the required PresenterModule.
    PresenterComponent newPresenterComponent(PresenterModule presenterModule);
}
