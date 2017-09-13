package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.itemTouchHelper.ItemTouchHelperAdapter;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EditPhoneAdapter extends RecyclerView.Adapter<EditPhoneViewHolder> implements ItemTouchHelperAdapter {

    //DATA
    private List<Phone> mPhones = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;
    private OnDragStartListener mDragStartListener;



    public EditPhoneAdapter(Activity activity, OnDragStartListener dragStartListener) {
        mActivity = activity;
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public EditPhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditPhoneViewHolder(mInflater.inflate(R.layout.list_item_edit_two_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(EditPhoneViewHolder holder, int position) {
        holder.setData(this, mActivity, mPhones.get(position), position);
        holder.setListeners(mDragStartListener);
    }

    public void setItems( List<Phone> phones) {
        mPhones = phones;
        notifyDataSetChanged();
    }

    public void addItem(Phone phone) {
        mPhones.add(phone);
        notifyItemInserted(getItemCount());
    }

    public void replaceItem(Phone phone, int position) {
        mPhones.set(position, phone);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mPhones.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mPhones.size();
    }





    /* ItemTouchHelperAdapter interface implementation */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Phone prev = mPhones.remove(fromPosition);
        mPhones.add(toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);

        Timber.d("NEW ORDER!");
        Timber.d("NEW ORDER!");

        for (Phone p : mPhones)
            Timber.d("Phone %s", p.toString());
    }

    @Override
    public void onItemDismiss(int position) {
        //Unused
    }

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }
}
