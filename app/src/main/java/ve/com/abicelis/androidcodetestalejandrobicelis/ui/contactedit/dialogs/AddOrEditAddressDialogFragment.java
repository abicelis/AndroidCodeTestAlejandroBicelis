package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.AttachmentType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseDialogFragment;


/**
 * Created by abice on 16/3/2017.
 */

public class AddOrEditAddressDialogFragment extends BaseDialogFragment implements View.OnClickListener {
    
    //DATA
    private AddressAddedListener mAddressAddedListener;
    private AddressEditedListener mAddressEditedListener;
    private Address mAddress = new Address();
    private int mPosition = -1;

    //UI
    @BindView(R.id.dialog_add_address_container)
    LinearLayout mContainer;


    @BindView(R.id.dialog_add_address_title)
    TextView mTitle;

    @BindView(R.id.dialog_add_address_street)
    CustomEditText mStreet;

    @BindView(R.id.dialog_add_address_city)
    CustomEditText mCity;

    @BindView(R.id.dialog_add_address_country)
    CustomEditText mCountry;

    @BindView(R.id.dialog_add_address_postal_code)
    CustomEditText mPostalCode;
    
    @BindView(R.id.dialog_add_address_spinner)
    CustomSpinner mSpinner;

    @BindView(R.id.dialog_add_address_button_container)
    RelativeLayout mButtonContainer;

    @BindView(R.id.dialog_add_address_cancel)
    Button mCancel;

    @BindView(R.id.dialog_add_address_add)
    Button mAdd;


    public AddOrEditAddressDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddOrEditAddressDialogFragment newInstance(@Nullable Address address, @Nullable Integer position) {
        AddOrEditAddressDialogFragment frag = new AddOrEditAddressDialogFragment();
        if(address != null && position != null)
            frag.setAddressAndPosition(address, position);
        return frag;
    }

    void setAddressAndPosition(Address address, int position) {
        mAddress = address;
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
        View dialogView =  inflater.inflate(R.layout.dialog_add_address, container);
        ButterKnife.bind(this, dialogView);

        mStreet.setMaxLength(Constants.ROOM_DATABASE_CONTACT_ADDRESS_STREET_MAX_LENGTH);
        mCity.setMaxLength(Constants.ROOM_DATABASE_CONTACT_ADDRESS_CITY_MAX_LENGTH);
        mCountry.setMaxLength(Constants.ROOM_DATABASE_CONTACT_ADDRESS_COUNTRY_MAX_LENGTH);
        mPostalCode.setMaxLength(Constants.ROOM_DATABASE_CONTACT_ADDRESS_POSTAL_CODE_MAX_LENGTH);
        mSpinner.setItems(AttachmentType.getFriendlyNames());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAddress.setAttachmentType(AttachmentType.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        if(mPosition != -1) {    //Editing existing address
            mStreet.setText(mAddress.getStreet());
            mCity.setText(mAddress.getCity());
            mCountry.setText(mAddress.getCountry());
            mPostalCode.setText(mAddress.getPostalCode());
            mSpinner.setSelection(mAddress.getAttachmentType().ordinal());
            mTitle.setText(R.string.dialog_add_address_title_edit);
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
            case R.id.dialog_add_address_cancel:
                dismiss();
                break;

            case R.id.dialog_add_address_add:
                mAddress.setStreet(mStreet.getText().trim());
                mAddress.setCity(mCity.getText().trim());
                mAddress.setCountry(mCountry.getText().trim());
                mAddress.setPostalCode(mPostalCode.getText().trim());
                Message message = checkAddress();

                if(message != null) {
                    Toast.makeText(getActivity(), message.getFriendlyName(getActivity()), Toast.LENGTH_SHORT).show();
                    break;
                }

                if(mPosition != -1) {    //Editing
                    if(mAddressEditedListener != null)
                        mAddressEditedListener.onAddressEdited(mAddress, mPosition);
                } else {
                    if(mAddressAddedListener != null)
                        mAddressAddedListener.onAddressAdded(mAddress);
                }
                dismiss();
                break;
        }
    }

    
    private Message checkAddress(){
        if(mAddress.getStreet().isEmpty())
            return Message.ERROR_CHECKING_ADDRESS_INVALID_STREET;
        if(mAddress.getCity().isEmpty())
            return Message.ERROR_CHECKING_ADDRESS_INVALID_CITY;
        if(mAddress.getCountry().isEmpty())
            return Message.ERROR_CHECKING_ADDRESS_INVALID_COUNTRY;
        if(mAddress.getPostalCode().isEmpty())
            return Message.ERROR_CHECKING_ADDRESS_INVALID_POSTAL_CODE;

        return null;
    }

    public void setAddressAddedListener(AddOrEditAddressDialogFragment.AddressAddedListener listener) {
        mAddressAddedListener = listener;
    }
    public void setAddressEditedListener(AddOrEditAddressDialogFragment.AddressEditedListener listener) {
        mAddressEditedListener = listener;
    }

    public interface AddressAddedListener {
        void onAddressAdded(Address Address);
    }
    public interface AddressEditedListener {
        void onAddressEdited(Address Address, int position);
    }
}
