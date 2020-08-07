package io.horrorshow.soulhub.data.repository;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(AppUser user);
}
