package myoidc.server.configuration;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2018/3/22
 *
 * @author Shengzhao Li
 */
class SecurityConfigurationTest {

    @Test
    void passwordEncoder() throws Exception {


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String rawPassword = "mobile";
        final String admin = passwordEncoder.encode(rawPassword);

        assertNotNull(admin);

//        System.out.println(rawPassword + " --> " + admin);

    }

}