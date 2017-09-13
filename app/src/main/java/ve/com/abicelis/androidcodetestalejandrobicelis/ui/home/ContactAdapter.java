package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactSortType;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ContactViewModel;

/**
 * Created by abicelis on 9/9/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //DATA
    private List<ContactViewModel> mContacts = new ArrayList<>();
    private ContactSortType mContactSortType;
    private LayoutInflater mInflater;
    private Activity mActivity;
    private ContactOpenedListener mListener;


    public ContactAdapter(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getItemViewType(int position) {
        return mContacts.get(position).getType().ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContactViewModel.ContactViewModelType type = ContactViewModel.ContactViewModelType.values()[viewType];

        switch (type) {
            case HEADER:
                return new ContactHeaderViewHolder(mInflater.inflate(R.layout.list_item_contact_header, parent, false));
            case CONTACT:
                return new ContactViewHolder(mInflater.inflate(R.layout.list_item_contact, parent, false));
        }
        throw new InvalidParameterException("Wrong Contact Adapter viewType!");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContactViewModel current = mContacts.get(position);
        switch (current.getType()) {
            case HEADER:
                ContactHeaderViewHolder chvh = (ContactHeaderViewHolder)holder;
                chvh.setData(current.getHeader());
                break;
            case CONTACT:
                ContactViewHolder tvh = (ContactViewHolder)holder;
                tvh.setData(this, mActivity, current.getContact(), mContactSortType, position);
                tvh.setListeners();
                break;
        }

    }

    public List<ContactViewModel> getItems() {
        return mContacts;
    }

    public void setSortType(ContactSortType sortType) {
        mContactSortType = sortType;
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    void triggerContactOpened() {
        if(mListener != null)
            mListener.onContactOpened();
    }

    public void setContactOpenedListener(ContactOpenedListener listener) {
        mListener = listener;
    }

    interface ContactOpenedListener {
        void onContactOpened();
    }
}
