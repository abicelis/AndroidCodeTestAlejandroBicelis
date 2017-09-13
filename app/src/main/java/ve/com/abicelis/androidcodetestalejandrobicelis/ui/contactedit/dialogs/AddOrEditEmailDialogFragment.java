package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.customviews.CustomEditText;
import ve.com.abicelis.androidcodetestalejandrobicelis.customviews.CustomSpinner;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.AttachmentType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseDialogFragment;


/**
 * Created by abice on 16/3/2017.
 */

public class AddOrEditEmailDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    
    //DATA
    private EmailAddedListener mEmailAddedListener;
    private EmailEditedListener mEmailEditedListener;
    private Email mEmail = new Email();
    private int mPosition = -1;


    //UI
    @BindView(R.id.dialog_add_email_container)
    LinearLayout mContainer;

    @BindView(R.id.dialog_add_email_title)
    TextView mTitle;

    @BindView(R.id.dialog_add_email_email)
    CustomEditText mEmailEditText;
    
    @BindView(R.id.dialog_add_email_spinner)
    CustomSpinner mSpinner;

    @BindView(R.id.dialog_add_email_button_container)
    RelativeLayout mButtonContainer;

    @BindView(R.id.dialog_add_email_cancel)
    Button mCancel;

    @BindView(R.id.dialog_add_email_add)
    Button mAdd;


    public AddOrEditEmailDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddOrEditEmailDialogFragment newInstance(@Nullable Email email, @Nullable Integer position) {
        AddOrEditEmailDialogFragment frag = new AddOrEditEmailDialogFragment();
        if(email != null && position != null)
            frag.setEmailAndPosition(email, position);
        return frag;
    }
    void setEmailAndPosition(Email email, int position) {
        mEmail = email;
        mPosition = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView =  inflater.inflate(R.layout.dialog_add_email, container);
        ButterKnife.bind(this, dialogView);

        mEmailEditText.setMaxLength(Constants.ROOM_DATABASE_CONTACT_EMAIL_MAX_LENGTH);
        mSpinner.setItems(AttachmentType.getFriendlyNames());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mEmail.setAttachmentType(AttachmentType.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        if(mPosition != -1) {    //Editing existing email
            mEmailEditText.setText(mEmail.getEmail());
            mSpinner.setSelection(mEmail.getAttachmentType().ordinal());
            mTitle.setText(R.string.dialog_add_email_title_edit);
            mAdd.setText(R.string.dialog_edit);
        }


        mAdd.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        return dialogView;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dialog_add_email_cancel:
                dismiss();
                break;

            case R.id.dialog_add_email_add:
                mEmail.setEmail(mEmailEditText.getText().trim());
                Message message = checkEmail();

                if(message != null) {
                    Toast.makeText(getActivity(), message.getFriendlyName(getActivity()), Toast.LENGTH_SHORT).show();
                    break;
                }

                if(mPosition != -1) {    //Editing
                    if(mEmailEditedListener != null)
                        mEmailEditedListener.onEmailEdited(mEmail, mPosition);
                } else {
                    if(mEmailAddedListener != null)
                        mEmailAddedListener.onEmaileAdded(mEmail);
                }
                dismiss();
                break;
        }
    }

    
    private Message checkEmail(){
        if(mEmail.getEmail().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail.getEmail()).matches())
            return Message.ERROR_CHECKING_EMAIL_INVALID_EMAIL;

        return null;
    }



    public void setEmailAddedListener(EmailAddedListener listener) {
        mEmailAddedListener = listener;
    }
    public void setEmailEditedListener(EmailEditedListener listener) {
        mEmailEditedListener = listener;
    }


    public interface EmailAddedListener {
        void onEmaileAdded(Email email);
    }
    public interface EmailEditedListener {
        void onEmailEdited(Email email, int position);
    }
}
