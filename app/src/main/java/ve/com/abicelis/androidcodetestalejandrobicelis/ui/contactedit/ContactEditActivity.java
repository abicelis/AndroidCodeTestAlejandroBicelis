package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.customviews.CustomEditText;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs.AddOrEditAddressDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs.AddOrEditEmailDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs.AddOrEditPhoneDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.itemTouchHelper.SimpleItemTouchHelperCallback;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.imageedit.ImageEditActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.CalendarUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.FileUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.SnackbarUtil;

/**
 * Created by abicelis on 11/9/2017.
 */

public class ContactEditActivity extends BaseActivity implements
        ContactEditMvpView,
        EditPhoneAdapter.OnDragStartListener,
        EditEmailAdapter.OnDragStartListener,
        EditAddressAdapter.OnDragStartListener {


    //CONSTS
    private static final String STATE_CONTACT = "STATE_CONTACT";
    private static final String STATE_EDITING_CONTACT = "STATE_EDITING_CONTACT";

    @Inject
    ContactEditPresenter mPresenter;

    @BindView(R.id.activity_contact_edit_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_contact_edit_container)
    LinearLayout mContainer;


    /* BASIC */
    @BindView(R.id.activity_contact_edit_first_name)
    CustomEditText mFirstName;

    @BindView(R.id.activity_contact_edit_last_name)
    CustomEditText mLastName;

    @BindView(R.id.activity_contact_edit_dob)
    CustomEditText mDateOfBirth;
    private CalendarDatePickerDialogFragment mDatePicker;


    @BindView(R.id.activity_contact_edit_image_container)
    FrameLayout mImageAddContainer;

    @BindView(R.id.activity_contact_edit_image_add)
    FrameLayout mImageAdd;

    @BindView(R.id.activity_contact_edit_image_existing_container)
    FrameLayout mImageExistingContainer;
    @BindView(R.id.activity_contact_edit_image_existing_container_delete)
    RelativeLayout mImageExistingDelete;
    @BindView(R.id.activity_contact_edit_image_existing)
    CircleImageView mImageExisting;




    /* PHONES */
    @BindView(R.id.activity_contact_edit_phones_add)
    Button mPhoneAdd;

    @BindView(R.id.activity_contact_edit_phones_recycler)
    RecyclerView mPhoneRecycler;

    private LinearLayoutManager mPhoneLayoutManager;
    private EditPhoneAdapter mPhoneAdapter;
    private ItemTouchHelper mPhoneItemTouchHelper;


    /* EMAILS */
    @BindView(R.id.activity_contact_edit_emails_add)
    Button mEmailAdd;

    @BindView(R.id.activity_contact_edit_emails_recycler)
    RecyclerView mEmailRecycler;

    private LinearLayoutManager mEmailLayoutManager;
    private EditEmailAdapter mEmailAdapter;
    private ItemTouchHelper mEmailItemTouchHelper;


    /* ADDRESSES */
    @BindView(R.id.activity_contact_edit_addresses_add)
    Button mAddressAdd;

    @BindView(R.id.activity_contact_edit_addresses_recycler)
    RecyclerView mAddressRecycler;

    private LinearLayoutManager mAdressLayoutManager;
    private EditAddressAdapter mAddressAdapter;
    private ItemTouchHelper mAddressItemTouchHelper;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);
        ButterKnife.bind(this);
        getPresenterComponent().inject(this);
        mPresenter.attachView(this);

        initViews();

        if(savedInstanceState != null) {
            mPresenter.restoreState((Contact)savedInstanceState.getSerializable(STATE_CONTACT), savedInstanceState.getBoolean(STATE_EDITING_CONTACT));
        } else {
            //Handle incoming Intent if editing an existing contact
            long contactId = getIntent().getLongExtra(Constants.EXTRA_CONTACT_EDIT_ACTIVITY_CONTACT_ID, -1);
            if(contactId != -1)
                mPresenter.setExisting(contactId);
            else
                mPresenter.creatingNew();
        }

        //Editing existing contact, change title to editing
        if(mPresenter.isEditing())
            getSupportActionBar().setTitle(R.string.activity_contact_edit_toolbar_title_edit);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Contact contact = mPresenter.getLoadedContact();
        contact.setFirstName(mFirstName.getText().trim());
        contact.setLastName(mLastName.getText().trim());
        outState.putSerializable(STATE_CONTACT, contact);
        outState.putBoolean(STATE_EDITING_CONTACT, mPresenter.isEditing());
    }

    @Override
    public void onBackPressed() {
        handleContactDiscard();
    }

    private void initViews() {

        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.activity_contact_edit_toolbar_title_new);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        mToolbar.setNavigationOnClickListener((v) -> {
            handleContactDiscard();
        });


        //Setup basic editTexts
        mFirstName.setMaxLength(Constants.ROOM_DATABASE_CONTACT_FIRST_NAME_MAX_LENGTH);
        mLastName.setMaxLength(Constants.ROOM_DATABASE_CONTACT_LAST_NAME_MAX_LENGTH);
        mDateOfBirth.setInnerEditTextNotClickable();
        mDateOfBirth.setOnClickListener((v) -> {
            Calendar mPreselectedDate =
                    (mPresenter.getLoadedContact().getDateOfBirth() == null ?
                            Calendar.getInstance() :
                            mPresenter.getLoadedContact().getDateOfBirth());
            mDatePicker = new CalendarDatePickerDialogFragment()
                    .setOnDateSetListener((dialog, year, monthOfYear,  dayOfMonth) -> {
                        mPresenter.setContactDateOfBirth(year, monthOfYear, dayOfMonth);
                        mDateOfBirth.setText(CalendarUtil.getCuteStringDateFromCalendar(mPresenter.getLoadedContact().getDateOfBirth()));

                    })
                    .setPreselectedDate(mPreselectedDate.get(Calendar.YEAR), mPreselectedDate.get(Calendar.MONTH), mPreselectedDate.get(Calendar.DAY_OF_MONTH))
                    .setDateRange(null, new MonthAdapter.CalendarDay(Calendar.getInstance()))
                    .setDoneText(getResources().getString(R.string.dialog_ok))
                    .setCancelText(getResources().getString(R.string.dialog_cancel));
            mDatePicker.show(getSupportFragmentManager(), "mDate");
        });

        mImageAddContainer.setOnClickListener((v) -> {
            Intent addImageIntent = new Intent(this, ImageEditActivity.class);

            if(mPresenter.getLoadedContact().getImage() != null) {
                String filename = UUID.randomUUID().toString() + Constants.IMAGE_FILE_EXTENSION;

                try {
                    Bitmap image = ImageUtil.getBitmap(mPresenter.getLoadedContact().getImage());
                    File cacheDir = FileUtil.getCacheDir();
                    FileUtil.createDirIfNotExists(cacheDir);
                    FileUtil.createNewFileIfNotExistsInDir(cacheDir, filename);
                    FileUtil.saveBitmapAsJpeg(new File(cacheDir.getPath(), filename), image, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);

                } catch (Exception e) {
                    Timber.e(e, "Error creating image");
                    showMessage(Message.ERROR_CREATING_IMAGE, null);
                    return;
                }
                addImageIntent.putExtra(Constants.EXTRA_IMAGE_EDIT_ACTIVITY_IMAGE_FILENAME, filename);
            }


            startActivityForResult(addImageIntent, Constants.REQUEST_ADD_CONTACT_IMAGE);
        });

        mImageExistingDelete.setOnClickListener((view) -> {
            mPresenter.setContactImage(null);
            mImageExistingContainer.setVisibility(View.GONE);
            mImageAdd.setVisibility(View.VISIBLE);
        });


        //Phones RecyclerView
        mPhoneLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPhoneAdapter = new EditPhoneAdapter(this, this);

        mPhoneRecycler.setLayoutManager(mPhoneLayoutManager);
        mPhoneRecycler.setAdapter(mPhoneAdapter);
        mPhoneRecycler.setNestedScrollingEnabled(false);

        mPhoneItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mPhoneAdapter));
        mPhoneItemTouchHelper.attachToRecyclerView(mPhoneRecycler);

        mPhoneAdd.setOnClickListener((v) -> {
            AddOrEditPhoneDialogFragment dialog = AddOrEditPhoneDialogFragment.newInstance(null, null);
            dialog.setPhoneAddedListener(( phone) -> {
                mPhoneAdapter.addItem(phone);
            });
            dialog.show(getSupportFragmentManager(), "AddOrEditPhoneDialogFragment");
        });


        //Emails RecyclerView
        mEmailLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEmailAdapter = new EditEmailAdapter(this, this);

        mEmailRecycler.setLayoutManager(mEmailLayoutManager);
        mEmailRecycler.setAdapter(mEmailAdapter);
        mEmailRecycler.setNestedScrollingEnabled(false);

        mEmailItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mEmailAdapter));
        mEmailItemTouchHelper.attachToRecyclerView(mEmailRecycler);

        mEmailAdd.setOnClickListener((v) -> {
            AddOrEditEmailDialogFragment dialog = AddOrEditEmailDialogFragment.newInstance(null, null);
            dialog.setEmailAddedListener((email) -> {
                mEmailAdapter.addItem(email);
            });
            dialog.show(getSupportFragmentManager(), "AddOrEditEmailDialogFragment");
        });


        //Addresses RecyclerView
        mAdressLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAddressAdapter = new EditAddressAdapter(this, this);

        mAddressRecycler.setLayoutManager(mAdressLayoutManager);
        mAddressRecycler.setAdapter(mAddressAdapter);
        mAddressRecycler.setNestedScrollingEnabled(false);

        mAddressItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mAddressAdapter));
        mAddressItemTouchHelper.attachToRecyclerView(mAddressRecycler);

        mAddressAdd.setOnClickListener((v) -> {
            AddOrEditAddressDialogFragment dialog = AddOrEditAddressDialogFragment.newInstance(null, null);
            dialog.setAddressAddedListener((address) -> {
                mAddressAdapter.addItem(address);
            });
            dialog.show(getSupportFragmentManager(), "AddOrEditAddressDialogFragment");
        });

    }

    private void handleContactDiscard() {
        AlertDialog dialog = new AlertDialog.Builder(ContactEditActivity.this)
                .setTitle(getResources().getString(R.string.dialog_contact_edit_discard_title))
                .setMessage(getResources().getString(R.string.dialog_contact_edit_discard_message))
                .setPositiveButton(getResources().getString(R.string.dialog_discard), (d, w) -> {
                    finish();
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), (d, w) -> {
                    d.dismiss();
                })
                .create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_contact_edit_save:
                mPresenter.saveContact(mFirstName.getText(), mLastName.getText());
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_ADD_CONTACT_IMAGE) {
            if(resultCode == RESULT_OK) {
                try {
                    String fileName = data.getStringExtra(Constants.EXTRA_IMAGE_EDIT_ACTIVITY_IMAGE_FILENAME);

                    if (fileName != null && !fileName.isEmpty()) {
                        Bitmap bitmap = ImageUtil.getBitmap(FileUtil.getCacheDir(), fileName);
                        FileUtil.deleteFile(FileUtil.getCacheDir(), fileName);

                        mPresenter.setContactImage(ImageUtil.toCompressedByteArray(bitmap, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE));
                        mImageExisting.setImageBitmap(bitmap);
                        mImageExistingContainer.setVisibility(View.VISIBLE);
                        mImageAdd.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Timber.e(e, "Error setting image");
                    showMessage(Message.ERROR_SAVING_IMAGE, null);
                }
            }
        }
    }




    /* ContactEditMvpView implementation */

    @Override
    public void showContact(Contact contact) {

        if(contact.getFirstName() != null)
            mFirstName.setText(contact.getFirstName());
        if(contact.getLastName() != null)
            mLastName.setText(contact.getLastName());
        if(contact.getDateOfBirth() != null)
            mDateOfBirth.setText(CalendarUtil.getCuteStringDateFromCalendar(contact.getDateOfBirth()));

        if(contact.getImage() != null) {
            mImageExisting.setImageBitmap(ImageUtil.getBitmap(contact.getImage()));
            mImageExistingContainer.setVisibility(View.VISIBLE);
            mImageAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public EditPhoneAdapter getPhoneAdapter() {
        return mPhoneAdapter;
    }

    @Override
    public EditEmailAdapter getEmailAdapter() {
        return mEmailAdapter;
    }

    @Override
    public EditAddressAdapter getAddressAdapter() {
        return mAddressAdapter;
    }

    @Override
    public void contactSavedSoFinish() {
        finish();
    }

    @Override
    public void showMessage(Message message, @Nullable BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        SnackbarUtil.showSnackbar(mContainer, message.getMessageType(), message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, callback);
    }



    /* OnDragStartListener interface implementation */
    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof EditPhoneViewHolder)
            mPhoneItemTouchHelper.startDrag(viewHolder);
        else if(viewHolder instanceof EditEmailViewHolder)
            mEmailItemTouchHelper.startDrag(viewHolder);
        else if(viewHolder instanceof EditAddressViewHolder)
            mAddressItemTouchHelper.startDrag(viewHolder);
    }
}
