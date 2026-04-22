package com.nerosoft.linkyou.utility;

import org.junit.jupiter.api.Test;

public class RandomUtilityTests {
    @Test
    void randomString_returnsStringOfCorrectLength() {
        int length = 10;
        String randomStr = RandomUtility.randomString(length);
        assert randomStr.length() == length : "Generated string should have the correct length";
    }

    @Test
    void randomString_returnsDifferentStrings() {
        String str1 = RandomUtility.randomString(10);
        String str2 = RandomUtility.randomString(10);
        assert !str1.equals(str2) : "Generated strings should be different";
    }
}
