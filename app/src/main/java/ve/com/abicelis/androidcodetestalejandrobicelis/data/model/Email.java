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

@Entity(tableName = "email",
        foreignKeys = {
                @ForeignKey(entity = Contact.class, parentColumns = "contact_id", childColumns = "contact_fk", onDelete = ForeignKey.CASCADE)
        }, indices = {
        @Index("contact_fk")
})
public class Email implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "email_id")
    private long mId;

    @ColumnInfo(name="contact_fk")
    private long mContactId;

    @ColumnInfo(name = "email")
    private String mEmail;

    @ColumnInfo(name = "attachment_type")
    private AttachmentType mAttachmentType;

    @ColumnInfo(name = "email_order")
    private int mOrder;

    public Email(long id, long contactId, String email, AttachmentType attachmentType) {
        this(email, attachmentType);
        mId = id;
        mContactId = contactId;
    }

    @Ignore
    public Email(String email, AttachmentType attachmentType) {
        mEmail = email;
        mAttachmentType = attachmentType;
    }

    @Ignore
    public Email() {}

    public long getId() {return mId;}
    public long getContactId() {return mContactId;}
    public String getEmail() {return mEmail;}
    public AttachmentType getAttachmentType() {return mAttachmentType;}
    public int getOrder() {return mOrder;}

    public void setId(long mId) {this.mId = mId;}
    public void setContactId(long mContactId) {this.mContactId = mContactId;}
    public void setEmail(String mEmail) {this.mEmail = mEmail;}
    public void setAttachmentType(AttachmentType attachmentType) {this.mAttachmentType = attachmentType;}
    public void setOrder(int mOrder) {this.mOrder = mOrder;}

    @Override
    public String toString() {
        return    "Email ID="     + mId
                + "\n contact ID=" + mContactId
                + "\n email="      + (mEmail != null ? mEmail : "NULL")
                ;
    }
}
