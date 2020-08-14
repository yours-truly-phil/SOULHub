package io.horrorshow.soulhub.security;

import io.horrorshow.soulhub.data.AppRole;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implements the {@link UserDetailsService}.
 * <p>
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    public UserDetailsServiceImpl(@Autowired AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findAppUserByEmail(email);
        if (user.isPresent()) {
            return getUserDetails(user.get());
        } else if ((user = userRepository.findByUserName(email)).isPresent()) {
            return getUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user present with email: %s", email));
        }
    }

    private UserDetails getUserDetails(AppUser user) {
        User.UserBuilder builder = User.withUsername(user.getEmail());
        builder.password(user.getEncryptedPassword());
        builder.roles(user.getRoles().stream().map(AppRole::getRoleName)
                .collect(Collectors.toSet()).toArray(String[]::new));
        builder.disabled(user.getStatus() == AppUser.UserStatus.UNCONFIRMED);
        return builder.build();
    }
}
