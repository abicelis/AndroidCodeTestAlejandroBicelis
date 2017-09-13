package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.ContactEditActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.CalendarUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.SnackbarUtil;

/**
 * Created by abicelis on 10/9/2017.
 */

public class ContactDetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, ContactDetailMvpView {

    //ANIMATION
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;


    //DATA
    @Inject
    ContactDetailPresenter mPresenter;


    //UI
    @BindView(R.id.activity_contact_detail_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.activity_contact_detail_appbar)
    AppBarLayout mAppBarLayout;

    /* Appbar FrameLayout header */
    @BindView(R.id.activity_contact_detail_header_image)
    ImageView mTitleImage;

    @BindView(R.id.activity_contact_detail_header_linearlayout)
    LinearLayout mTitleContainer;

    @BindView(R.id.activity_contact_detail_header_title)
    TextView mTitleTitle;

    @BindView(R.id.activity_contact_detail_header_detail)
    TextView mTitleDetail;

    /* Top Toolbar */
    @BindView(R.id.activity_contact_detail_toolbar_top)
    Toolbar mToolbar;

    @BindView(R.id.activity_contact_detail_toolbar_container)
    LinearLayout mToolbarContainer;

    @BindView(R.id.activity_contact_detail_toolbar_title)
    TextView mToolbarTitle;

    /* Floating "bottom" Toolbar */
    @BindView(R.id.activity_contact_detail_toolbar_bottom)
    Toolbar mToolbarBottom;

    /* Big round CircleImageView */
    @BindView(R.id.activity_contact_detail_circle_image)
    CircleImageView mImage;

    /* Share FAB */
    @BindView(R.id.activity_contact_detail_fab_share)
    FloatingActionButton mFabShare;

    /* Phone numbers recycler */
    @BindView(R.id.activity_contact_detail_recycler_phones)
    RecyclerView mPhonesRecycler;
    private LinearLayoutManager mPhonesLayoutManager;
    private PhoneAdapter mPhoneAdapter;

    /* Emails recycler */
    @BindView(R.id.activity_contact_detail_recycler_emails)
    RecyclerView mEmailsRecycler;
    private LinearLayoutManager mEmailsLayoutManager;
    private EmailAdapter mEmailAdapter;

    /* Address recycler */
    @BindView(R.id.activity_contact_detail_container_addresses)
    LinearLayout mAddressesContainer;
    @BindView(R.id.activity_contact_detail_recycler_addresses)
    RecyclerView mAddressesRecycler;
    private LinearLayoutManager mAdresssesLayoutManager;
    private AddressAdapter mAddressAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        getPresenterComponent().inject(this);
        mPresenter.attachView(this);

        initViews();

