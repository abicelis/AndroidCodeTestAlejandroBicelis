package ve.com.abicelis.androidcodetestalejandrobicelis.util;

/**
 * Created by abicelis on 23/7/2017.
 */

public class StringUtil {

    public static String startWithUppercase(String string) {
        string = string.trim();

        if(string.length() == 0)
            return string;

        if(string.length() == 1)
            return string.toUpperCase();

        String[] words = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if(i == 0)
                sb.append(word.substring(0, 1).toUpperCase());
            else
                sb.append(word.substring(0, 1));

            if(word.length() > 1)
                sb.append(word.substring(1).toLowerCase());

            sb.append(" ");
        }

        if(sb.length() > 0)
            sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}
