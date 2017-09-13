package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by abicelis on 9/9/2017.
 */

@Dao
public interface PhoneDao {

    @Query("SELECT * FROM phone WHERE contact_fk = :contactId ORDER BY phone_order")
    Maybe<List<Phone>> getByContactId(long contactId);

    @Insert(onConflict=REPLACE)
    long insert(Phone phone);

    @Insert
    long[] insert(Phone... phones);

}
