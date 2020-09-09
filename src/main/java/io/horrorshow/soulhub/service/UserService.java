package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.*;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.data.repository.VerificationTokenRepository;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.ui.UIConst;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@Log4j2
public class UserService {

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailSender mailSender;
    private final Validator validator;
    @Value("${soulhub.url}")
    private String url = "<protocol>://<soulhub-url>:<port>";

    public UserService(@Autowired AppRoleRepository appRoleRepository,
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

    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> loadAppUser(String username) {
        return appUserRepository.findByUserName(username);
    }

    public Optional<AppUser> loadAppUserByEmail(String email) throws UsernameNotFoundException {
        return appUserRepository.findAppUserByEmail(email);
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
        log.debug("trying to register app user: {}", appUser.toString());

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
            log.debug("Validation Violation: {}", violation);
            throw new ValidationException(violation.getMessage());
        });

        user.setStatus(AppUser.UserStatus.UNCONFIRMED);

        Optional<AppRole> userRole = appRoleRepository.findByRoleName("USER");
        userRole.ifPresent(
                appRole -> user.getRoles().add(appRole)
        );
        userRole.orElseThrow(RoleNotFoundException::new);
        log.info("saving new user as: {}", user.toString());

        AppUser savedUser = appUserRepository.save(user);
        log.info("user registered {}", savedUser);

        final SimpleMailMessage email = createTokenMailMessage(savedUser);
        mailSender.send(email);
        log.info("verification email sent to {}", savedUser);

        return savedUser;
    }

    private SimpleMailMessage createTokenMailMessage(AppUser savedUser) {
        final String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = createVerificationToken(savedUser, token);
        return constructEmailRegistrationMessage(savedUser, verificationToken);
    }

    public void resendConfirmationEmail(String email) {
        if (emailExists(email) && loadAppUserByEmail(email).isPresent()) {
            var user = loadAppUserByEmail(email).get();
            if (user.getStatus() == AppUser.UserStatus.UNCONFIRMED) {
                mailSender.send(createTokenMailMessage(user));
            }
        }
    }

    private SimpleMailMessage constructEmailRegistrationMessage(final AppUser user, final VerificationToken token) {
        final String recipientAddress = user.getEmail();
        final String subject = String.format("%s Registration Confirmation", UIConst.TITLE);
        final String url = this.url;
        final String confirmationUrl = String.format("%s/confirm?%s=%s", url, UIConst.PARAM_TOKEN, token.getToken());
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
        if (SecurityUtils.isUserLoggedIn()) return loadAppUserByEmail(SecurityUtils.getUserEmail());
        else return Optional.empty();
    }

    public Optional<VerificationToken> getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public AppUser updateUser(AppUser user) {
        return appUserRepository.saveAndFlush(user);
    }
}
