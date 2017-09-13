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

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.dialogs.AddOrEditPhoneDialogFragment;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.itemTouchHelper.ItemTouchHelperViewHolder;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EditPhoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

    //DATA
    private Activity mActivity;
    private EditPhoneAdapter mAdapter;
    private Phone mCurrent;
    private int mPosition;

    //UI
    @BindView(R.id.list_item_edit_two_rows_container)
    RelativeLayout mContainer;
    @BindView(R.id.list_item_edit_two_rows_1)
    TextView mRow1;
    @BindView(R.id.list_item_edit_two_rows_2)
    TextView mRow2;
    @BindView(R.id.list_item_edit_two_rows_delete)
    RelativeLayout mDelete;
    @BindView(R.id.list_item_edit_two_rows_drag_handle)
    ImageView mDrag;


    public EditPhoneViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EditPhoneAdapter adapter, Activity activity, Phone current, int position) {
        mAdapter = adapter;
        mActivity = activity;
        mCurrent = current;
        mPosition = position;

        mRow1.setText(mCurrent.getPhone());
        mRow2.setText(mCurrent.getAttachmentType().getFriendlyName());
    }

    public void setListeners(final EditPhoneAdapter.OnDragStartListener dragStartListener) {
        mContainer.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mDrag.setOnTouchListener( (view, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                dragStartListener.onDragStarted(EditPhoneViewHolder.this);
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.list_item_edit_two_rows_container:
                AddOrEditPhoneDialogFragment dialog = AddOrEditPhoneDialogFragment.newInstance(mCurrent, mPosition);
                dialog.setPhoneEditedListener(( phone, position) -> {
                    mAdapter.replaceItem(phone, position);
                });
                dialog.show(((AppCompatActivity)mActivity).getSupportFragmentManager(), "AddOrEditPhoneDialogFragment");
                break;
            case R.id.list_item_edit_two_rows_delete:
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