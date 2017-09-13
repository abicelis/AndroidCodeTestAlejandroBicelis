package ve.com.abicelis.androidcodetestalejandrobicelis.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import ve.com.abicelis.androidcodetestalejandrobicelis.application.AndroidCodeTestApplication;

/**
 * Created by abicelis on 3/7/2017.
 */

public class FileUtil {


    public static File getCacheDir() {
        return new File(AndroidCodeTestApplication.getAppContext().getCacheDir().getPath() + "/image/");
    }


    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile
     *            - FileInputStream for the file to copy from.
     * @param toFile
     *            - FileInputStream for the file to copy to.
     */
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

    public static void createDirIfNotExists(File directory) throws IOException, SecurityException {
        if (directory.mkdirs()){
            File nomedia = new File(directory, ".nomedia");
            nomedia.createNewFile();
        }
    }

    public static boolean deleteFile(File directory, String filename) {
        File file = new File(directory, filename);
        return file.delete();
    }


    /**
     * Creates an empty file at the specified directory, with the given name if it doesn't already exist
     */
    public static File createNewFileIfNotExistsInDir(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        file.createNewFile();
        return file;
    }

    public static Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(AndroidCodeTestApplication.getAppContext(), "ve.com.abicelis.androidcodetestalejandrobicelis.fileprovider", file);
    }



    /**
     * Saves a JPEG at the given quality to disk at the specified path of the given File
     * @param file The file where the JPEG will be saved
     * @param bitmapToSave The Bitmap to save into the file
     * @param quality The percentage of JPEG compression
     */
    public static void saveBitmapAsJpeg(File file, Bitmap bitmapToSave, int quality) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(ImageUtil.toCompressedByteArray(bitmapToSave, quality));
        fos.close();
    }

}