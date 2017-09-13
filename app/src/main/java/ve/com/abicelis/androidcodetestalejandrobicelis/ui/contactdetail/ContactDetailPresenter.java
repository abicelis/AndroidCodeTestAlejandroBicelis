package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.DataManager;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BasePresenter;

/**
 * Created by abicelis on 10/9/2017.
 */

public class ContactDetailPresenter extends BasePresenter<ContactDetailMvpView> {

    //DATA
    DataManager mDataManager;
    long mContactId;
    Contact mContact;

    public ContactDetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void getContact(long contactId) {
        mContactId = contactId;
        mDataManager.getContact(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contact -> {
                    mContact = contact;
                    getMvpView().showContact(mContact);
                }, throwable -> {
                    Timber.e(throwable, "Failed to load contact. ID=%d", contactId);
                   getMvpView().showMessage(Message.ERROR_INVALID_CONTACT, null);
                });
    }

    public void reloadContact(){
        mDataManager.getContact(mContactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contact -> {
                    mContact = contact;
                    getMvpView().showContact(mContact);
                }, throwable -> {
                    Timber.e(throwable, "Failed to load contact. ID=%d", mContactId);
                    getMvpView().showMessage(Message.ERROR_INVALID_CONTACT, null);
                });
    }

    public void deleteContact() {
        mDataManager.deleteContact(mContact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getMvpView().contactDeletedSoFinish();
                }, throwable -> {
                    Timber.e(throwable, "Error deleting contact");
                    getMvpView().showMessage(Message.ERROR_DELETING_CONTACT, null);
                });
    }

    public Contact getLoadedContact() {
        return mContact;
    }

    public ContactSortType getSortType(){
        return mDataManager.getSharedPreferences().getContactSortType();
    }

}
