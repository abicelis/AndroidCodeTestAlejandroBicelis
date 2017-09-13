package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.itemTouchHelper.ItemTouchHelperAdapter;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EditAddressAdapter extends RecyclerView.Adapter<EditAddressViewHolder> implements ItemTouchHelperAdapter {

    //DATA
    private List<Address> mAddresses = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;
    private OnDragStartListener mDragStartListener;



    public EditAddressAdapter(Activity activity, OnDragStartListener dragStartListener) {
        mActivity = activity;
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public EditAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditAddressViewHolder(mInflater.inflate(R.layout.list_item_edit_three_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(EditAddressViewHolder holder, int position) {
        holder.setData(this, mActivity, mAddresses.get(position), position);
        holder.setListeners(mDragStartListener);
    }

    public void setItems(List<Address> addresses) {
        mAddresses = addresses;
        notifyDataSetChanged();
    }


    public void addItem(Address address) {
        mAddresses.add(address);
        notifyItemInserted(getItemCount());
    }

    public void replaceItem(Address address, int position) {
        mAddresses.set(position, address);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mAddresses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }






    /* ItemTouchHelperAdapter interface implementation */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Address prev = mAddresses.remove(fromPosition);
        mAddresses.add(toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //Unused
    }

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }
}
