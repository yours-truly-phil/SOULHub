package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppRole;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.data.repository.VerificationTokenRepository;
import io.horrorshow.soulhub.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;

import javax.management.relation.RoleNotFoundException;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SOULHubUserDetailsServiceTest {

    @Mock
    AppUserRepository userRepository;

    @Mock
    AppRoleRepository roleRepository;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    JavaMailSender mailSender;

    SOULHubUserDetailsService userDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        userDetailsService =
                new SOULHubUserDetailsService(
                        roleRepository,
                        userRepository,
                        verificationTokenRepository,
                        mailSender);
    }

    @Test
    void register_new_user_saves_with_user_role() throws RoleNotFoundException {

        String userName = "new user";
        String userClearPassword = "password";
        String email = "mail@mail.com";

        AppUser newUser = new AppUser();
        newUser.setUserName(userName);
        newUser.setEncryptedPassword(userClearPassword);
        newUser.setEmail(email);

        Mockito.doReturn(Optional.of(newRole()))
                .when(roleRepository)
                .findByRoleName("USER");

        Mockito.when(userRepository.save(Mockito.any(AppUser.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        AppUser userAccount = userDetailsService.registerAppUser(newUser);
        System.out.println(userAccount);
        // user name
        assertEquals(userName, userAccount.getUserName());
        // user email
        assertEquals(email, userAccount.getEmail());
        // password encoded
        assertTrue(SecurityUtils.passwordEncoder()
                        .matches(userClearPassword, userAccount.getEncryptedPassword()),
                "Password encoded");
        // role names
        assertTrue(userAccount
                .getRoles().stream().map(AppRole::getRoleName)
                .allMatch(s -> s.equals("USER")));
        // user status
        assertEquals(AppUser.UserStatus.UNCONFIRMED, userAccount.getStatus());
    }

    @Test
    void register_new_user_when_roles_unavailable_throws_exception() {
        AppUser user = new AppUser();
        user.setUserName("username");
        user.setEncryptedPassword("bla");
        user.setEmail("mail@mail.com");

        Mockito.doReturn(Optional.empty())
                .when(roleRepository)
                .findByRoleName(Mockito.anyString());

        assertThrows(RoleNotFoundException.class,
                () -> userDetailsService.registerAppUser(user));
    }

    @Test
    void exception_when_trying_to_register_with_invalid_email() {

        AppUser user = new AppUser();
        user.setUserName("user");
        user.setEncryptedPassword("pü0q,43+ti");

        String[] invalidEmails = {
                "",
                "invalid",
                "alice.example.com",
                "alice..bob@example.com",
                "alice@.example.me.org"};

        Arrays.asList(invalidEmails).forEach(email -> {
            user.setEmail(email);
            assertThrows(ValidationException.class,
                    () -> userDetailsService.registerAppUser(user));
        });
    }

    @Test
    void exception_when_trying_to_register_with_invalid_username() {
        AppUser user = new AppUser();
        user.setEncryptedPassword("9a4p8tzu");
        user.setEmail("valid@email.com");

        String[] invalidUsernames =
                {"", "a", "looooooooooooooooooooooooooooooooooooooooooooo" +
                        "oooooooooooooooooooooooooooooooooooooooonnnnnnnnnnn" +
                        "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                        "nggggggggggggggggggggggggggggggggggggggggggggggggg" +
                        "ggggggggggggggggguuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" +
                        "uuuuuuussssssssssssssssssssseeeeeeeeeeeeeeeeeeerrrr" +
                        "rrrrrrrrrrrrrrrnnnnnnnnnnnnnnnnaaaaaaaaaaaaaaaaaa" +
                        "mmmmmmmmmmmmmmmmmmeeeeeeeeeeeeeeeeeeeeeeeeeeee"};

        Arrays.asList(invalidUsernames).forEach(username -> {
            user.setUserName(username);
            assertThrows(ValidationException.class,
                    () -> userDetailsService.registerAppUser(user));
        });
    }

    private AppRole newRole() {
        AppRole userRole = new AppRole();
        userRole.setRoleName("USER");
        return userRole;
    }

}
