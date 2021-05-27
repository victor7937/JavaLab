package victor.lab.util;

import org.apache.commons.lang3.math.NumberUtils;
import victor.lab.exception.NotANumberException;

public class StringUtils {

    private static final String EXCEPTION_MESSAGE = "Argument should be a number";

    public static boolean isPositiveNumber(String str) throws NotANumberException {
        if (NumberUtils.isNumber(str)) {
            return NumberUtils.toDouble(str) > 0.0;
        } else {
            throw new NotANumberException(EXCEPTION_MESSAGE);
        }
    }
}
