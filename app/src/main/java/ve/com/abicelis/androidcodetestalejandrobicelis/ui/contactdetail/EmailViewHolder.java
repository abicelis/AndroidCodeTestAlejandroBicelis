package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //DATA
    private Activity mActivity;
    private EmailAdapter mAdapter;
    private Email mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_two_rows_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_two_rows_1)
    TextView mRow1;
    @BindView(R.id.list_item_two_rows_2)
    TextView mRow2;


    public EmailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EmailAdapter adapter, Activity activity, Email current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mRow1.setText(mCurrent.getEmail());
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
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + mCurrent.getEmail()));
                mActivity.startActivity(intent);
                break;
        }
    }
}