package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppRole;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

public class SOULHubUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUserName(userName);
        if (appUser.isPresent()) {
            return getUserDetails(appUser.get());
        } else {
            throw new UsernameNotFoundException(userName);
        }
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

}
