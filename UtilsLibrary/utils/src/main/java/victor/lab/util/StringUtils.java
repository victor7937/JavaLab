package victor.lab.util;

import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    public static boolean isPositiveNumber(String str){
        if (NumberUtils.isNumber(str)) {
            return NumberUtils.toDouble(str) > 0.0;
        } else {
            return false;
        }
    }
}
