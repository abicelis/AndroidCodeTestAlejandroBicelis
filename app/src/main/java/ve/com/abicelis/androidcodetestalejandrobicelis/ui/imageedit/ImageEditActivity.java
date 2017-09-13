package ve.com.abicelis.androidcodetestalejandrobicelis.ui.imageedit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import ve.com.abicelis.androidcodetestalejandrobicelis.R;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Constants;
import ve.com.abicelis.androidcodetestalejandrobicelis.application.Message;
import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.ImageSourceType;
import ve.com.abicelis.androidcodetestalejandrobicelis.ui.base.BaseActivity;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.FileUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.ImageUtil;
import ve.com.abicelis.androidcodetestalejandrobicelis.util.SnackbarUtil;


/**
 * Created by abice on 18/4/2017.
 */

public class ImageEditActivity extends BaseActivity implements View.OnClickListener, ImageEditMvpView {


    //CONSTS
    private static String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String STATE_WORKING_WITH_EXISTING_IMAGE = "STATE_WORKING_WITH_EXISTING_IMAGE";
    private static final String STATE_FILENAME = "STATE_FILENAME";

    //DATA
    private int mRotation = 0;


    @Inject
    ImageEditPresenter mPresenter;

    //UI
    @BindView(R.id.activity_edit_image_container)
    RelativeLayout mContainer;

    @BindView(R.id.activity_edit_image_image)
    CropImageView mImage;

    @BindView(R.id.activity_edit_image_crop)
    FloatingActionButton mCrop;

    @BindView(R.id.activity_edit_image_rotate)
    FloatingActionButton mRotate;

    @BindView(R.id.activity_edit_image_camera)
    FloatingActionButton mCamera;

    @BindView(R.id.activity_edit_image_done)
    Button mDone;

    @BindView(R.id.activity_edit_image_cancel)
    Button mCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        ButterKnife.bind(this);

        getPresenterComponent().inject(this);
        mPresenter.attachView(this);

        initViews();


//        if(savedInstanceState != null) {
//            mPresenter.restoreState(savedInstanceState.getString(STATE_FILENAME), savedInstanceState.getBoolean(STATE_WORKING_WITH_EXISTING_IMAGE, false));
//            return;
//        }

        String fileName = getIntent().getStringExtra(Constants.EXTRA_IMAGE_EDIT_ACTIVITY_IMAGE_FILENAME);

        if(fileName != null) {
            mPresenter.loadExistingImage(fileName);
        }


        if(!permissionsGranted())
            requestAllPermissions();
        else {
            if(!mPresenter.workingWithExistingImage())
                mPresenter.startImageCapture();
        }

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putBoolean(STATE_WORKING_WITH_EXISTING_IMAGE, mPresenter.workingWithExistingImage());
//        outState.putString(STATE_FILENAME, mPresenter.getImageFilename());
//    }

    private void initViews() {
        mCrop.setOnClickListener(this);
        mRotate.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        mDone.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }


