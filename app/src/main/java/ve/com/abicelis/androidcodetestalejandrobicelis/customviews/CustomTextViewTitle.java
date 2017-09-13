package ve.com.abicelis.androidcodetestalejandrobicelis.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;

/**
 * Created by abicelis on 11/9/2017.
 */

public class CustomTextViewTitle extends LinearLayout {


    @BindView(R.id.custom_textview_title_icon)
    ImageView mIcon;

    @BindView(R.id.custom_textview_title_text)
    TextView mText;

    public CustomTextViewTitle(Context context) {
        super(context);
        init(context, null);
    }
    public CustomTextViewTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public CustomTextViewTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_textview_title, this);

        ButterKnife.bind(this);

        //Get/apply custom xml configs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.custom_text_view_title);
        int iconId = a.getResourceId(R.styleable.custom_text_view_title_icon, -1);
        int textId = a.getResourceId(R.styleable.custom_text_view_title_text, -1);


        setIcon(iconId);
        setText(textId);
        a.recycle();

        //Set attributes of this (LinearLayout)
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setIcon(@DrawableRes int iconRes) {
        if (iconRes != -1) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageResource(iconRes);
        } else {
            mIcon.setVisibility(View.GONE);
        }
    }
    public void setText(String text) {
        mText.setText(text);
    }
    public void setText(@StringRes int textId) {
        if (textId != -1)
            mText.setText(textId);
    }


}
