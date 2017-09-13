package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by abicelis on 9/9/2017.
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    Maybe<List<Contact>> getAll();

    @Query("SELECT * FROM contact where contact_id = :contactId")
    Maybe<Contact> getById(long contactId);

    @Query("SELECT * FROM contact WHERE first_name LIKE :query OR last_name LIKE :query")
    Maybe<List<Contact>> find(String query);

    @Insert
    long[] insert(Contact... contacts);

    @Insert(onConflict=REPLACE)
    long insert(Contact contact);

    @Update
    int update(Contact contact);

    @Delete
    int delete(Contact contact);

    @Query("DELETE FROM contact")
    int deleteAll();
}
