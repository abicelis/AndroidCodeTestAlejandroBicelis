package ve.com.abicelis.androidcodetestalejandrobicelis.ui.base;

import android.support.annotation.UiThread;
import android.support.v4.app.DialogFragment;

import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;
import ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter.PresenterComponent;
import ve.com.abicelis.androidcodetestalejandrobicelis.injection.presenter.PresenterModule;

/**
 * Created by abicelis on 9/9/2017.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    @UiThread
    protected PresenterComponent getPresenterComponent() {
        return ((AndroidCodeTestApplication)getActivity().getApplication())
                .getApplicationComponent()
                .newPresenterComponent(new PresenterModule(getActivity()));
    }

}
