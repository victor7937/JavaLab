package victor.lab.util;

import org.junit.jupiter.api.Test;

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
    void notNumbersShouldReturnFalse() {
        assertAll(
                () -> assertFalse(StringUtils.isPositiveNumber("dfdf")),
                () -> assertFalse(StringUtils.isPositiveNumber("-1jh11"))
        );
    }
}
