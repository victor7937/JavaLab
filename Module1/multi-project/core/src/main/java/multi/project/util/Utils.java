package multi.project.util;

import victor.lab.exception.NotANumberException;
import victor.lab.util.StringUtils;

public class Utils {
    public static boolean isAllPositiveNumbers (String... strs) throws NotANumberException {
        for (String str : strs) {
            if (!StringUtils.isPositiveNumber(str)){
                return false;
            }
        }
        return true;
    }
}
