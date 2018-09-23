package ve.com.abicelis.androidcodetestalejandrobicelis.application;

/**
 * Created by abicelis on 9/9/2017.
 * App-wide constants file
 * Element	                Field Name Prefix
 * SharedPreferences	    PREF_
 * Bundle	                BUNDLE_
 * Fragment Arguments	    ARGUMENT_
 * Intent Extra	            EXTRA_
 * Intent Action	        ACTION_
 * Permissions  	        PERMISSION_
 */

public class Constants {


    /* IMAGE EDIT ACTIVITY */
    public static final String  EXTRA_IMAGE_EDIT_ACTIVITY_IMAGE_FILENAME = "EXTRA_IMAGE_EDIT_ACTIVITY_IMAGE_FILENAME";
    public static final int     PERMISSION_IMAGE_EDIT_ACTIVITY = 201;
    public static final int     REQUEST_IMAGE_CAPTURE = 123;
    public static final int     REQUEST_PICK_IMAGE_GALLERY = 124;


    /* CONTACT EDIT ACTIVITY */
    public static final String  EXTRA_CONTACT_EDIT_ACTIVITY_CONTACT_ID = "EXTRA_CONTACT_EDIT_ACTIVITY_CONTACT_ID";
    public static final String  CONTACT_EDIT_ACTIVITY_MAX_CONTACT_ID = "EXTRA_CONTACT_EDIT_ACTIVITY_CONTACT_ID";
    public static final int     REQUEST_ADD_CONTACT_IMAGE = 122;



    /* CONTACT DETAIL ACTIVITY */
    public static final String  EXTRA_CONTACT_DETAIL_ACTIVITY_CONTACT_ID = "EXTRA_CONTACT_DETAIL_ACTIVITY_CONTACT_ID";


    /* MISC */
    public static final int     IMAGE_JPEG_COMPRESSION_PERCENTAGE = 70;
    public static final int     IMAGE_JPEG_MAX_SIZE_PX = 600;
    public static final String  IMAGE_FILE_EXTENSION = ".jpg";
    public static final String  IMAGE_FILES_DIR = "recipe/image";
    public static final String  IMAGE_FILENAMES_SEPARATOR = "|";


    /* SHARED PREFERENCES */
    public static final String  SHARED_PREFERENCE_APP_THEME_TYPE = "SHARED_PREFERENCE_APP_THEME_TYPE";
    public static final String  SHARED_PREFERENCE_CONTACT_SORT_TYPE = "SHARED_PREFERENCE_CONTACT_SORT_TYPE";

    /* ROOM DATABASE */
    public static final String ROOM_DATABASE_NAME = "android_code_test_app.db";
    public static final String ROOM_DATABASE_CALENDAR_CONVERTER_SEPARATOR = "!!!";
    public static final int ROOM_DATABASE_CONTACT_FIRST_NAME_MAX_LENGTH = 25;
    public static final int ROOM_DATABASE_CONTACT_LAST_NAME_MAX_LENGTH = 25;
    public static final int ROOM_DATABASE_CONTACT_PHONE_MAX_LENGTH = 20;
    public static final int ROOM_DATABASE_CONTACT_EMAIL_MAX_LENGTH = 30;
    public static final int ROOM_DATABASE_CONTACT_ADDRESS_STREET_MAX_LENGTH = 40;
    public static final int ROOM_DATABASE_CONTACT_ADDRESS_CITY_MAX_LENGTH = 20;
    public static final int ROOM_DATABASE_CONTACT_ADDRESS_COUNTRY_MAX_LENGTH = 20;
    public static final int ROOM_DATABASE_CONTACT_ADDRESS_POSTAL_CODE_MAX_LENGTH = 10;


    /* RANDOM USER API */
    public static final String RANDOMUSER_BASE_URL = "https://randomuser.me/api/1.2/";      //Added /1.2/ to prevent app breaking if RandomUser.me decides to change stuff again.

}
