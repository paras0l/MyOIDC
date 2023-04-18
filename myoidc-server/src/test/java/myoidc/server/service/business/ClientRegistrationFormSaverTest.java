package myoidc.server.service.business;

import myoidc.server.domain.oauth.OauthClientDetails;
import myoidc.server.infrastructure.oidc.OIDCUseScene;
import myoidc.server.service.dto.ClientRegistrationFormDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 2020/4/1
 *
 * @author Shengzhao Li
 * @since 1.1.0
 */
class ClientRegistrationFormSaverTest {


    @Test
    void createDomain() throws Exception {

        ClientRegistrationFormDto dto = new ClientRegistrationFormDto(OIDCUseScene.WEB);
        dto.setAppName("Test");
        dto.setWebServerRedirectUri("https://abc.com/back");
        ClientRegistrationFormSaver formSaver = new ClientRegistrationFormSaver(dto);

        OauthClientDetails secret = formSaver.createDomain();
        assertNotNull(secret);
//        System.out.println(secret);
    }

}