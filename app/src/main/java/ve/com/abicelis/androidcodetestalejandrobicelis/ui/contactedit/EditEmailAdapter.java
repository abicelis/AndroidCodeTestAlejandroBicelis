package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail.EmailViewHolder;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactedit.itemTouchHelper.ItemTouchHelperAdapter;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EditEmailAdapter extends RecyclerView.Adapter<EditEmailViewHolder> implements ItemTouchHelperAdapter {

    //DATA
    private List<Email> mEmails = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;
    private OnDragStartListener mDragStartListener;


    public EditEmailAdapter(Activity activity, OnDragStartListener dragStartListener) {
        mActivity = activity;
        mDragStartListener = dragStartListener;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public EditEmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditEmailViewHolder(mInflater.inflate(R.layout.list_item_edit_two_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(EditEmailViewHolder holder, int position) {
        holder.setData(this, mActivity, mEmails.get(position), position);
        holder.setListeners(mDragStartListener);
    }

    public void setItems(List<Email> emails) {
        mEmails = emails;
        notifyDataSetChanged();
    }

    public void addItem(Email email) {
        mEmails.add(email);
        notifyItemInserted(getItemCount());
    }

    public void replaceItem(Email email, int position) {
        mEmails.set(position, email);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        mEmails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mEmails.size();
    }





    /* ItemTouchHelperAdapter interface implementation */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Email prev = mEmails.remove(fromPosition);
        mEmails.add(toPosition, prev);
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
