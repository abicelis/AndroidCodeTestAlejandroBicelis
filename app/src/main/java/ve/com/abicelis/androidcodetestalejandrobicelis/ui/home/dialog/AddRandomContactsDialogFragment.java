package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.RandomContactAmount;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseDialogFragment;


/**
 * Created by abice on 16/3/2017.
 */

public class AddRandomContactsDialogFragment extends BaseDialogFragment implements View.OnClickListener, AddRandomContactsMvpView {

    //CONST
    enum DialogState {STATE_IDLE, STATE_FETCHING, STATE_SAVING};

    //DATA
    private UsersAddedListener mListener;
    @Inject
    AddRandomContactsPresenter mPresenter;

    //UI
    @BindView(R.id.dialog_add_random_contacts_container)
    LinearLayout mContainer;

    @BindView(R.id.dialog_add_random_contacts_layout_1)
    LinearLayout mLayout1;

    @BindView(R.id.dialog_add_random_contacts_spinner)
    Spinner mSpinner;

    @BindView(R.id.dialog_add_random_contacts_layout_2)
    LinearLayout mLayout2;

    @BindView(R.id.dialog_add_random_contacts_text)
    TextView mText;

    @BindView(R.id.dialog_add_random_contacts_button_container)
    RelativeLayout mButtonContainer;

    @BindView(R.id.dialog_add_random_contacts_cancel)
    Button mCancel;

    @BindView(R.id.dialog_add_random_contacts_fetch)
    Button mFetch;


    public AddRandomContactsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddRandomContactsDialogFragment newInstance() {
        AddRandomContactsDialogFragment frag = new AddRandomContactsDialogFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        getPresenterComponent().inject(this);
        mPresenter.attachView(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView =  inflater.inflate(R.layout.dialog_add_random_contacts, container);
        ButterKnife.bind(this, dialogView);

        mText.setText(getString(R.string.dialog_add_random_contacts_text_1));


        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, RandomContactAmount.getStringList());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setSelectedAmount(RandomContactAmount.values()[i].getAmount());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        mFetch.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        return dialogView;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dialog_add_random_contacts_cancel:
                dismiss();
                break;

            case R.id.dialog_add_random_contacts_fetch:
                mPresenter.insertRandomContacts();
                break;

        }
    }


    public void setListener(UsersAddedListener listener) {
        mListener = listener;
    }
    public interface UsersAddedListener {
        void onContactsAdded();
    }



    /* AddRandomContactsMvpView implementation */

    @Override
    public void updateDialogState(DialogState state) {
        switch (state) {
            case STATE_IDLE:
                TransitionManager.beginDelayedTransition(mContainer, new Fade());
                mButtonContainer.setVisibility(View.VISIBLE);
                mLayout1.setVisibility(View.VISIBLE);
                mLayout2.setVisibility(View.GONE);
                break;
            case STATE_FETCHING:
                TransitionManager.beginDelayedTransition(mContainer, new Fade());
                mText.setText(getString(R.string.dialog_add_random_contacts_text_3));
                mButtonContainer.setVisibility(View.GONE);
                mLayout1.setVisibility(View.GONE);
                mLayout2.setVisibility(View.VISIBLE);
                break;
            case STATE_SAVING:
                mText.setText(getString(R.string.dialog_add_random_contacts_text_4));
                break;
            default:
                throw new InvalidParameterException("Invalid DialogState");
        }
    }


    @Override
    public void contactsSavedSoDismiss() {
        if(mListener != null)
            mListener.onContactsAdded();
        dismiss();
    }

    @Override
    public void showMessage(Message message, @Nullable BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        Toast.makeText(getActivity(), message.getFriendlyName(getActivity()), Toast.LENGTH_SHORT).show();
    }

}
