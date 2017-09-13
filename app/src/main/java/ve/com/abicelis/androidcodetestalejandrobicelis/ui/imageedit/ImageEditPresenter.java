package ve.com.abicelis.androidcodetestalejandrobicelis.ui.imageedit;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BasePresenter;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.FileUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;

/**
 * Created by abicelis on 12/9/2017.
 */

public class ImageEditPresenter extends BasePresenter<ImageEditMvpView> {

    private String mImageFilename;
    private Bitmap mImage;
    private boolean mLoadedExistingImage = false;


    void restoreState(String imageFilename, boolean loadedExistingImage) {
        mImageFilename = imageFilename;
        mLoadedExistingImage = loadedExistingImage;
        mImage = ImageUtil.getBitmap(FileUtil.getCacheDir(), imageFilename);
    }

    void loadExistingImage(String imageFilename) {
        mImageFilename = imageFilename;
        mImage = ImageUtil.getBitmap(FileUtil.getCacheDir(), imageFilename);
        mLoadedExistingImage = true;
        getMvpView().setImage(mImage);
    }


    void startImageCapture() {
        File imageDir = FileUtil.getCacheDir();
        try {
            mImageFilename = UUID.randomUUID().toString() + Constants.IMAGE_FILE_EXTENSION;
            FileUtil.createDirIfNotExists(imageDir);
            FileUtil.createNewFileIfNotExistsInDir(imageDir, mImageFilename);
        } catch (IOException e) {
            Timber.e(e, "Error creating image");
            getMvpView().showMessage(Message.ERROR_CREATING_IMAGE, null);
            return;
        }

        getMvpView().showSelectImageSourceDialog();
    }

    String getImageFilename() {
        return mImageFilename;
    }

    boolean workingWithExistingImage() {
        return mLoadedExistingImage;
    }

    Bitmap getTempImage() {
        return mImage;
    }

    void setTempImage(Bitmap image) {
        mImage = image;
    }

    void saveImage() {
        try {
            if (mImage.getWidth() > Constants.IMAGE_JPEG_MAX_SIZE_PX || mImage.getHeight() > Constants.IMAGE_JPEG_MAX_SIZE_PX)
                mImage = ImageUtil.scaleBitmap(mImage, Constants.IMAGE_JPEG_MAX_SIZE_PX);

            File file = new File(FileUtil.getCacheDir(), mImageFilename);
            FileUtil.saveBitmapAsJpeg(file, mImage, Constants.IMAGE_JPEG_COMPRESSION_PERCENTAGE);
            getMvpView().imageSavedSoFinish();
        }catch (IOException e) {
            Timber.e(e, "Error saving image");
            getMvpView().showMessage(Message.ERROR_SAVING_IMAGE, null);
        }
    }
}
