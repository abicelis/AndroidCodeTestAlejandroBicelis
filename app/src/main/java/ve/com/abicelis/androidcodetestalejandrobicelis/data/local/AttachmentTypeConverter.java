package ve.com.abicelis.androidcodetestalejandrobicelis.data.local;

import android.arch.persistence.room.TypeConverter;

import ve.com.abicelis.androidcodetestalejandrobicelis.data.model.AttachmentType;

/**
 * Created by abicelis on 9/9/2017.
 */

public class AttachmentTypeConverter {

    @TypeConverter
    public static AttachmentType toAttachmentType(String string) {
        try {
            return AttachmentType.valueOf(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toStr(AttachmentType attachmentType) {
        if(attachmentType == null)
            return null;

        return attachmentType.name();
    }
}