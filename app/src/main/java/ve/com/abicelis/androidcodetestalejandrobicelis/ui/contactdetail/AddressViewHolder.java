package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;

/**
 * Created by abicelis on 9/9/2017.
 */

public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private Activity mActivity;
    private AddressAdapter mAdapter;
    private Address mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_three_rows_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_three_rows_1)
    TextView mRow1;
    @BindView(R.id.list_item_three_rows_2)
    TextView mRow2;
    @BindView(R.id.list_item_three_rows_3)
    TextView mRow3;


    public AddressViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(AddressAdapter adapter, Activity activity, Address current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mRow1.setText(mCurrent.getStreet());
        mRow2.setText(String.format(Locale.getDefault(), "%1$s, %2$s (%3$s)", mCurrent.getCity(), mCurrent.getCountry(), mCurrent.getPostalCode()));
        mRow3.setText(mCurrent.getAttachmentType().getFriendlyName());
    }

    public void setListeners() {
        mContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_three_rows_container:
                //Copy address to clipboard
                ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", mCurrent.toPrettyString());
                clipboard.setPrimaryClip(clip);
                mAdapter.notifyAddressCopiedToClipbiard();
                break;
        }
    }
}