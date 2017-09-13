package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home;


import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactViewModel;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.MvpView;

/**
 * Created by abicelis on 9/9/2017.
 */

public interface HomeMvpView extends MvpView {

   void showLoading(boolean loading);
   void showContacts(List<ContactViewModel> contacts, ContactSortType sortType);
}
