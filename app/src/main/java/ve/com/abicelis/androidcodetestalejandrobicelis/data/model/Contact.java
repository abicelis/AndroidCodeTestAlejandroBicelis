package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ve.com.abicelis.androidcodetestalejandrobicelis.util.CalendarUtil;

/**
 * Created by abicelis on 9/9/2017.
 */

@Entity(tableName = "contact",
        indices = {@Index("first_name"), @Index("last_name")}
)
public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    private long mId;

    @ColumnInfo(name = "first_name")
    private String mFirstName;

    @ColumnInfo(name = "last_name")
    private String mLastName;

    @ColumnInfo(name = "date_of_birth")
    private Calendar mDateOfBirth;

    @ColumnInfo(name = "image")
    private byte[] mImage;

    @Ignore
    private List<Phone> mPhoneNumbers = new ArrayList<>();

    @Ignore
    private List<Email> mEmails = new ArrayList<>();

    @Ignore
    private List<Address> mAddresses = new ArrayList<>();

    @Ignore
    public Contact() {} //Used when creating new contact

    public Contact(long id, String firstName, String lastName, Calendar dateOfBirth, byte[] image) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
        mDateOfBirth = dateOfBirth;
        mImage = image;
    }

    public long getId() {return mId;}
    public String getFirstName() {return mFirstName;}
    public String getLastName() {return mLastName;}
    public String getFullName(ContactSortType contactSortType) {
        switch (contactSortType) {
            case BY_FIRST_NAME:
                return mFirstName + " " + mLastName;
            case BY_LAST_NAME:
                return mLastName + " " + mFirstName;
        }
        throw new InvalidParameterException("Invalid ContactSortType");
    }
    public Calendar getDateOfBirth() {return mDateOfBirth;}
    public byte[] getImage() {return mImage;}
    public List<Phone> getPhoneNumbers() {return mPhoneNumbers;}
    public List<Email> getEmails() {return mEmails;}
    public List<Address> getAddresses() {return mAddresses;}

    public void setId(long mId) {this.mId = mId;}
    public void setFirstName(String mFirstName) {this.mFirstName = mFirstName;}
    public void setLastName(String mLastName) {this.mLastName = mLastName;}
    public void setDateOfBirth(Calendar mDateOfBirth) {this.mDateOfBirth = mDateOfBirth;}
    public void setImage(byte[] mImage) {this.mImage = mImage;}
    public void setPhoneNumbers(List<Phone> phoneNumbers) {this.mPhoneNumbers = phoneNumbers;}
    public void setEmails(List<Email> emails) {this.mEmails = emails;}
    public void setAddresses(List<Address> addresses) {this.mAddresses = addresses;}

    public void addPhoneNumber(Phone phoneNumber) {mPhoneNumbers.add(phoneNumber);}
    public void addEmail(Email email) {mEmails.add(email);}
    public void addAddress(Address address) {mAddresses.add(address);}

    @Override
    public String toString() {
        String out =  "Contact ID="     + mId
                + "\n first name="   + (mFirstName != null ? mFirstName : "NULL")
                + "\n last name="    + (mLastName != null ? mLastName : "NULL")
                + "\n date of birth="+ (mDateOfBirth != null ? CalendarUtil.getStringDateFromCalendar(mDateOfBirth) : "NULL")
                + "\n image="        + (mImage != null ? mImage.length : "NULL")
                ;

        if(mPhoneNumbers != null) {
            for (Phone p : mPhoneNumbers)
                out += "\n   PhoneNumber=" + p.toString();
        } else
            out = " NULL";

        if(mEmails != null) {
            for (Email e : mEmails)
                out += "\n   Email=" + e.toString();
        } else
            out = " NULL";

        if(mAddresses != null) {
            for (Address a : mAddresses)
                out += "\n   Address=" + a.toString();
        } else
            out = " NULL";


        return out;
    }

    public String toPrettyString() {
        String out = String.format(Locale.getDefault(), "%1$s %2$s:", mFirstName, mLastName);
        if(mPhoneNumbers.size() > 0) {
            for (Phone p : mPhoneNumbers)
                out += String.format(Locale.getDefault(), "\n%1$s phone: %2$s", p.getAttachmentType().getFriendlyName(), p.getPhone());
        }
        if(mEmails.size() > 0) {
            for (Email e : mEmails)
                out += String.format(Locale.getDefault(), "\n%1$s email: %2$s", e.getAttachmentType().getFriendlyName(), e.getEmail());
        }
        if(mAddresses.size() > 0) {
            for (Address a : mAddresses)
                out += String.format(Locale.getDefault(), "\n%1$s address:\n%2$s", a.getAttachmentType().getFriendlyName(), a.toPrettyString());
        }
//
//        out += String.format(Locale.getDefault(), "%1$s %2$s:", mFirstName, mLastName);
//
//        String phone = (mPhoneNumbers.size() > 0 ? mPhoneNumbers.get(0).getPhone() : "None");
//        String email = (mEmails.size() > 0 ? mEmails.get(0).getEmail() : "None");
//        return String.format(Locale.getDefault(), "%1$s %2$s:\nPhone: %3$s\nEmail: %4$s", mFirstName, mLastName, phone, email);
        return out;
    }
}
