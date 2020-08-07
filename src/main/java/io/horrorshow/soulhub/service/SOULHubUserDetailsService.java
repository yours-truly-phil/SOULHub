package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.*;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.data.repository.VerificationTokenRepository;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.ui.UIConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SOULHubUserDetailsService implements UserDetailsService {

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private static final Logger LOGGER = LoggerFactory.getLogger(SOULHubUserDetailsService.class);

    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailSender mailSender;

    private final Validator validator;

    public SOULHubUserDetailsService(@Autowired AppRoleRepository appRoleRepository,
                                     @Autowired AppUserRepository appUserRepository,
                                     @Autowired VerificationTokenRepository verificationTokenRepository,
                                     @Autowired JavaMailSender mailSender) {
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailSender = mailSender;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return getUserDetails(loadAppUser(userName).get());
    }

    public Optional<AppUser> loadAppUser(String username) {
        return appUserRepository.findByUserName(username);
    }

    public Optional<AppUser> loadAppUserByEmail(String email) throws UsernameNotFoundException {
        return appUserRepository.findAppUserByEmail(email);
    }

    private UserDetails getUserDetails(AppUser appUser) {
        User.UserBuilder builder = User.withUsername(appUser.getUserName());
        builder.password(appUser.getEncryptedPassword());
        builder.roles(appUser.getRoles().stream().map(AppRole::getRoleName)
                .collect(Collectors.toSet()).toArray(String[]::new));
        return builder.build();
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public boolean emailExists(String email) {
        return appUserRepository.findAppUserByEmail(email).isPresent();
    }

    public boolean usernameExists(String username) {
        return appUserRepository.findByUserName(username).isPresent();
    }

    @Transactional
    public AppUser registerAppUser(AppUser appUser) throws RoleNotFoundException, ValidationException {
        LOGGER.debug("trying to register app user: {}", appUser.toString());

        if (usernameExists(appUser.getUserName())) {
            throw new ValidationException("Username unavailable");
        }
        if (emailExists(appUser.getEmail())) {
            throw new ValidationException("Email unavailable");
        }

        AppUser user = new AppUser();
        user.setUserName(appUser.getUserName());
        user.setEmail(appUser.getEmail());
        user.setEncryptedPassword(SecurityUtils.encryptPassword(appUser.getEncryptedPassword()));

        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);
        violations.forEach(violation -> {
            LOGGER.debug("Validation Violation: {}", violation);
            throw new ValidationException(violation.getMessage());
        });

        user.setStatus(AppUser.UserStatus.UNCONFIRMED);

        Optional<AppRole> userRole = appRoleRepository.findByRoleName("USER");
        userRole.ifPresent(
                appRole -> user.getRoles().add(appRole)
        );
        userRole.orElseThrow(RoleNotFoundException::new);
        LOGGER.info("saving new user as: {}", user.toString());

        AppUser savedUser = appUserRepository.save(user);
        LOGGER.info("user registered {}", savedUser);

        final String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = createVerificationToken(savedUser, token);
        final SimpleMailMessage email = constructEmailRegistrationMessage(savedUser, verificationToken);
        mailSender.send(email);
        LOGGER.info("verification email sent to {}", savedUser);

        return savedUser;
    }

    private SimpleMailMessage constructEmailRegistrationMessage(final AppUser user, final VerificationToken token) {
        final String recipientAddress = user.getEmail();
        final String subject = String.format("%s Registration Confirmation", UIConst.TITLE);
        final String url = "localhost:8080"; // TODO setup environment config
        final String confirmationUrl = String.format("%s/confirm?token=%s", url, token.getToken());
        final String message = String.format("You registered successfully. To confirm your registration, please click on the below link.\r\n%s", confirmationUrl);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("system@soulhub.info");
        return email;
    }

    public VerificationToken createVerificationToken(AppUser appUser, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(appUser);
        verificationToken.setToken(token);
        return verificationTokenRepository.save(verificationToken);
    }

    public boolean isCurrentUserOwnerOf(SOULPatch soulPatch) {
        Optional<AppUser> appUser = getCurrentAppUser();
        return appUser.filter(user -> soulPatch.getAuthor().equals(user)).isPresent();
    }

    public boolean isCurrentUserOwnerOf(SPFile spFile) {
        return isCurrentUserOwnerOf(spFile.getSoulPatch());
    }

    public Optional<AppUser> getCurrentAppUser() {
        return loadAppUser(SecurityUtils.getUsername());
    }

}
