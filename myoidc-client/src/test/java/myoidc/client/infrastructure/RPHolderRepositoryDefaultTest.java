package myoidc.client.infrastructure;

import myoidc.client.domain.RPHolder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 2020/4/7
 *
 * @author Shengzhao Li
 */
class RPHolderRepositoryDefaultTest {


    private RPHolderRepositoryDefault repositoryDefault = new RPHolderRepositoryDefault();


    @Test
            @Disabled
//    @Ignore
    void test() {


        RPHolder rpHolder = repositoryDefault.findRPHolder();
        assertNotNull(rpHolder);
//        assertFalse(rpHolder.isConfigRP());


        rpHolder = new RPHolder();
        rpHolder.setClientId("clientId");
        rpHolder.setClientSecret("clientSecret");
        rpHolder.setDiscoveryEndpoint("https://...");

        boolean ok = repositoryDefault.saveRPHolder(rpHolder);
        assertTrue(ok);

    }
}