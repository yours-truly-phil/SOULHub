package io.horrorshow.soulswap;

import io.horrorshow.soulswap.model.SOULPatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SOULSwapRepository extends JpaRepository<SOULPatchEntity, Long> {

}
