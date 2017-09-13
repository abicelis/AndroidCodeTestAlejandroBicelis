package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.dialog;


import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.MvpView;

/**
 * Created by abicelis on 9/9/2017.
 */

public interface AddRandomContactsMvpView extends MvpView {

    void updateDialogState(AddRandomContactsDialogFragment.DialogState state);
    void contactsSavedSoDismiss();
}
