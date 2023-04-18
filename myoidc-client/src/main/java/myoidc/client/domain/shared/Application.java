package myoidc.client.domain.shared;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Shengzhao Li
 */
public class Application implements InitializingBean {

    //系统字符编码
    public static final String ENCODING = "UTF-8";

    public static final String PROJECT_HOME = "https://gitee.com/mkk/MyOIDC";

    /**
     * Current  version
     * same with pom.xml
     */
    public static final String VERSION = "1.2.0";


    //application host
    private static String host;


    /**
     * default
     */
    public Application() {
    }


    public static String host() {
        return host;
    }

    public void setHost(String host) {
        Application.host = host.endsWith("/") ? host : host + "/";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(host, "host is null");
    }
}