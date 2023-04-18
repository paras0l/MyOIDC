package myoidc.server.infrastructure;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Shengzhao Li
 */
class RandomUtilsTest {

    @Test
    void testRandomText() throws Exception {

        final String random = RandomUtils.randomText();
        assertNotNull(random);
        assertEquals(random.length(), 32);
//        System.out.println(random);

    }

    @Test
    void testRandomNumber() throws Exception {

        final String number = RandomUtils.randomNumber();
        assertNotNull(number);
        assertEquals(number.length(), 32);
//        System.out.println(number);

    }
}