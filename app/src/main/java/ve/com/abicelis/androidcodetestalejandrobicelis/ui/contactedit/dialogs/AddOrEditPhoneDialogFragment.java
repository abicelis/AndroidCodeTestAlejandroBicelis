package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
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
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.InputFilterUtil;


/**
 * Created by abice on 16/3/2017.
 */

public class AddOrEditPhoneDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    //DATA
    private PhoneAddedListener mPhoneAddedListener;
    private PhoneEditedListener mPhoneEditedListener;
    private Phone mPhone = new Phone();
    private int mPosition = -1;

    //UI
    @BindView(R.id.dialog_add_phone_container)
    LinearLayout mContainer;

    @BindView(R.id.dialog_add_phone_title)
    TextView mTitle;

    @BindView(R.id.dialog_add_phone_number)
    CustomEditText mPhoneNumber;

    @BindView(R.id.dialog_add_phone_spinner)
    CustomSpinner mSpinner;

    @BindView(R.id.dialog_add_phone_button_container)
    RelativeLayout mButtonContainer;

    @BindView(R.id.dialog_add_phone_cancel)
    Button mCancel;

    @BindView(R.id.dialog_add_phone_add)
    Button mAdd;


    public AddOrEditPhoneDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddOrEditPhoneDialogFragment newInstance(@Nullable Phone phone, @Nullable Integer position) {
        AddOrEditPhoneDialogFragment frag = new AddOrEditPhoneDialogFragment();
        if(phone != null && position != null)
            frag.setPhoneAndPosition(phone, position);
        return frag;
    }

    void setPhoneAndPosition(Phone phone, int position) {
        mPhone = phone;
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
        View dialogView =  inflater.inflate(R.layout.dialog_add_phone, container);
        ButterKnife.bind(this, dialogView);


        mPhoneNumber.setMaxLength(Constants.ROOM_DATABASE_CONTACT_PHONE_MAX_LENGTH);
        mPhoneNumber.setFilters(new InputFilter[] {InputFilterUtil.getPhoneNumberInputFilter()} );
        mSpinner.setItems(AttachmentType.getFriendlyNames());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPhone.setAttachmentType(AttachmentType.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        if(mPosition != -1) {    //Editing existing phone
            mPhoneNumber.setText(mPhone.getPhone());
            mSpinner.setSelection(mPhone.getAttachmentType().ordinal());
            mTitle.setText(R.string.dialog_add_phone_title_edit);
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
            case R.id.dialog_add_phone_cancel:
                dismiss();
                break;

            case R.id.dialog_add_phone_add:
                mPhone.setPhone(mPhoneNumber.getText().trim());
                Message message = checkPhone();

                if(message != null) {
                    Toast.makeText(getActivity(), message.getFriendlyName(getActivity()), Toast.LENGTH_SHORT).show();
                    break;
                }

                if(mPosition != -1) {    //Editing
                    if(mPhoneEditedListener != null)
                        mPhoneEditedListener.onPhoneEdited(mPhone, mPosition);
                } else {
                    if(mPhoneAddedListener != null)
                        mPhoneAddedListener.onPhoneAdded(mPhone);
                }
                dismiss();
                break;
        }
    }


    private Message checkPhone(){
        if(mPhone.getPhone().isEmpty())
            return Message.ERROR_CHECKING_PHONE_INVALID_NUMBER;

        return null;
    }



    public void setPhoneAddedListener(PhoneAddedListener listener) {
        mPhoneAddedListener = listener;
    }
    public void setPhoneEditedListener(PhoneEditedListener listener) {
        mPhoneEditedListener = listener;
    }


    public interface PhoneAddedListener {
        void onPhoneAdded(Phone phone);
    }
    public interface PhoneEditedListener {
        void onPhoneEdited(Phone phone, int position);
    }
}
