package io.horrorshow.soulhub.data.repository;

import io.horrorshow.soulhub.data.SOULPatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SOULPatchRepository extends JpaRepository<SOULPatch, Long> {

    Page<SOULPatch> findSOULPatchesByAuthorIdInAndNameContainingIgnoreCase
            (Set<Long> authorId, String nameFilter, Pageable pageable);

    long countSOULPatchesByAuthorIdInAndNameContainingIgnoreCase
            (Set<Long> authorId, String nameFilter);

    Page<SOULPatch> findSOULPatchesByAuthorIdIn
            (Set<Long> authorId, Pageable pageable);

    long countSOULPatchesByAuthorIdIn
            (Set<Long> authorId);

    Page<SOULPatch> findSOULPatchesByNameContainingIgnoreCase
            (String nameFilter, Pageable pageable);

    long countSOULPatchesByNameContainingIgnoreCase
            (String nameFilter);

}
