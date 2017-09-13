package ve.com.abicelis.androidcodetestalejandrobicelis.data.model;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;

/**
 * Created by abicelis on 9/9/2017.
 */

public enum RandomContactAmount {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50)
    ;

    int mAmount;

    RandomContactAmount(int amount) {
        mAmount = amount;
    }

    public int getAmount() {return mAmount;}

    public static List<String> getStringList() {
        List<String> friendlyValues = new ArrayList<>();
        for (RandomContactAmount x : values()) {
            friendlyValues.add(String.valueOf(x.mAmount));
        }
        return friendlyValues;
    }
}
