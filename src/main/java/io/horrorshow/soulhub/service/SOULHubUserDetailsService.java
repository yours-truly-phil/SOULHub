package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppRole;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.repository.AppRoleRepository;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SOULHubUserDetailsService implements UserDetailsService {

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public SOULHubUserDetailsService(@Autowired AppRoleRepository appRoleRepository, @Autowired AppUserRepository appUserRepository) {
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
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
    public AppUser registerAppUser(AppUser appUser) throws RoleNotFoundException {
        AppUser user = new AppUser();

        user.setUserName(appUser.getUserName());
        user.setEncryptedPassword(SecurityUtils.encryptPassword(appUser.getEncryptedPassword()));
        Optional<AppRole> userRole = appRoleRepository.findByRoleName("USER");
        userRole.ifPresent(
                appRole -> user.setRoles(Set.of(appRole))
        );
        userRole.orElseThrow(RoleNotFoundException::new);
        user.setStatus(AppUser.UserStatus.ACTIVE);
        logger.debug(String.format("saving %s: %s", AppUser.class.getSimpleName(), user.toString()));
        return appUserRepository.save(user);
    }

    public boolean isCurrentUserOwnerOf(SOULPatch soulPatch) {
        Optional<AppUser> appUser = getCurrentAppUser();
        return appUser.filter(user -> soulPatch.getAuthor().equals(user)).isPresent();
    }

    public Optional<AppUser> getCurrentAppUser() {
        return Optional.ofNullable(loadAppUser(SecurityUtils.getUsername()));
    }

}
