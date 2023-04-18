package myoidc.server.infrastructure.jpa;

import myoidc.server.domain.user.Privilege;
import myoidc.server.domain.user.User;
import myoidc.server.domain.user.UserPrivilege;
import myoidc.server.domain.user.UserRepository;
import myoidc.server.infrastructure.AbstractRepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2018/3/22
 *
 * @author Shengzhao Li
 */
class UserRepositoryHibernateTest extends AbstractRepositoryTest {


    @Autowired
    private UserRepository userRepository;


    @Test
    void findUserPrivileges() throws Exception {


        User user = new User().username("test").password("owesdds");
        userRepository.saveOrUpdate(user);

        UserPrivilege userPrivilege = new UserPrivilege(user, Privilege.USER);
        userRepository.saveOrUpdate(userPrivilege);

//        flush();

        final List<Privilege> privileges = userRepository.findUserPrivileges(user.uuid());
        assertNotNull(privileges);
//        System.out.println(privileges);
    }


    @Test
    void findUsersByUsername() {

        List<User> list = userRepository.findUsersByUsername("us");
        assertTrue(list.isEmpty());

    }


    @Test
    void findUserByUsernameNoArchived() {

        User user = userRepository.findUserByUsernameNoArchived("user");
        assertNull(user);

    }

}