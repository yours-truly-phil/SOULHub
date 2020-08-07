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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            User.UserBuilder builder = User.withUsername(user.get().getUserName());
            builder.password(user.get().getEncryptedPassword());
            builder.roles(user.get().getRoles().stream().map(AppRole::getRoleName)
                    .collect(Collectors.toSet()).toArray(String[]::new));
            builder.disabled(user.get().getStatus() == AppUser.UserStatus.UNCONFIRMED);
            return builder.build();
        } else {
            throw new UsernameNotFoundException(String.format("No user present with username: %s", username));
        }
    }
}
