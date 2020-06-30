package io.horrorshow.soulhub.data.repository;

import io.horrorshow.soulhub.data.SOULPatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SOULPatchRepository extends JpaRepository<SOULPatch, Long> {

}
