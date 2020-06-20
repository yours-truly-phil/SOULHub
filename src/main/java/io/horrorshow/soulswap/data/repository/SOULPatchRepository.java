package io.horrorshow.soulswap.data.repository;

import io.horrorshow.soulswap.data.SOULPatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SOULPatchRepository extends JpaRepository<SOULPatch, Long> {

}
