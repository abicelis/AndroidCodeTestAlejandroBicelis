package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import java.security.InvalidParameterException;

/**
 * Created by abicelis on 9/9/2017.
 * Helper class to list contacts and headers for Home view recycler.
 */

public class ContactViewModel {

    private Contact mContact;
    private String mHeader;
    private ContactViewModelType mType;

    public ContactViewModel(Contact contact) {
        mType = ContactViewModelType.CONTACT;
        mContact = contact;
    }

    public ContactViewModel(String header) {
        mType = ContactViewModelType.HEADER;
        mHeader = header;
    }


    public String getHeader() {return mHeader;}
    public Contact getContact() {return mContact;}
    public ContactViewModelType getType() {return mType;}

    public enum ContactViewModelType { HEADER, CONTACT }
}
