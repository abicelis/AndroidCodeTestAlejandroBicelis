package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import android.app.Activity;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs.AddOrEditAddressDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.itemTouchHelper.ItemTouchHelperViewHolder;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EditAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

    //DATA
    private Activity mActivity;
    private EditAddressAdapter mAdapter;
    private Address mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_edit_three_rows_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_edit_three_rows_1)
    TextView mRow1;
    @BindView(R.id.list_item_edit_three_rows_2)
    TextView mRow2;
    @BindView(R.id.list_item_edit_three_rows_3)
    TextView mRow3;
    @BindView(R.id.list_item_edit_three_rows_delete)
    RelativeLayout mDelete;
    @BindView(R.id.list_item_edit_three_rows_drag_handle)
    ImageView mDrag;


    public EditAddressViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EditAddressAdapter adapter, Activity activity, Address current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mRow1.setText(mCurrent.getStreet());
        mRow2.setText(String.format(Locale.getDefault(), "%1$s, %2$s (%3$s)", mCurrent.getCity(), mCurrent.getCountry(), mCurrent.getPostalCode()));
        mRow3.setText(mCurrent.getAttachmentType().getFriendlyName());
    }

    public void setListeners(final EditAddressAdapter.OnDragStartListener dragStartListener) {
        mContainer.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mDrag.setOnTouchListener( (view, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                dragStartListener.onDragStarted(EditAddressViewHolder.this);
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_edit_three_rows_container:
                AddOrEditAddressDialogFragment dialog = AddOrEditAddressDialogFragment.newInstance(mCurrent, mPosition);
                dialog.setAddressEditedListener(( address, position) -> {
                    mAdapter.replaceItem(address, position);
                });
                dialog.show(((AppCompatActivity)mActivity).getSupportFragmentManager(), "AddOrEditAddressDialogFragment");
                break;

            case R.id.list_item_edit_three_rows_delete:
                mAdapter.removeItem(mPosition);
                break;
        }
    }



    /* ItemTouchHelperViewHolder interface implementation */
    @Override
    public void onItemSelected() {
        //Unused
    }

    @Override
    public void onItemClear() {
        //Unused
    }

    @Override
    public void onItemMoved(int newPosition) {
        mPosition = newPosition;
    }
}