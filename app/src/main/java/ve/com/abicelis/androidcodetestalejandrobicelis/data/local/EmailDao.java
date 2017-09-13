package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by abicelis on 9/9/2017.
 */

@Dao
public interface EmailDao {

    @Query("SELECT * FROM email WHERE contact_fk = :contactId ORDER BY email_order")
    Maybe<List<Email>> getByContactId(long contactId);

    @Insert(onConflict=REPLACE)
    long insert(Email email);

    @Insert
    long[] insert(Email... emails);
}
