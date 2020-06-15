package io.horrorshow.soulswap.dao;

import io.horrorshow.soulswap.model.SOULPatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("SOULSwapRepository Tests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SOULSwapRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SOULSwapRepository repository;

    private static final HashMap<String, SOULPatch> soulPatches = new HashMap<>();

    @BeforeAll
    static void initAll() {
        SOULPatch soulPatch1 = new SOULPatch();
        SOULPatch soulPatch2 = new SOULPatch();
        soulPatch1.setName("name1");
        soulPatch2.setName("name2");
        soulPatch1.setAuthor("author1");
        soulPatch2.setAuthor("author2");

        soulPatches.put("Valid1", soulPatch1);
        soulPatches.put("Valid2", soulPatch2);
    }

    @BeforeEach
    void init() {
        repository.deleteAll();
    }

    @Test
    void find_all_soulpatches() {
        repository.saveAll(soulPatches.values());
        List<SOULPatch> patches = repository.findAll();
        assertTrue(patches.containsAll(soulPatches.values()));
    }

    @Test
    void delete_all_found_soulpatches() {
        repository.saveAll(soulPatches.values());
        List<SOULPatch> soulPatchesInRepo = repository.findAll();
        assertTrue(soulPatchesInRepo.size() > 0);
        soulPatchesInRepo.forEach(soulPatch -> repository.delete(soulPatch));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void delete_soulpatch_By_Id() {
        repository.saveAll(soulPatches.values());
        Long id = soulPatches.get("Valid1").getId();
        assertNotNull(id);
        SOULPatch toBeDeleted = repository.getOne(id);
        assertEquals(id, toBeDeleted.getId());
        repository.deleteById(toBeDeleted.getId());
        assertEquals(Optional.empty(), repository.findById(toBeDeleted.getId()));
    }
}