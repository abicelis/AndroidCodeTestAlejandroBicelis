package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.dialog;


import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.DataManager;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BasePresenter;

/**
 * Created by abicelis on 9/9/2017.
 */

public class AddRandomContactsPresenter extends BasePresenter<AddRandomContactsMvpView> {

    private DataManager mDataManager;
    private int mAmount;

    public AddRandomContactsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void setSelectedAmount(int amount) {
        mAmount = amount;
    }

    public void insertRandomContacts() {
        getMvpView().updateDialogState(AddRandomContactsDialogFragment.DialogState.STATE_FETCHING);
        addDisposable(mDataManager.getRandomContacts(mAmount)
                .delay(200, TimeUnit.MILLISECONDS)          //To see the effect
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    getMvpView().updateDialogState(AddRandomContactsDialogFragment.DialogState.STATE_SAVING);
                    mDataManager.saveContacts(contacts)
                            .delay(200, TimeUnit.MILLISECONDS)          //To see the effect
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                getMvpView().contactsSavedSoDismiss();
                            }, throwable -> {
                                Timber.e(throwable, "Error saving contacts");
                                getMvpView().showMessage(Message.ERROR_SAVING_CONTACTS, null);
                                getMvpView().updateDialogState(AddRandomContactsDialogFragment.DialogState.STATE_IDLE);
                            });

                }, throwable -> {
                    Timber.e(throwable, "Error fetching contacts");
                    getMvpView().showMessage(Message.ERROR_FETCHING_CONTACTS, null);
                    getMvpView().updateDialogState(AddRandomContactsDialogFragment.DialogState.STATE_IDLE);
                    //TODO reset view or send a callable and then kill dialog.. or something
                }));
    }
}
