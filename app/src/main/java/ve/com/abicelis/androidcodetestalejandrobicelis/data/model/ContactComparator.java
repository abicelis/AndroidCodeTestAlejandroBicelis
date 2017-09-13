package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import java.util.Comparator;

/**
 * Created by abicelis on 9/9/2017.
 */

public class ContactComparator implements Comparator<Contact> {

    private ContactSortType mContactSortType;

    public ContactComparator(ContactSortType contactSortType) {
        mContactSortType = contactSortType;
    }

    @Override
    public int compare(Contact contact, Contact contact2) {
        if (contact == null)
            return 1;
        if (contact2 == null)
            return -1;
        return contact.getFullName(mContactSortType).toLowerCase().compareTo(contact2.getFullName(mContactSortType).toLowerCase());
    }
}
