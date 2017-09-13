package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;


import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;

/**
 * Created by abicelis on 9/9/2017.
 */

public enum ContactSortType {
    BY_FIRST_NAME(R.string.contact_sort_type_first_name),
    BY_LAST_NAME(R.string.contact_sort_type_last_name);

    @StringRes
    int mFriendlyName;

    ContactSortType(@StringRes int friendlyName) {
        mFriendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return AndroidCodeTestApplication.getAppContext().getString(mFriendlyName);
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (ContactSortType x : values()) {
            friendlyValues.add(AndroidCodeTestApplication.getAppContext().getString(x.mFriendlyName));
        }
        return friendlyValues;
    }
}