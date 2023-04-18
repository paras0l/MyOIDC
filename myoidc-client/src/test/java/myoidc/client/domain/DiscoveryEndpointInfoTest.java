package myoidc.client.domain;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2020/4/7
 *
 * @author Shengzhao Li
 * @since 1.1.0
 */
class DiscoveryEndpointInfoTest {


    @Test
    @Disabled
//    @Ignore
    void test() {

        RestTemplate restTemplate = new RestTemplate();
        DiscoveryEndpointInfo info = restTemplate.getForObject("http://localhost:8080/myoidc-server/.well-known/openid-configuration", DiscoveryEndpointInfo.class);

        assertNotNull(info);

    }

}