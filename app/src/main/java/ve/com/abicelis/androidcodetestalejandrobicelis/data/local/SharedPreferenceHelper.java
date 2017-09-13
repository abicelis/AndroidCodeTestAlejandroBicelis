package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.AppThemeType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;

/**
 * Created by abice on 1/4/2017.
 */

public class SharedPreferenceHelper {

    private SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferenceHelper(Context applicationContext) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }


    /* APP THEME TYPE */
    public AppThemeType getAppThemeType() {
        String value = mSharedPreferences.getString(Constants.SHARED_PREFERENCE_APP_THEME_TYPE, null);
        AppThemeType pref;
        try {
            pref = AppThemeType.valueOf(value);
        } catch (Exception e) {
            pref = null;
        }

        if(pref == null) {
            Timber.d("getAppThemeType() found null, setting DARK");
            pref = AppThemeType.DARK;
            setAppThemeType(pref);
        }

        return pref;
    }
    public AppThemeType toggleAppThemeType() {
        AppThemeType appTheme = getAppThemeType();
        appTheme = (appTheme==AppThemeType.DARK ? AppThemeType.LIGHT : AppThemeType.DARK);
        mSharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_APP_THEME_TYPE, appTheme.name()).apply();
        return appTheme;
    }
    public void setAppThemeType(AppThemeType value) {
        mSharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_APP_THEME_TYPE, value.name()).apply();
    }


    /* CONTACT SORT TPYE */
    public ContactSortType getContactSortType() {
        String value = mSharedPreferences.getString(Constants.SHARED_PREFERENCE_CONTACT_SORT_TYPE, null);
        ContactSortType pref;
        try {
            pref = ContactSortType.valueOf(value);
        } catch (Exception e) {
            pref = null;
        }

        if(pref == null) {
            Timber.d("getContactSortType() found null, setting BY_FIRST_NAME");
            pref = ContactSortType.BY_FIRST_NAME;
            setContactSortType(pref);
        }

        return pref;
    }
    public ContactSortType toggleContactSortType() {
        ContactSortType contactSortType = getContactSortType();
        contactSortType = (contactSortType==ContactSortType.BY_FIRST_NAME ? ContactSortType.BY_LAST_NAME : ContactSortType.BY_FIRST_NAME);
        mSharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_CONTACT_SORT_TYPE, contactSortType.name()).apply();
        return contactSortType;
    }
    public void setContactSortType(ContactSortType value) {
        mSharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_CONTACT_SORT_TYPE, value.name()).apply();
    }

}
