package io.horrorshow.soulhub.data.repository;

import io.horrorshow.soulhub.data.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUserName(String userName);

    Optional<AppUser> findAppUserByEmail(String email);

}
