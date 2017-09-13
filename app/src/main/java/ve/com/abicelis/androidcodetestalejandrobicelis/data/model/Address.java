package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by abicelis on 9/9/2017.
 */

@Entity(tableName = "address",
        foreignKeys = {
                @ForeignKey(entity = Contact.class, parentColumns = "contact_id", childColumns = "contact_fk", onDelete = ForeignKey.CASCADE)
        }, indices = {
        @Index("contact_fk")
})
public class Address implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "address_id")
    private long mId;

    @ColumnInfo(name="contact_fk")
    private long mContactId;

    @ColumnInfo(name = "street")
    private String mStreet;

    @ColumnInfo(name = "city")
    private String mCity;

    @ColumnInfo(name = "country")
    private String mCountry;

    @ColumnInfo(name = "postal_code")
    private String mPostalCode;

    @ColumnInfo(name = "attachment_type")
    private AttachmentType mAttachmentType;

    @ColumnInfo(name = "address_order")
    private int mOrder;


    public Address(long id, long contactId, String street, String city, String country, String postalCode, AttachmentType attachmentType) {
        this(street, city, country, postalCode, attachmentType);
        mId = id;
        mContactId = contactId;
    }

    @Ignore
    public Address(String street, String city, String country, String postalCode, AttachmentType attachmentType) {
        mStreet = street;
        mCity = city;
        mCountry = country;
        mPostalCode = postalCode;
        mAttachmentType = attachmentType;
    }

    @Ignore
    public Address(){}


    public long getId() {return mId;}
    public long getContactId() {return mContactId;}
    public String getStreet() {return mStreet;}
    public String getCity() {return mCity;}
    public String getCountry() {return mCountry;}
    public String getPostalCode() {return mPostalCode;}
    public AttachmentType getAttachmentType() {return mAttachmentType;}
    public int getOrder() {return mOrder;}

    public void setId(long mId) {this.mId = mId;}
    public void setContactId(long mContactId) {this.mContactId = mContactId;}
    public void setStreet(String mStreet) {this.mStreet = mStreet;}
    public void setCity(String mCity) {this.mCity = mCity;}
    public void setCountry(String mCountry) {this.mCountry = mCountry;}
    public void setPostalCode(String mPostalCode) {this.mPostalCode = mPostalCode;}
    public void setAttachmentType(AttachmentType attachmentType) {this.mAttachmentType = attachmentType;}
    public void setOrder(int mOrder) {this.mOrder = mOrder;}

    @Override
    public String toString() {
        return    "Address ID="     + mId
                + "\n street="       + (mStreet != null ? mStreet : "NULL")
                + "\n city="         + (mCity != null ? mCity : "NULL")
                + "\n country="      + (mCountry != null ? mCountry : "NULL")
                + "\n postal code="  + (mPostalCode != null ? mPostalCode : "NULL")
                ;
    }

    public String toPrettyString() {
        return    (mStreet != null ? " "+mStreet : "")
                + (mCity != null ? "\n"+mCity : "")
                + (mCountry != null ? ", "+mCountry : "")
                + (mPostalCode != null ? " ("+mPostalCode+")" : "")
                ;
    }
}
