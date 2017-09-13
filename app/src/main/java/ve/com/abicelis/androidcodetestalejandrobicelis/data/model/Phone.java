package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by abicelis on 9/9/2017.
 */

@Entity(tableName = "phone",
        foreignKeys = {
                @ForeignKey(entity = Contact.class, parentColumns = "contact_id", childColumns = "contact_fk", onDelete = ForeignKey.CASCADE)
        }, indices = {
                @Index("contact_fk")
})
public class Phone implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "phone_id")
    private long mId;

    @ColumnInfo(name="contact_fk")
    private long mContactId;

    @ColumnInfo(name = "phone")
    private String mPhone;

    @ColumnInfo(name = "attachment_type")
    private AttachmentType mAttachmentType;

    @ColumnInfo(name = "phone_order")
    private int mOrder;

    public Phone(long id, long contactId, String phone, AttachmentType attachmentType) {
        this(phone, attachmentType);
        mId = id;
        mContactId = contactId;
    }

    @Ignore
    public Phone(String phone, AttachmentType attachmentType) {
        mPhone = phone;
        mAttachmentType = attachmentType;
    }

    @Ignore
    public Phone() {}

    public long getId() {return mId;}
    public long getContactId() {return mContactId;}
    public String getPhone() {return mPhone;}
    public AttachmentType getAttachmentType() {return mAttachmentType;}
    public int getOrder() {return mOrder;}

    public void setId(long mId) {this.mId = mId;}
    public void setContactId(long mContactId) {this.mContactId = mContactId;}
    public void setPhone(String mPhone) {this.mPhone = mPhone;}
    public void setAttachmentType(AttachmentType attachmentType) {this.mAttachmentType = attachmentType;}
    public void setOrder(int mOrder) {this.mOrder = mOrder;}

    @Override
    public String toString() {
        return    "Phone ID="           + mId
                + "\n contact ID="      + mContactId
                + "\n phone="           + (mPhone != null ? mPhone : "NULL")
                + "\n attachmentType="  + (mAttachmentType != null ? mAttachmentType : "NULL")
                + "\n order="           + mOrder
                ;
    }

}
