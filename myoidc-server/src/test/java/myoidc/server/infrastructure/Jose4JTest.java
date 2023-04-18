package myoidc.server.infrastructure;


import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.commons.lang3.RandomStringUtils;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.*;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.keys.RsaKeyUtil;
import org.junit.jupiter.api.Test;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import static myoidc.server.Constants.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * 2016/12/25
 * <p/>
 * Testing
 * https://bitbucket.org/b_c/jose4j
 *
 * @author Shengzhao Li
 */
class Jose4JTest {


    @Test
    void testRsaJsonWebKey() throws Exception {

        RsaJsonWebKey jwk = RsaJwkGenerator.generateJwk(DEFAULT_KEY_SIZE);
        //sig or enc
        jwk.setUse(USE_SIG);
        jwk.setKeyId(DEFAULT_KEY_ID);
        jwk.setAlgorithm(OIDC_ALG);
//        jwk.setKeyOps();

        final String publicKeyString = jwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        final String privateKeyString = jwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);

        assertNotNull(publicKeyString);
        assertNotNull(privateKeyString);
//        System.out.println("PublicKey:\n" + publicKeyString);
//        System.out.println("PrivateKey:\n" + privateKeyString);


        try (InputStream is = getClass().getClassLoader().getResourceAsStream(KEYSTORE_NAME)) {
            String keyJson = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            JsonWebKeySet jsonWebKeySet = new JsonWebKeySet(keyJson);
            assertNotNull(jsonWebKeySet);
            JsonWebKey jsonWebKey = jsonWebKeySet.findJsonWebKey(DEFAULT_KEY_ID, RsaKeyUtil.RSA, USE_SIG, OIDC_ALG);
            assertNotNull(jsonWebKey);
//            System.out.println(jsonWebKey);
        }


    }


    /**
     * RSA 加密与解密, 256位
     *
     * @since 1.1.0
     */
    @Test
    void aesEncryptDecryptRSA() throws Exception {

        RsaJsonWebKey jwk = RsaJwkGenerator.generateJwk(2048);
        jwk.setKeyId(keyId());

        final String publicKeyString = jwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        final String privateKeyString = jwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);

        String data = "I am marico 3";
        //加密
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        PublicKey publicKey = RsaJsonWebKey.Factory.newPublicJwk(publicKeyString).getPublicKey();
        jwe.setKey(publicKey);
        jwe.setPayload(data);

        String idToken = jwe.getCompactSerialization();
        assertNotNull(idToken);
        System.out.println(data + " ->>: " + idToken);


        //解密
        JsonWebEncryption jwe2 = new JsonWebEncryption();
        PrivateKey privateKey = RsaJsonWebKey.Factory.newPublicJwk(privateKeyString).getPrivateKey();
        jwe2.setKey(privateKey);
//        jwe2.setKey(jwk.getRsaPrivateKey());
        jwe2.setCompactSerialization(idToken);

        final String payload = jwe2.getPayload();
        assertNotNull(payload);
        assertEquals(payload, data);

    }


    /*
    * AES 加密与解密, 128位
    * */
    @Test
    void aesEncryptDecrypt128() throws Exception {

        String keyText = "iue98623diDEs096";
        String data = "I am marico";
        Key key = new AesKey(keyText.getBytes());

        //加密
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);
        jwe.setPayload(data);

        String idToken = jwe.getCompactSerialization();
        assertNotNull(idToken);
        System.out.println(data + " idToken: " + idToken);

        //解密
        JsonWebEncryption jwe2 = new JsonWebEncryption();
        jwe2.setKey(key);
        jwe2.setCompactSerialization(idToken);

        final String payload = jwe2.getPayload();
        assertNotNull(payload);
        assertEquals(payload, data);

    }


    /*
    * AES 加密与解密, 256位
    * */
    @Test
    void aesEncryptDecrypt256() throws Exception {

        String keyText = "iue98623diDEs096_8u@idls(*JKse09";
        String data = "I am marico";
        Key key = new AesKey(keyText.getBytes());

        //加密
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A256KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        jwe.setKey(key);
        jwe.setPayload(data);

        String idToken = jwe.getCompactSerialization();
        assertNotNull(idToken);
        System.out.println(data + " idToken: " + idToken);

        //解密
        JsonWebEncryption jwe2 = new JsonWebEncryption();
        jwe2.setKey(key);
        jwe2.setCompactSerialization(idToken);

        final String payload = jwe2.getPayload();
        assertNotNull(payload);
        assertEquals(payload, data);

    }


    /**
     * JWT 生成 idToken, 并进行消费(consume)
     * 算法:RSA SHA256
     *
     * @throws Exception e
     */
    @Test
    void jwtIdTokenConsumer() throws Exception {

        String keyId = keyId();

        //生成idToken
        JwtClaims claims = getJwtClaims();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(keyId);
        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);


        RsaJsonWebKey jwk = RsaJwkGenerator.generateJwk(2048);
        jwk.setKeyId(keyId);
        //set private key
        jws.setKey(jwk.getPrivateKey());

