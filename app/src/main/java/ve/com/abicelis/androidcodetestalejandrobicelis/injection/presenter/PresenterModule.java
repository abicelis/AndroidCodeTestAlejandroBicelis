package ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.DataManager;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail.ContactDetailPresenter;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.ContactEditPresenter;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.dialog.AddRandomContactsPresenter;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.HomePresenter;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.imageedit.ImageEditPresenter;

/**
 * Created by abicelis on 9/9/2017.
 */

@Module
public class PresenterModule {

    private final Activity mActivity;

    public PresenterModule(Activity activity) { mActivity = activity; }

    @Provides
    Activity activity() { return mActivity; }



    /* Presenters */
    @Provides
    HomePresenter homePresenter(DataManager dataManager) {
        return new HomePresenter(dataManager);
    }

    @Provides
    AddRandomContactsPresenter addRandomContactsPresenter(DataManager dataManager) {return new AddRandomContactsPresenter(dataManager);}

    @Provides
    ContactDetailPresenter contactDetailPresenter(DataManager dataManager) {return new ContactDetailPresenter(dataManager);}

    @Provides
    ContactEditPresenter contactEditPresenter(DataManager dataManager) {return new ContactEditPresenter(dataManager);}

    @Provides
    ImageEditPresenter imageEditPresenter() {return new ImageEditPresenter();}

}