        long contactId = getIntent().getLongExtra(Constants.EXTRA_CONTACT_DETAIL_ACTIVITY_CONTACT_ID, -1);
        if(contactId != -1) {
            mPresenter.getContact(contactId);
        } else {
            BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    finish();
                }
            };
            showMessage(Message.ERROR_INVALID_CONTACT, callback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.reloadContact();
    }

    /* Fancy Animations */

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitleAndCircularImage(percentage);
        handleToolbarContainerVisibility(percentage);
    }
    private void handleToolbarContainerVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mToolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mToolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }
    private void handleAlphaOnTitleAndCircularImage(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mToolbarBottom, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mToolbarBottom, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


    public void initViews() {
        //Hide upper toolbar (toolbarContainer)
        startAlphaAnimation(mToolbar, 0, View.INVISIBLE);

        //Hook into appbar movement to hide/show views
        mAppBarLayout.addOnOffsetChangedListener(this);

        //Set up fab
        mFabShare.setOnClickListener((v) -> {
            handleShare();
        });

        //Set up toolbars
        mToolbar.inflateMenu(R.menu.menu_contact_detail);
        mToolbar.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());
        mToolbarBottom.inflateMenu(R.menu.menu_contact_detail);
        mToolbarBottom.findViewById(R.id.menu_contact_detail_share).setVisibility(View.GONE);   //Hide share from menu, using fab here
        mToolbarBottom.setOnMenuItemClickListener(new ToolbarMenuItemClickListener());

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        mToolbar.setNavigationOnClickListener(new NavigationBackListener());
        mToolbarBottom.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        mToolbarBottom.setNavigationOnClickListener(new NavigationBackListener());


        //Phones RecyclerView
        mPhonesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPhoneAdapter = new PhoneAdapter(this);

        mPhonesRecycler.setLayoutManager(mPhonesLayoutManager);
        mPhonesRecycler.setAdapter(mPhoneAdapter);
        mPhonesRecycler.setNestedScrollingEnabled(false);


        //Emails RecyclerView
        mEmailsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEmailAdapter = new EmailAdapter(this);

        mEmailsRecycler.setLayoutManager(mEmailsLayoutManager);
        mEmailsRecycler.setAdapter(mEmailAdapter);
        mEmailsRecycler.setNestedScrollingEnabled(false);


        //Addredded RecyclerView
        mAdresssesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAddressAdapter = new AddressAdapter(this);
        mAddressAdapter.setOnAddressCopiedToClipboardListener(() -> {
            showMessage(Message.NOTICE_ADDRESS_COPIED_CLIPBOARD, null);
        });

        mAddressesRecycler.setLayoutManager(mAdresssesLayoutManager);
        mAddressesRecycler.setAdapter(mAddressAdapter);
        mAddressesRecycler.setNestedScrollingEnabled(false);
    }

    private void handleShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mPresenter.getLoadedContact().toPrettyString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }



    /* ContactDetailMvpView implementation */

    @Override
    public void showContact(Contact contact) {
        mToolbarTitle.setText(contact.getFullName(mPresenter.getSortType()));
        if(contact.getImage() != null) {
            Bitmap image = ImageUtil.getBitmap(contact.getImage());
            Blurry.with(this).radius(1).from(image).into(mTitleImage);
            mImage.setImageBitmap(image);
        } else {
            Bitmap image = ImageUtil.getBitmap(ContextCompat.getDrawable(this, R.drawable.ic_person_large));
            Blurry.with(this).radius(1).from(image).into(mTitleImage);
            mImage.setImageBitmap(image);
        }


        mTitleTitle.setText(contact.getFullName(mPresenter.getSortType()));
        if(contact.getDateOfBirth() != null)
            mTitleDetail.setText(CalendarUtil.getCuteStringDateFromCalendar(contact.getDateOfBirth()));

        //Phone numbers recyclerView
        mPhoneAdapter.getItems().clear();
        mPhoneAdapter.getItems().addAll(contact.getPhoneNumbers());
        mPhoneAdapter.notifyDataSetChanged();

        //Emails recyclerView
        mEmailAdapter.getItems().clear();
        mEmailAdapter.getItems().addAll(contact.getEmails());
        mEmailAdapter.notifyDataSetChanged();

        //Addresses recyclerView
        mAddressAdapter.getItems().clear();
        mAddressAdapter.getItems().addAll(contact.getAddresses());
        mAddressAdapter.notifyDataSetChanged();

        if(mAddressAdapter.getItems().size() == 0)
            mAddressesContainer.setVisibility(View.GONE);
        else
            mAddressesContainer.setVisibility(View.VISIBLE);
    }




    @Override
    public void contactDeletedSoFinish() {
        finish();
    }

    @Override
    public void showMessage(Message message, @Nullable BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        SnackbarUtil.showSnackbar(mCoordinatorLayout, message.getMessageType(), message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, callback);
    }





    private class ToolbarMenuItemClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_contact_detail_share:
                    handleShare();
                    break;

                case R.id.menu_contact_detail_edit:
                    Intent editContactIntent = new Intent(ContactDetailActivity.this, ContactEditActivity.class);
                    editContactIntent.putExtra(Constants.EXTRA_CONTACT_EDIT_ACTIVITY_CONTACT_ID, mPresenter.getLoadedContact().getId());
                    startActivity(editContactIntent);
                    break;

                case R.id.menu_contact_detail_delete:
                    AlertDialog dialog = new AlertDialog.Builder(ContactDetailActivity.this)
                            .setTitle(getResources().getString(R.string.dialog_contact_detail_delete_title))
                            .setMessage(String.format(Locale.getDefault(),
                                    getResources().getString(R.string.dialog_contact_detail_delete_message),
                                    mPresenter.getLoadedContact().getFullName(mPresenter.getSortType())))
                            .setPositiveButton(getResources().getString(R.string.dialog_delete),  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mPresenter.deleteContact();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                    break;

            }
            return true;
        }
    }


    private class NavigationBackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

}
