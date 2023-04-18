package myoidc.server.infrastructure.jdbc;

import myoidc.server.domain.oauth.OauthClientDetails;
import myoidc.server.infrastructure.AbstractRepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2020/3/19
 *
 * @author Shengzhao Li
 */
class OauthRepositoryJdbcTest extends AbstractRepositoryTest {


    @Autowired
    private OauthRepositoryJdbc repositoryJdbc;


    @Test
    void findOauthClientDetails() {

        OauthClientDetails clientDetails = repositoryJdbc.findOauthClientDetails("client");
        assertNull(clientDetails);
    }


    @Test
    void findAllOauthClientDetails() {

        List<OauthClientDetails> list = repositoryJdbc.findAllOauthClientDetails("client_id");
        assertTrue(list.isEmpty());
    }


}