package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

/**
 * Created by abicelis on 9/9/2017.
 */

@Database(entities = {Contact.class, Phone.class, Email.class, Address.class}, version = 15)
@TypeConverters({CalendarConverter.class, AttachmentTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract PhoneDao phoneDao();
    public abstract EmailDao emailDao();
    public abstract AddressDao addressDao();
}
