package myoidc.server.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2020/3/22
 *
 * @author Shengzhao Li
 */
class PasswordHandlerTest {


    @Test
    void randomPassword() {

        String s = PasswordHandler.randomPassword();
        assertNotNull(s);
        assertEquals(s.length(), 12);
        System.out.println(s);

    }

}