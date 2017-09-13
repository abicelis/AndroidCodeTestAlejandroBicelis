package ve.com.abicelis.androidcodetestalejandrobicelis.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;

/**
 * Created by abicelis on 9/9/2017.
 */

public class ContactHeaderViewHolder extends RecyclerView.ViewHolder {
    //UI
    @BindView(R.id.list_item_contact_header_title)
    TextView mHeaderTitle;


    public ContactHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(String headerTitle) {
        mHeaderTitle.setText(headerTitle);
    }
}