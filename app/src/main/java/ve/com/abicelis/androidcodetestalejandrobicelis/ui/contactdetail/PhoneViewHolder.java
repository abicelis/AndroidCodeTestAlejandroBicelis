package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Contact;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.home.ContactAdapter;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;

/**
 * Created by abicelis on 9/9/2017.
 */

public class PhoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private Activity mActivity;
    private PhoneAdapter mAdapter;
    private Phone mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_two_rows_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_two_rows_1)
    TextView mRow1;
    @BindView(R.id.list_item_two_rows_2)
    TextView mRow2;


    public PhoneViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(PhoneAdapter adapter, Activity activity, Phone current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mRow1.setText(mCurrent.getPhone());
        mRow2.setText(mCurrent.getAttachmentType().getFriendlyName());
    }

    public void setListeners() {
        mContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_two_rows_container:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mCurrent.getPhone()));
                mActivity.startActivity(intent);
                break;
        }
    }
}