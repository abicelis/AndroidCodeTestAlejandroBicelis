package ve.com.abicelis.androidcodetestalejandrobicelis.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;

import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.local.SharedPreferenceHelper;
import ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter.PresenterComponent;
import ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter.PresenterModule;

/**
 * Created by abicelis on 9/9/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSavedAppTheme();
    }

    @UiThread
    protected PresenterComponent getPresenterComponent() {
        return ((AndroidCodeTestApplication)getApplication())
                .getApplicationComponent()
                .newPresenterComponent(new PresenterModule(this));
    }


    protected void setSavedAppTheme(){
        setTheme(new SharedPreferenceHelper(this).getAppThemeType().getTheme());
    }

}
