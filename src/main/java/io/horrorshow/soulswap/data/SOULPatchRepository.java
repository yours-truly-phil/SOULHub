package io.horrorshow.soulswap.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SOULPatchRepository extends JpaRepository<SOULPatch, Long> {

}
