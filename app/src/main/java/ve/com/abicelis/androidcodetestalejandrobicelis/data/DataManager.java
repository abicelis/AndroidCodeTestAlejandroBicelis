package ve.com.abicelis.androidcodetestalejandrobicelis.data;

import android.net.ParseException;
import android.support.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.local.AppDatabase;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.local.SharedPreferenceHelper;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.AttachmentType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactComparator;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactViewModel;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.randomuserapi.RandomApiResponse;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.remote.RandomUserApi;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.StringUtil;

/**
 * Created by abicelis on 9/9/2017.
 */

public class DataManager {

    private AppDatabase mAppDatabase;
    private RandomUserApi mRandomUserApi;
    private SharedPreferenceHelper mSharedPreferencesHelper;

    @Inject
    public DataManager(AppDatabase appDatabase, RandomUserApi randomUserApi, SharedPreferenceHelper sharedPreferenceHelper) {
        mAppDatabase = appDatabase;
        mRandomUserApi = randomUserApi;
        mSharedPreferencesHelper = sharedPreferenceHelper;
    }

    public SharedPreferenceHelper getSharedPreferences() {
        return mSharedPreferencesHelper;
    }

    public Maybe<List<ContactViewModel>> getContacts(@Nullable String filter) {
        if(filter == null)
            filter = "%";
        else
            filter = "%"+filter+"%";

        return mAppDatabase.contactDao().find(filter)
                .map(contacts -> {

                    //Set data from other tables: Phones, Emails and Addresses
                    for (Contact c : contacts) {
                        c.setPhoneNumbers(mAppDatabase.phoneDao().getByContactId(c.getId()).blockingGet());
                        c.setEmails(mAppDatabase.emailDao().getByContactId(c.getId()).blockingGet());
                        c.setAddresses(mAppDatabase.addressDao().getByContactId(c.getId()).blockingGet());
                    }

                    //Sort contacts according to contactSortType
                    ContactComparator comparator = new ContactComparator(mSharedPreferencesHelper.getContactSortType());
                    Collections.sort(contacts, comparator);

                    //Convert into List<ContactViewModel
                    List<ContactViewModel> contactsVM = new ArrayList<>();
                    String lastStartingLetter = null;
                    for (Contact c : contacts) {
                        String startingLetter = c.getFullName(mSharedPreferencesHelper.getContactSortType()).trim().toLowerCase().substring(0, 1);

                        if(!startingLetter.equals(lastStartingLetter)) {
                            contactsVM.add(new ContactViewModel(startingLetter.toUpperCase()));
                            lastStartingLetter = startingLetter;
                        }

                        contactsVM.add(new ContactViewModel(c));
                    }

                    return contactsVM;
                });
    }

    public Maybe<Contact> getContact(long contactId) {
        return mAppDatabase.contactDao().getById(contactId)
                .map(c -> {

                    //Set data from other tables: Phones, Emails and Addresses
                    c.setPhoneNumbers(mAppDatabase.phoneDao().getByContactId(c.getId()).blockingGet());
                    c.setEmails(mAppDatabase.emailDao().getByContactId(c.getId()).blockingGet());
                    c.setAddresses(mAppDatabase.addressDao().getByContactId(c.getId()).blockingGet());
                    return c;
                });
    }

    public Single<List<Contact>> getRandomContacts(int amount) {
        return mRandomUserApi.getRandomUsers(amount)
                .map(randomApiResponse -> {
                    List<Contact> contacts = new ArrayList<>();
                    Random rand = new Random();


                    for (RandomApiResponse.RandomApiResult r : randomApiResponse.getResults()) {
                        Phone phone = new Phone(r.getPhone(), AttachmentType.HOME);
                        Email email = new Email(r.getEmail(), AttachmentType.WORK);
                        Address address = null;

                        //Add address only to half of the contacts
                        int a = rand.nextInt(2);
                        if(a == 1) {
                            address = new Address(r.getLocation().getStreet(),
                                    r.getLocation().getCity(),
                                    r.getLocation().getState(),
                                    r.getLocation().getPostCode(), AttachmentType.HOME);
                        }

                        Calendar dateOfBirth = Calendar.getInstance();
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                            dateOfBirth.setTime(sdf.parse(r.getDob().getDate()));
                        } catch (ParseException e) { /*Just skip the thing*/ }

                        byte[] image = null;
                        try {
                            image = ImageUtil.toCompressedByteArray(
                                    Picasso.with(AndroidCodeTestApplication.getAppContext())
                                            .load(r.getPicture().getLarge())
                                            .get(), Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
                        } catch (ParseException e) { /*Just skip the thing*/ }

                        Contact c = new Contact(0,
                                StringUtil.startWithUppercase(r.getName().getFirst()),
                                StringUtil.startWithUppercase(r.getName().getLast()),
                                dateOfBirth,
                                image);

                        c.addPhoneNumber(phone);
                        c.addEmail(email);
                        if(address != null) {
                            c.addAddress(address);
                        }
                        contacts.add(c);
                    }
                    return contacts;
                });
    }

    public Completable saveContacts(List<Contact> contacts) {
        return Completable.fromAction(() -> {
                    long[] ids = mAppDatabase.contactDao().insert(contacts.toArray(new Contact[contacts.size()]));
                    for (int i = 0; i < contacts.size(); i++) {

                        //Insert Phones
                        int order = 0;
                        for (Phone phone : contacts.get(i).getPhoneNumbers()) {
                            if(phone != null) {
                                phone.setContactId(ids[i]);
                                phone.setOrder(order);
                                mAppDatabase.phoneDao().insert(phone);
                                order++;
                            }
                        }

                        //Insert emails
                        order = 0;
                        for (Email email : contacts.get(i).getEmails()) {
                            if(email != null) {
                                email.setContactId(ids[i]);
                                email.setOrder(order);
                                mAppDatabase.emailDao().insert(email);
                                order++;
                            }
                        }

                        //Insert addresses
                        order = 0;
                        for (Address address : contacts.get(i).getAddresses()) {
                            if(address != null) {
                                address.setContactId(ids[i]);
                                address.setOrder(order);
                                mAppDatabase.addressDao().insert(address);
                                order++;
                            }
                        }

                    }
                }
        );
    }

    public Completable saveContact(Contact contact) {
        return Completable.fromAction(() -> {
                    long id = mAppDatabase.contactDao().insert(contact);

                    //Insert Phones
                    int order = 0;
                    for (Phone phone : contact.getPhoneNumbers()) {
                        if(phone != null) {
                            phone.setContactId(id);
                            phone.setOrder(order);
                            mAppDatabase.phoneDao().insert(phone);
                            order++;
                        }
                    }

                    //Insert emails
                    order = 0;
                    for (Email email : contact.getEmails()) {
                        if(email != null) {
                            email.setContactId(id);
                            email.setOrder(order);
                            mAppDatabase.emailDao().insert(email);
                            order++;
                        }
                    }

                    //Insert addresses
                    order = 0;
                    for (Address address : contact.getAddresses()) {
                        if(address != null) {
                            address.setContactId(id);
                            address.setOrder(order);
                            mAppDatabase.addressDao().insert(address);
                            order++;
                        }
                    }


                }
        );
    }

    public Completable deleteContact(Contact contact) {
        return Completable.fromAction(() -> {
            mAppDatabase.contactDao().delete(contact);
        });
    }
}
