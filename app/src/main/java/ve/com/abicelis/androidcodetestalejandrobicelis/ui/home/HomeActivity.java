package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.local.SharedPreferenceHelper;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.AppThemeType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactViewModel;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.ContactEditActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.dialog.AddRandomContactsDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.SnackbarUtil;

/**
 * Created by abicelis on 9/9/2017.
 */

public class HomeActivity extends BaseActivity implements HomeMvpView {

    @Inject
    HomePresenter mHomePresenter;

    @BindView(R.id.activity_home_container)
    CoordinatorLayout mContainer;

    @BindView(R.id.activity_home_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_home_search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.activity_home_no_items_container)
    RelativeLayout mNoItemsContainer;
    @BindView(R.id.activity_home_swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.activity_home_recyclerview)
    RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private ContactAdapter mContactAdapter;

    @BindView(R.id.activity_home_fab_add_contact)
    FloatingActionButton mAddContact;

    @BindView(R.id.activity_home_fab_fetch_contacts)
    FloatingActionButton mFetchContacts;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getPresenterComponent().inject(this);
        mHomePresenter.attachView(this);

        setUpToolbar();
        setUpRecyclerView();
        setupSearchView();

        mAddContact.setOnClickListener(v -> {
            Intent newContactIntent = new Intent(this, ContactEditActivity.class);
            startActivity(newContactIntent);
        });

        mFetchContacts.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            AddRandomContactsDialogFragment df = AddRandomContactsDialogFragment.newInstance();
            df.setListener(() -> {
                        mHomePresenter.refreshContactList(null);
                    }
            );
            df.show(fm, "AddRandomContactsDialogFragment");
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomePresenter.refreshContactList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);

        MenuItem item = menu.findItem(R.id.menu_home_search);
        mSearchView.setMenuItem(item);

        switch (mHomePresenter.getSortType()) {
            case BY_FIRST_NAME:
                menu.findItem(R.id.menu_home_sort_by_first_name).setChecked(true);
                break;
            case BY_LAST_NAME:
                menu.findItem(R.id.menu_home_sort_by_last_name).setChecked(true);
                break;
            default:
                throw new InvalidParameterException("Invalid Contact SotType");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home_theme:
                BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        recreate();
                    }
                };
                AppThemeType t = new SharedPreferenceHelper(this).toggleAppThemeType();
                String message = String.format(Locale.getDefault(), Message.NOTICE_APP_THEME_TOGGLED.getFriendlyName(this), t.getFriendlyName());
                SnackbarUtil.showSnackbar(mContainer, Message.NOTICE_APP_THEME_TOGGLED.getMessageType(), message, SnackbarUtil.SnackbarDuration.SHORT, callback);
                break;

            case R.id.menu_home_sort_by_first_name:
                item.setChecked(true);
                mHomePresenter.changeSortType(ContactSortType.BY_FIRST_NAME);
                return true;

            case R.id.menu_home_sort_by_last_name:
                item.setChecked(true);
                mHomePresenter.changeSortType(ContactSortType.BY_LAST_NAME);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.activity_home_title);
        getSupportActionBar().setLogo(R.drawable.ic_person);
    }

    private void setUpRecyclerView() {

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContactAdapter = new ContactAdapter(this);
        mContactAdapter.setContactOpenedListener(() -> {
            if(mSearchView.isSearchOpen())
                mSearchView.closeSearch();
        });

        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mContactAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.swipe_refresh_green, R.color.swipe_refresh_red, R.color.swipe_refresh_yellow);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                               @Override
                                               public void onRefresh() {
                                                   mHomePresenter.refreshContactList(null);
                                               }
                                           }
        );
    }

    private void setupSearchView() {
        mSearchView.setVoiceSearch(true);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHomePresenter.refreshContactList(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.trim();
                if(newText.length() > 0)
                    mHomePresenter.refreshContactList(newText);
                if(newText.length() == 0)
                    mHomePresenter.refreshContactList(null);
                return true;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mFetchContacts.hide();
                mAddContact.hide();
            }

            @Override
            public void onSearchViewClosed() {
                mAddContact.show();
                mFetchContacts.show();
                mHomePresenter.refreshContactList(null);
            }
        });
    }






    /* HomeMvpView implementation */

    @Override
    public void showMessage(Message message, @Nullable BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        SnackbarUtil.showSnackbar(mContainer, message.getMessageType(), message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, callback);
    }

    @Override
    public void showLoading(boolean loading) {
        mSwipeRefresh.setRefreshing(loading);
    }

    @Override
    public void showContacts(List<ContactViewModel> contacts, ContactSortType sortType) {
        mSwipeRefresh.setRefreshing(false);
        mContactAdapter.setSortType(sortType);
        mContactAdapter.getItems().clear();
        mContactAdapter.getItems().addAll(contacts);
        mContactAdapter.notifyDataSetChanged();

        if(mContactAdapter.getItems().size() == 0) {
            mNoItemsContainer.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        } else {
            mNoItemsContainer.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }
}
