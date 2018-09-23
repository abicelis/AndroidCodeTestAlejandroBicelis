package ve.com.abicelis.androidcodetestalejandrobicelis.data.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.randomuserapi.RandomApiResponse;

/**
 * Created by abicelis on 9/9/2017.
 */

public interface RandomUserApi {

    @GET("?nat=us&inc=name,location,email,dob,phone,cell,picture&noinfo")
    Single<RandomApiResponse> getRandomUsers(@Query("results") int amount);

}
