package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.MvpView;

/**
 * Created by abicelis on 11/9/2017.
 */

public interface ContactEditMvpView extends MvpView {
    void showContact(Contact contact);
    EditPhoneAdapter getPhoneAdapter();
    EditEmailAdapter getEmailAdapter();
    EditAddressAdapter getAddressAdapter();
    void contactSavedSoFinish();
}
