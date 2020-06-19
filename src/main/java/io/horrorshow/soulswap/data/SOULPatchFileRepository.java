package io.horrorshow.soulswap.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SOULPatchFileRepository extends JpaRepository<SOULPatchFile, Long> {
}
