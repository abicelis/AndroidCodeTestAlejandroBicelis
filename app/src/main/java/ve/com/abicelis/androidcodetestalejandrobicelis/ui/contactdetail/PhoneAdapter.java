package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

/**
 * Created by abicelis on 9/9/2017.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneViewHolder> {

    //DATA
    private List<Phone> mPhones = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;


    public PhoneAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhoneViewHolder(mInflater.inflate(R.layout.list_item_two_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {
        holder.setData(this, mActivity, mPhones.get(position), position);
        holder.setListeners();
    }

    public List<Phone> getItems() {
        return mPhones;
    }

    @Override
    public int getItemCount() {
        return mPhones.size();
    }
}
