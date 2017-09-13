package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.MvpView;

/**
 * Created by abicelis on 10/9/2017.
 */

public interface ContactDetailMvpView extends MvpView {
    void showContact(Contact contact);
    void contactDeletedSoFinish();
}
