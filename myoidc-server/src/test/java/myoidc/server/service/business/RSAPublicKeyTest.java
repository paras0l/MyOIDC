package myoidc.server.service.business;


import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;


import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 2021/2/23
 *
 * @author Shengzhao Li
 * @since 1.1.2
 */
class RSAPublicKeyTest {


    @Test
    void test() throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        assertNotNull(keyPair);

        PrivateKey aPrivate = keyPair.getPrivate();
        PublicKey aPublic = keyPair.getPublic();

        //base64
        String base64PrivateKey = new String(Base64.encodeBase64Chunked(aPrivate.getEncoded()));
//        System.out.println(base64PrivateKey);
        String base64PublicKey = new String(Base64.encodeBase64Chunked(aPublic.getEncoded()));
//        System.out.println(base64PublicKey);


        //read
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey));
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        assertNotNull(privateKey);
        assertEquals(privateKey, aPrivate);


        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey)));
        assertNotNull(publicKey);
        assertEquals(publicKey, aPublic);

    }

}
