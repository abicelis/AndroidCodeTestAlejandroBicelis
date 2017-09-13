package ve.com.abicelis.androidcodetestalejandrobicelis.application;

import android.content.Context;
import android.support.annotation.StringRes;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.SnackbarUtil;


/**
 * Created by abicelis on 9/9/2017.
 * App-wide Messages
 * ERROR_
 * NOTICE_
 * SUCCESS_
 *
 */

public enum Message {
    ERROR_LOADING_CONTACTS(R.string.error_loading_contacts, SnackbarUtil.SnackbarType.ERROR),
    ERROR_INVALID_CONTACT(R.string.error_invalid_contact, SnackbarUtil.SnackbarType.ERROR),
    ERROR_SAVING_CONTACTS(R.string.error_saving_contacts, SnackbarUtil.SnackbarType.ERROR),
    ERROR_SAVING_CONTACT(R.string.error_saving_contact, SnackbarUtil.SnackbarType.ERROR),
    ERROR_FETCHING_CONTACTS(R.string.error_fetching_contacts, SnackbarUtil.SnackbarType.ERROR),
    ERROR_DELETING_CONTACT(R.string.error_deleting_contact, SnackbarUtil.SnackbarType.ERROR),
    ERROR_PERMISSIONS_NOT_GRANTED(R.string.error_permissions_not_granted, SnackbarUtil.SnackbarType.ERROR),
    ERROR_NO_CAMERA_INSTALLED(R.string.error_no_camera_installed, SnackbarUtil.SnackbarType.ERROR),
    ERROR_REQUESTING_CAMERA(R.string.error_requesting_camera, SnackbarUtil.SnackbarType.ERROR),
    ERROR_REQUESTING_IMAGE_GALLERY(R.string.error_requesting_image_gallery, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CREATING_IMAGE(R.string.error_creating_image, SnackbarUtil.SnackbarType.ERROR),
    ERROR_SAVING_IMAGE(R.string.error_saving_image, SnackbarUtil.SnackbarType.ERROR),

    ERROR_CHECKING_CONTACT_INVALID_FIRST_NAME(R.string.error_checking_contact_invalid_first_name, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_CONTACT_INVALID_LAST_NAME(R.string.error_checking_contact_invalid_last_name, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_CONTACT_INVALID_DOB(R.string.error_checking_contact_invalid_dob, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_CONTACT_NO_PHONES(R.string.error_checking_contact_no_phones, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_CONTACT_NO_EMAILS(R.string.error_checking_contact_no_emails, SnackbarUtil.SnackbarType.ERROR),

    ERROR_CHECKING_PHONE_INVALID_NUMBER(R.string.error_checking_phone_invalid_number, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_EMAIL_INVALID_EMAIL(R.string.error_checking_email_invalid_email, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_ADDRESS_INVALID_STREET(R.string.error_checking_address_invalid_street, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_ADDRESS_INVALID_CITY(R.string.error_checking_address_invalid_city, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_ADDRESS_INVALID_COUNTRY(R.string.error_checking_address_invalid_country, SnackbarUtil.SnackbarType.ERROR),
    ERROR_CHECKING_ADDRESS_INVALID_POSTAL_CODE(R.string.error_checking_address_invalid_postal_code, SnackbarUtil.SnackbarType.ERROR),


    NOTICE_APP_THEME_TOGGLED(R.string.info_app_theme_toggle_message, SnackbarUtil.SnackbarType.SUCCESS),
    NOTICE_ADDRESS_COPIED_CLIPBOARD(R.string.notice_address_copied_clipboard, SnackbarUtil.SnackbarType.SUCCESS),

    SUCCESS_SAVING_IMAGE(R.string.success_saving_image, SnackbarUtil.SnackbarType.SUCCESS),

    ;

    @StringRes
    int friendlyName;
    SnackbarUtil.SnackbarType messageType;

    Message(@StringRes int friendlyName, SnackbarUtil.SnackbarType messageType) {
        this.friendlyName = friendlyName;
        this.messageType = messageType;
    }


    public @StringRes int getFriendlyNameRes() {
        return friendlyName;
    }
    public SnackbarUtil.SnackbarType getMessageType() {
        return messageType;
    }
    public String getFriendlyName(Context context) {
        return AndroidCodeTestApplication.getAppContext().getString(friendlyName);
    }

}