//        jwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);

        final String idToken = jws.getCompactSerialization();
        assertNotNull(idToken);
        System.out.println("idToken: " + idToken);


        //解析idToken, 验签
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setMaxFutureValidityInMinutes(300) // but the  expiration time can't be too crazy
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
                .setExpectedAudience("Audience") // to whom the JWT is intended for
                //公钥
                .setVerificationKey(jwk.getKey()) // verify the signature with the public key
                .build(); // create the JwtConsumer instance

        final JwtClaims jwtClaims = jwtConsumer.processToClaims(idToken);
        assertNotNull(jwtClaims);
        System.out.println(jwtClaims);

    }

    private String keyId() {
        return RandomStringUtils.random(32, true, true);
    }

    private JwtClaims getJwtClaims() {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Issuer");  // who creates the token and signs it
        claims.setAudience("Audience"); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject("subject"); // the subject/principal is whom the token is about
        claims.setClaim("email", "mail@example.com"); // additional claims/attributes about the subject can be added
        return claims;
    }


    /**
     * JWT 生成 idToken+加密, 进行消费(consume)
     * 使用EC
     *
     * @throws Exception e
     */
    @Test
    void jwtECIdTokenConsumer() throws Exception {

//        String keyId = GuidGenerator.generate();
        EllipticCurveJsonWebKey sendJwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);
        sendJwk.setKeyId(keyId());

        final String publicKeyString = sendJwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        final String privateKeyString = sendJwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
        System.out.println("publicKeyString: " + publicKeyString);
        System.out.println("privateKeyString: " + privateKeyString);

        //生成 idToken
        final JwtClaims jwtClaims = getJwtClaims();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        //私钥
        jws.setKey(sendJwk.getPrivateKey());
        jws.setKeyIdHeaderValue(sendJwk.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

        String innerIdToken = jws.getCompactSerialization();
        assertNotNull(innerIdToken);
        System.out.println("innerIdToken: " + innerIdToken);


        //对 idToken 进行加密
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.ECDH_ES_A128KW);
        String encAlg = ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256;
        jwe.setEncryptionMethodHeaderParameter(encAlg);


        EllipticCurveJsonWebKey receiverJwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);
        receiverJwk.setKeyId(keyId());

        jwe.setKey(receiverJwk.getPublicKey());
        jwe.setKeyIdHeaderValue(receiverJwk.getKeyId());

        jwe.setContentTypeHeaderValue("JWT");
        jwe.setPayload(innerIdToken);

        String idToken = jwe.getCompactSerialization();
        assertNotNull(idToken);
        System.out.println("idToken: " + idToken);


        //解析idToken, 验签
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
                .setExpectedAudience("Audience") // to whom the JWT is intended for
                //解密的私钥
                .setDecryptionKey(receiverJwk.getPrivateKey()) // decrypt with the receiver's private key
                //验签的公钥
                .setVerificationKey(sendJwk.getPublicKey()) // verify the signature with the sender's public key
                .build(); // create the JwtConsumer instance

        final JwtClaims claims = jwtConsumer.processToClaims(idToken);
        assertNotNull(claims);
        System.out.println(claims);


    }


}
