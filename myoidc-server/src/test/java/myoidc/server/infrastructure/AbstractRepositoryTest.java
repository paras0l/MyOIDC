package myoidc.server.infrastructure;

import myoidc.server.ContextTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2018/3/22
 *
 * @author Shengzhao Li
 */
public class AbstractRepositoryTest extends ContextTest {


    @Autowired
    private EntityManagerFactory entityManagerFactory;


    protected EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }


    protected void flush() {
        EntityManager entityManager = entityManager();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
     void test() {
        assertNotNull(entityManagerFactory);
    }

}
