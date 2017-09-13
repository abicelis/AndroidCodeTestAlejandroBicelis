package ve.com.abicelis.androidcodetestalejandrobicelis.ui.imageedit;

import android.graphics.Bitmap;

import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.MvpView;

/**
 * Created by abicelis on 12/9/2017.
 */

public interface ImageEditMvpView extends MvpView {
    void setImage(Bitmap bitmap);
    void imageSavedSoFinish();
    void showSelectImageSourceDialog();
}
