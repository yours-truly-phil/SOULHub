package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppRole;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SOULHubUserDetailsServiceTest {

    @Mock
    AppUserRepository userRepository;

    @Mock
    AppRoleRepository roleRepository;

    SOULHubUserDetailsService userDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new SOULHubUserDetailsService(roleRepository, userRepository);
    }

    @Test
    void register_new_user_saves_with_user_role() throws RoleNotFoundException {
        String userRoleName = "USER";
        AppRole userRole = new AppRole();
        userRole.setRoleName(userRoleName);

        String userName = "new user";
        String userClearPassword = "password";

        AppUser newUser = new AppUser();
        newUser.setUserName(userName);
        newUser.setEncryptedPassword(userClearPassword);

        Mockito.doReturn(Optional.of(userRole)).when(roleRepository).findByRoleName(userRoleName);
        Mockito.when(userRepository.save(Mockito.any(AppUser.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        AppUser userAccount = userDetailsService.registeNewUserAccount(newUser);
        System.out.println(userAccount);
        // user name
        assertEquals(userName, userAccount.getUserName());
        // password encoded
        assertTrue(SecurityUtils.passwordEncoder().matches(userClearPassword, userAccount.getEncryptedPassword()), "Password encoded");
        // role names
        assertTrue(userAccount.getRoles().stream().map(AppRole::getRoleName).allMatch(s -> s.equals(userRoleName)));
        // user status
        assertEquals(AppUser.UserStatus.ACTIVE, userAccount.getStatus());
    }

    @Test
    void register_new_user_when_roles_unavailable_throws_exception() {
        AppUser user = new AppUser();
        user.setUserName("username");
        user.setEncryptedPassword("bla");

        Mockito.doReturn(Optional.empty()).when(roleRepository).findByRoleName(Mockito.anyString());

        assertThrows(RoleNotFoundException.class, () -> userDetailsService.registeNewUserAccount(user));
    }


}
