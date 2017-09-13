package ve.com.abicelis.androidcodetestalejandrobicelis.injection.application;


import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.remote.RandomUserApi;

/**
 * Created by abicelis on 9/9/2017.
 */

@Module
@ApplicationScope
public class RemoteModule {

    private static final String CONVERTER_SCALAR = "CONVERTER_SCALAR";
    private static final String CONVERTER_GSON = "CONVERTER_GSON";

    private static final String RANDOMUSER_BASE_URL = "RANDOMUSER_BASE_URL";
    private static final String RANDOMUSER_RETROFIT = "RANDOMUSER_RETROFIT";


    /**
     * Common
     */
    @Provides
    @Named(CONVERTER_SCALAR)    //Scalars Converter so Retrofit can return Strings
    Converter.Factory provideScalarConverter() {return ScalarsConverterFactory.create();}

    @Provides
    @Named(CONVERTER_GSON)
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    RxJava2CallAdapterFactory provideRxJavaFactory() {
        return RxJava2CallAdapterFactory.create();
    }




    /**
     * INJECTION GRAPH FOR RANDOM USER API
     */
    @Provides
    @Named(RANDOMUSER_BASE_URL)
    String provideRandomUserBaseUrlString() {return Constants.RANDOMUSER_BASE_URL;}

    @Provides
    @Named(RANDOMUSER_RETROFIT)
    Retrofit provideRandomUserRetrofit(@Named(CONVERTER_GSON) Converter.Factory converter,
                                  RxJava2CallAdapterFactory factory,
                                  @Named(RANDOMUSER_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(factory)
                .build();
    }

    @Provides
    RandomUserApi providesRandomUserApi(@Named(RANDOMUSER_RETROFIT) Retrofit retrofit) {
        return retrofit.create(RandomUserApi.class);
    }
}
