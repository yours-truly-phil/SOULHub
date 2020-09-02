package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppRole;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.VerificationToken;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.data.repository.VerificationTokenRepository;
import io.horrorshow.soulhub.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.management.relation.RoleNotFoundException;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    AppUserRepository userRepository;

    @Mock
    AppRoleRepository roleRepository;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    JavaMailSender mailSender;

    UserService userDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);

        userDetailsService =
                new UserService(
                        roleRepository,
                        userRepository,
                        verificationTokenRepository,
                        mailSender);
    }

    @Test
    void register_new_user_saves_with_user_role() throws RoleNotFoundException {

        String userName = "new_über-user";
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

        Mockito.when(verificationTokenRepository.save(Mockito.any(VerificationToken.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        AppUser userAccount = userDetailsService.registerAppUser(newUser);
        System.out.println(userAccount);
        // user name
        assertThat(userAccount.getUserName()).isEqualTo(userName);
        // user email
        assertThat(userAccount.getEmail()).isEqualTo(email);
        // password encoded
        assertThat(SecurityUtils.passwordEncoder()
                .matches(userClearPassword, userAccount.getEncryptedPassword())).isTrue();

        // role names
        assertThat(userAccount.getRoles().stream().map(AppRole::getRoleName)).allMatch(s -> s.equals("USER"));
        // user status
        assertThat(userAccount.getStatus()).isEqualTo(AppUser.UserStatus.UNCONFIRMED);
        // try to send the confirmation mail
        ArgumentCaptor<VerificationToken> tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);
        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(verificationTokenRepository).save(tokenCaptor.capture());
        Mockito.verify(mailSender).send(emailCaptor.capture());

        List<SimpleMailMessage> sentMails = emailCaptor.getAllValues();
        assertThat(sentMails.size()).isOne();
        List<VerificationToken> tokensSaved = tokenCaptor.getAllValues();
        assertThat(tokensSaved.size()).isOne();
        assertThat(sentMails.get(0).getText()).contains(tokensSaved.get(0).getToken());
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

        assertThatThrownBy(() -> userDetailsService.registerAppUser(user))
                .isInstanceOf(RoleNotFoundException.class);
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
            assertThatThrownBy(() -> userDetailsService.registerAppUser(user))
                    .isInstanceOf(ValidationException.class);
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
                        "mmmmmmmmmmmmmmmmmmeeeeeeeeeeeeeeeeeeeeeeeeeeee"
                        , "§$%&", "user-", "-user!"};

        Arrays.asList(invalidUsernames).forEach(username -> {
            user.setUserName(username);
            assertThatThrownBy(() -> userDetailsService.registerAppUser(user))
                    .isInstanceOf(ValidationException.class);
        });
    }

    private AppRole newRole() {
        AppRole userRole = new AppRole();
        userRole.setRoleName("USER");
        return userRole;
    }

}