    private boolean permissionsGranted() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_IMAGE_EDIT_ACTIVITY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Constants.PERMISSION_IMAGE_EDIT_ACTIVITY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (!mPresenter.workingWithExistingImage())
                    mPresenter.startImageCapture();
            } else {
                //Show error and exit
                BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                };
                showMessage(Message.ERROR_PERMISSIONS_NOT_GRANTED, callback);
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.activity_edit_image_crop:
                if(mPresenter.getTempImage() != null) {
                    Bitmap bitmap = mImage.getCroppedImage();
                    mImage.setImageBitmap(bitmap);
                    mPresenter.setTempImage(bitmap);
                    mRotation = 0;
                }
                break;

            case R.id.activity_edit_image_rotate:
                if(mPresenter.getTempImage() != null) {
                    mRotation -= 90;
                    mImage.rotateImage(-90);
                }
                break;

            case R.id.activity_edit_image_camera:
                mRotation = 0;
                showSelectImageSourceDialog();
                break;

            case R.id.activity_edit_image_done:
                if(mPresenter.getTempImage() != null) {
                    applyPendingRotation();
                    mPresenter.saveImage();
                }
                break;

            case R.id.activity_edit_image_cancel:
                File file = new File(FileUtil.getCacheDir(), mPresenter.getImageFilename());
                file.delete();
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }


    private void applyPendingRotation() {
        if(mRotation != 0) {
            //Normalize rotation
            mRotation = mRotation % 360;
            if (mRotation < 0) mRotation += 360;

            //Get the bitmap
            Bitmap imageBitmap = mPresenter.getTempImage();

            //Rotate it
            Matrix matrix = new Matrix();
            matrix.postRotate(mRotation);
            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
            mPresenter.setTempImage(imageBitmap);

            //Reset Rotation
            mRotation = 0;
        }
    }

    private void handleImageCapture(String imageFileName) {
        File imageFile = new File(FileUtil.getCacheDir(), imageFileName);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {

            Uri imageUri;
            try {
                imageUri = FileUtil.getUriForFile(imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                //HACK: Before starting the camera activity on pre-lollipop devices, make sure to grant permissions to all packages that need it
                List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    this.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
            }catch (Exception e) {
                Timber.e(e, "Error requesting camera");
                showMessage(Message.ERROR_REQUESTING_CAMERA, null);
            }
        } else {
            Timber.e("Error, no camera installed");
            showMessage(Message.ERROR_NO_CAMERA_INSTALLED, null);
        }
    }

    private void handlePickImageUsingGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Constants.REQUEST_PICK_IMAGE_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //checkExifAndFixImageRotation();
            Bitmap newBitmap = ImageUtil.getBitmap(FileUtil.getCacheDir(), mPresenter.getImageFilename());
            mImage.setImageBitmap(newBitmap);
            mPresenter.setTempImage(newBitmap);

        } else if (requestCode == Constants.REQUEST_PICK_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap newBitmap = ImageUtil.getBitmap(imageUri);
                mImage.setImageBitmap(newBitmap);
                mPresenter.setTempImage(newBitmap);
            } catch (IOException e) {
                Timber.e(e, "Error requesting image from gallery");
                showMessage(Message.ERROR_REQUESTING_IMAGE_GALLERY, null);
            }
        }

    }

    private void checkExifAndFixImageRotation() {
        try {

            String imageFilename = mPresenter.getImageFilename();
            File imageFilesDir = FileUtil.getCacheDir();
            Log.d("checkExif", imageFilename);
            Log.d("checkExif", imageFilesDir.toString());

            String imageFilePath = new File(imageFilesDir, imageFilename).getAbsolutePath();
            ExifInterface ei = new ExifInterface(imageFilePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    mRotation = 90;
                    applyPendingRotation();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    mRotation = 180;
                    applyPendingRotation();
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    mRotation = 270;
                    applyPendingRotation();
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    //Good!

                default:
                    break;
            }
            //ei.setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        }catch (IOException e) {
            /*Do nothing*/
        }
    }





    @Override
    public void showSelectImageSourceDialog() {
        FragmentManager fm = this.getSupportFragmentManager();

        SelectImageSourceDialogFragment dialog = SelectImageSourceDialogFragment.newInstance();

        dialog.setListener(new SelectImageSourceDialogFragment.ImageSourceSelectedListener() {
            @Override
            public void onSourceSelected(ImageSourceType imageSourceType) {
                switch (imageSourceType) {
                    case CAMERA:
                        handleImageCapture(mPresenter.getImageFilename());
                        break;
                    case GALLERY:
                        handlePickImageUsingGallery();
                        break;
                    case NONE:
                        break;
                }
            }

        });
        dialog.show(fm, "SelectImageSourceDialogFragment");
    }

    @Override
    public void setImage(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    @Override
    public void imageSavedSoFinish() {
        BaseTransientBottomBar.BaseCallback<Snackbar> callback = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                Intent returnData = new Intent();
                returnData.putExtra(Constants.EXTRA_IMAGE_EDIT_ACTIVITY_IMAGE_FILENAME, mPresenter.getImageFilename());
                setResult(RESULT_OK, returnData);
                finish();
            }
        };
        showMessage(Message.SUCCESS_SAVING_IMAGE, callback);
    }


    /* ImageEditMvpView interface implementation */

    @Override
    public void showMessage(Message message, @Nullable BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        SnackbarUtil.showSnackbar(mContainer, message.getMessageType(), message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, callback);

    }
}
