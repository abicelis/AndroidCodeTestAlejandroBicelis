package ve.com.abicelis.androidcodetestalejandrobicelis.injection.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.local.AppDatabase;

/**
 * Created by abicelis on 9/9/2017.
 */

@Module
public class LocalModule {

    private static final String ROOM_DATABASE_NAME = "ROOM_DATABASE_NAME";

    /**
     * INJECTION GRAPH FOR ROOM DATABASE
     */

    @Provides
    @Named(ROOM_DATABASE_NAME)
    String provideRoomDatabaseName() {return Constants.ROOM_DATABASE_NAME;}

    @Provides
    @ApplicationScope
    AppDatabase provideRoomAppDatabase(Application applicationContext, @Named(ROOM_DATABASE_NAME) String databaseName) {
        return Room.databaseBuilder(applicationContext,
                AppDatabase.class, databaseName).fallbackToDestructiveMigration().build();
    }

}
