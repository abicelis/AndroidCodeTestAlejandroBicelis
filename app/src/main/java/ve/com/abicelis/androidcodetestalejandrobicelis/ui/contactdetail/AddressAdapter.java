package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Address;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;

/**
 * Created by abicelis on 9/9/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> {

    //DATA
    private List<Address> mAddresses = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;
    private AddressCopiedToClipboardListener mListener;


    public AddressAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressViewHolder(mInflater.inflate(R.layout.list_item_three_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        holder.setData(this, mActivity, mAddresses.get(position), position);
        holder.setListeners();
    }

    public List<Address> getItems() {
        return mAddresses;
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }

    public void setOnAddressCopiedToClipboardListener(AddressCopiedToClipboardListener listener) {
        mListener = listener;
    }

    public void notifyAddressCopiedToClipbiard(){
        if(mListener != null)
            mListener.onAddressCopiedToClipboard();
    }

    public interface AddressCopiedToClipboardListener {
        void onAddressCopiedToClipboard();
    }
}
