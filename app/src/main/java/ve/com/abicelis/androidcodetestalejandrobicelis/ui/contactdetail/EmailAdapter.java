package ve.com.abicelis.androidcodetestalejandrobicelis.ui.contactdetail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Email;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.Phone;

/**
 * Created by abicelis on 9/9/2017.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailViewHolder> {

    //DATA
    private List<Email> mEmails = new ArrayList<>();
    private LayoutInflater mInflater;
    private Activity mActivity;


    public EmailAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmailViewHolder(mInflater.inflate(R.layout.list_item_two_rows, parent, false));
    }

    @Override
    public void onBindViewHolder(EmailViewHolder holder, int position) {
        holder.setData(this, mActivity, mEmails.get(position), position);
        holder.setListeners();
    }

    public List<Email> getItems() {
        return mEmails;
    }

    @Override
    public int getItemCount() {
        return mEmails.size();
    }
}
