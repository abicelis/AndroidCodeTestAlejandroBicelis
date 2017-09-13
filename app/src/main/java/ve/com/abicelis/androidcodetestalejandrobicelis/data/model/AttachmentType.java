package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import android.support.annotation.StringRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;

/**
 * Created by abicelis on 9/9/2017.
 */

public enum AttachmentType implements Serializable {
    HOME(R.string.attachment_type_home),
    WORK(R.string.attachment_type_work),
    OTHER(R.string.attachment_type_other);

    @StringRes int mFriendlyName;

    AttachmentType(@StringRes int friendlyName) {
        mFriendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return AndroidCodeTestApplication.getAppContext().getString(mFriendlyName);
    }

    public static List<String> getFriendlyNames() {
        List<String> friendlyValues = new ArrayList<>();
        for (AttachmentType x : values()) {
            friendlyValues.add(AndroidCodeTestApplication.getAppContext().getString(x.mFriendlyName));
        }
        return friendlyValues;
    }
}
