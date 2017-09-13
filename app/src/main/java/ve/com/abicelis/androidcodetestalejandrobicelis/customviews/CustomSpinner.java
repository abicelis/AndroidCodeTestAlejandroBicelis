package ve.com.abicelis.androidcodetestalejandrobicelis.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;

/**
 * Created by abicelis on 11/9/2017.
 */

public class CustomSpinner extends LinearLayout {

    AdapterView.OnItemSelectedListener mItemSelectedListener;

    @BindView(R.id.fancy_spinner_icon)
    ImageView mIcon;

    @BindView(R.id.fancy_spinner_spinner)
    Spinner mSpinner;

    public CustomSpinner(Context context) {
        super(context);
        init(context, null);
    }
    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public CustomSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_spinner, this);

        ButterKnife.bind(this);

        //Get/apply custom xml configs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.custom_spinner);
        int iconId = a.getResourceId(R.styleable.custom_spinner_icon, -1);

        setIcon(iconId);
        a.recycle();


        this.setBackgroundResource(R.drawable.round_edges_background);
        this.setGravity(Gravity.CENTER_VERTICAL);

        //Trigger spinner also when clicking outside the spinner but inside the fancySpinner linearLayout container!
        this.setOnClickListener((v) -> {
            mSpinner.performClick();
        });
    }


    public void setIcon(@DrawableRes int iconRes) {
        if (iconRes != -1) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageResource(iconRes);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }


    /* Bypass functions for internal spinner */
    public void setItems(List<String> items) {
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSpinner.setOnItemSelectedListener(listener);
    }
    public void setSelection(int position) {
        mSpinner.setSelection(position);
    }

}
