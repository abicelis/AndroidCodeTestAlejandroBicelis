package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by abicelis on 9/9/2017.
 */

@Dao
public interface AddressDao {

    @Query("SELECT * FROM address WHERE contact_fk = :contactId ORDER BY address_order")
    Maybe<List<Address>> getByContactId(long contactId);

    @Insert(onConflict=REPLACE)
    long insert(Address address);

    @Insert
    long[] insert(Address... addresses);
}
