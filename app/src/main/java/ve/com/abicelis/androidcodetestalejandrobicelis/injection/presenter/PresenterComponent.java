package ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter;

import dagger.Subcomponent;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail.ContactDetailActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.ContactEditActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.dialog.AddRandomContactsDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.HomeActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.imageedit.ImageEditActivity;

/**
 * Created by abicelis on 9/9/2017.
 */

@Subcomponent(
        modules = {
                PresenterModule.class,
        }
)
public interface PresenterComponent {
    void inject(HomeActivity target);
    void inject(AddRandomContactsDialogFragment target);
    void inject(ContactDetailActivity target);
    void inject(ContactEditActivity target);
    void inject(ImageEditActivity target);

}
