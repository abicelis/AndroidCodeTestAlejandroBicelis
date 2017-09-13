package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.DataManager;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BasePresenter;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.CalendarUtil;

/**
 * Created by abicelis on 11/9/2017.
 */

public class ContactEditPresenter extends BasePresenter<ContactEditMvpView> {

    //DATA
    private DataManager mDataManager;
    private Contact mContact;
    private boolean mEditingContact;

    public ContactEditPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    public void restoreState(Contact contact, boolean editingContact) {
        mContact = contact;
        mEditingContact = editingContact;

        getMvpView().showContact(mContact);
        linkAdaptersToContact();
    }
    public void setExisting(long contactId){
        mDataManager.getContact(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contact -> {
                    mContact = contact;
                    mEditingContact = true;
                    getMvpView().showContact(mContact);
                    linkAdaptersToContact();
                }, throwable -> {
                    Timber.e(throwable, "Error getting contact from db, invalid? ID=%d", contactId);
                    getMvpView().showMessage(Message.ERROR_INVALID_CONTACT, null);
                });
    }

    public void creatingNew(){
        mEditingContact = false;
        mContact = new Contact();
        linkAdaptersToContact();
    }

    private void linkAdaptersToContact() {
        getMvpView().getPhoneAdapter().setItems(mContact.getPhoneNumbers());
        getMvpView().getEmailAdapter().setItems(mContact.getEmails());
        getMvpView().getAddressAdapter().setItems(mContact.getAddresses());
    }



    public boolean isEditing() {
        return mEditingContact;
    }

    public void setContactDateOfBirth(int year, int monthOfYear, int dayOfMonth) {
        if(mContact.getDateOfBirth() == null) {
            mContact.setDateOfBirth(CalendarUtil.getNewInstanceZeroedCalendar());
        }
        mContact.getDateOfBirth().set(year, monthOfYear, dayOfMonth);
    }

    public Contact getLoadedContact() {
        return mContact;
    }

    public void setContactImage(byte[] imageBytes) {
        mContact.setImage(imageBytes);
    }


    public void saveContact(String firstName, String lastName) {
        mContact.setFirstName(firstName.trim());
        mContact.setLastName(lastName.trim());

        Message message = checkContact();
        if(message == null) {
            mDataManager.saveContact(mContact)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        getMvpView().contactSavedSoFinish();
                    }, throwable -> {
                        Timber.e(throwable, "Error saving contact");
                        getMvpView().showMessage(Message.ERROR_SAVING_CONTACTS, null);
                    });

        } else {
            getMvpView().showMessage(message, null);
        }
    }

    public Message checkContact() {
        if(mContact.getFirstName().isEmpty())
            return Message.ERROR_CHECKING_CONTACT_INVALID_FIRST_NAME;
        if(mContact.getLastName().isEmpty())
            return Message.ERROR_CHECKING_CONTACT_INVALID_LAST_NAME;
        if(mContact.getDateOfBirth() == null)
            return Message.ERROR_CHECKING_CONTACT_INVALID_DOB;
        if(mContact.getPhoneNumbers().size() == 0)
            return Message.ERROR_CHECKING_CONTACT_NO_PHONES;
        if(mContact.getEmails().size() == 0)
            return Message.ERROR_CHECKING_CONTACT_NO_EMAILS;

        return null;
    }

}
