package victor.lab.util;

import org.junit.jupiter.api.Test;
import victor.lab.exception.NotANumberException;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void positiveNumbersShouldReturnTrue() {
        assertAll(
                () -> assertTrue(StringUtils.isPositiveNumber("11")),
                () -> assertTrue(StringUtils.isPositiveNumber("16.1183"))
        );
    }

    @Test
    void negativeNumbersShouldReturnFalse() {
        assertAll(
                () -> assertFalse(StringUtils.isPositiveNumber("-11")),
                () -> assertFalse(StringUtils.isPositiveNumber("-11.11"))
        );
    }

    @Test
    void notNumbersShouldThrowsException() {
        assertAll(
                () -> assertThrows(NotANumberException.class, () -> StringUtils.isPositiveNumber("dfdf")),
                () -> assertThrows(NotANumberException.class, () ->  StringUtils.isPositiveNumber("-1jh11"))
        );
    }
}
