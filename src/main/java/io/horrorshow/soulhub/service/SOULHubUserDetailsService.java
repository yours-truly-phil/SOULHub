package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.*;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.data.repository.VerificationTokenRepository;
import io.horrorshow.soulhub.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Validator validator;

    public SOULHubUserDetailsService(@Autowired AppRoleRepository appRoleRepository,
                                     @Autowired AppUserRepository appUserRepository,
                                     @Autowired VerificationTokenRepository verificationTokenRepository) {
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
        this.verificationTokenRepository = verificationTokenRepository;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return getUserDetails(loadAppUser(userName));
    }

    public AppUser loadAppUser(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUserName(username);
        return appUser.orElse(null);
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

    @Transactional
    public AppUser registerAppUser(AppUser appUser) throws RoleNotFoundException, ValidationException {
        LOGGER.debug("trying to register app user: {}", appUser.toString());

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

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = createVerificationToken(savedUser, token);
        LOGGER.info("verificationToken created {}", verificationToken);

        return savedUser;
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
        return Optional.ofNullable(loadAppUser(SecurityUtils.getUsername()));
    }

}
