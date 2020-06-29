package io.horrorshow.soulswap.data.repository;

import io.horrorshow.soulswap.data.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
}
