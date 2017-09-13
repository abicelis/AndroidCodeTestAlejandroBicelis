package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail.ContactDetailActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.CalendarUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;

/**
 * Created by abicelis on 9/9/2017.
 */

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private ContactAdapter mAdapter;
    private Activity mActivity;
    private Contact mCurrent;
    private ContactSortType mContactSortType;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_contact_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_contact_image)
    CircleImageView mImage;
    @BindView(R.id.list_item_contact_image_no_image)
    ImageView mImageNoImage;
    @BindView(R.id.list_item_contact_name)
    TextView mName;
    @BindView(R.id.list_item_contact_info)
    TextView mInfo;
    @BindView(R.id.list_item_contact_address)
    FrameLayout mAddressIcon;

    public ContactViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(ContactAdapter adapter, Activity activity, Contact current, ContactSortType contactSortType,  int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mContactSortType = contactSortType;
        mPosition = position;

        if (mCurrent.getImage() != null && mCurrent.getImage().length != 0) {
            mImageNoImage.setVisibility(View.GONE);
            mImage.setVisibility(View.VISIBLE);
            mImage.setImageBitmap(ImageUtil.getBitmap(mCurrent.getImage()));
        } else {
            mImageNoImage.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.GONE);
            mImage.setImageResource(R.drawable.person);
        }
        mName.setText(mCurrent.getFullName(mContactSortType));

        if(mCurrent.getAddresses().size() > 0)
            mAddressIcon.setVisibility(View.VISIBLE);
        else
            mAddressIcon.setVisibility(View.GONE);

        if(mCurrent.getDateOfBirth() != null) {
            mInfo.setText(CalendarUtil.getCuteStringDateFromCalendar(mCurrent.getDateOfBirth()));
        }
        //TODO la idea de vicky de upcoming bday!
    }

    public void setListeners() {
        mContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_contact_container:
                mAdapter.triggerContactOpened();
                Intent contactDetailIntent = new Intent(mActivity, ContactDetailActivity.class);
                contactDetailIntent.putExtra(Constants.EXTRA_CONTACT_DETAIL_ACTIVITY_CONTACT_ID, mCurrent.getId());
                mActivity.startActivity(contactDetailIntent);
                break;



        }
    }
}