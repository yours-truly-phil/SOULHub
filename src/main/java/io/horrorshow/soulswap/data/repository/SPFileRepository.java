package io.horrorshow.soulswap.data.repository;

import io.horrorshow.soulswap.data.SPFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SPFileRepository extends JpaRepository<SPFile, Long> {
}
