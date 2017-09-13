package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home;


import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.DataManager;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactViewModel;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BasePresenter;

/**
 * Created by abicelis on 9/9/2017.
 */

public class HomePresenter extends BasePresenter<HomeMvpView> {

    private DataManager mDataManager;

    public HomePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public void refreshContactList(@Nullable String filter) {
        getMvpView().showLoading(true);
        mDataManager.getContacts(filter)
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    getMvpView().showContacts(contacts, mDataManager.getSharedPreferences().getContactSortType());
                }, throwable -> {
                    getMvpView().showLoading(false);
                    Timber.e(throwable, "Error loading contacts. ");
                    getMvpView().showMessage(Message.ERROR_LOADING_CONTACTS, null);
                });
    }

    public void changeSortType(ContactSortType sortType){
        mDataManager.getSharedPreferences().setContactSortType(sortType);
        refreshContactList(null);
    }

    public ContactSortType getSortType(){
       return mDataManager.getSharedPreferences().getContactSortType();
    }

}
