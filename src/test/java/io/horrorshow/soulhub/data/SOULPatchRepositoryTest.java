package io.horrorshow.soulhub.data;

import io.horrorshow.soulhub.data.repository.AppUserRepository;
import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import io.horrorshow.soulhub.service.SOULPatchServiceTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static io.horrorshow.soulhub.data.SPFile.FileType.MANIFEST;
import static io.horrorshow.soulhub.data.SPFile.FileType.SOUL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("SOULHubRepository Tests")
@EnableJpaAuditing
class SOULPatchRepositoryTest {

    private static final HashMap<String, SOULPatch> soulPatches =
            new HashMap<>();
    private static final AppUser user1 = SOULPatchServiceTest.createAppUser();

    @Autowired
    private SOULPatchRepository soulPatchRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeAll
    static void initAll() {
        SOULPatch soulPatch1 = new SOULPatch();
        soulPatch1.setSpFiles(new HashSet<>());

        SPFile file1 = new SPFile();
        file1.setName("soulfile name");
        file1.setFileContent("soulfile content");
        file1.setFileType(SOUL);
        file1.setSoulPatch(soulPatch1);

        SPFile file2 = new SPFile();
        file2.setName("soulpatch manifest 1");
        file2.setFileContent("soulpatch manifest content");
        file2.setFileType(MANIFEST);
        file2.setSoulPatch(soulPatch1);

        soulPatch1.getSpFiles().add(file1);
        soulPatch1.getSpFiles().add(file2);

        SOULPatch soulPatch2 = new SOULPatch();
        soulPatch1.setName("name1");
        soulPatch2.setName("name2");

        soulPatches.put("Valid1", soulPatch1);
        soulPatches.put("Valid2", soulPatch2);
    }

    @BeforeEach
    void init() {
        AppUser user = appUserRepository.save(user1);
        soulPatches.forEach((s, soulPatch) -> soulPatch.setAuthor(user));
        soulPatchRepository.saveAll(soulPatches.values());
    }

    @Test
    void save_and_retrieve_some_soulpatches() {
        List<SOULPatch> patches = soulPatchRepository.findAll();
        for (SOULPatch patch : patches) {
            assertNotNull(patch.getId());
        }
    }

    @Test
    void created_updated_at_on_save_of_soulpatches() {
        var soulPatches = soulPatchRepository.findAll();
        assertTrue(soulPatches.size() > 0, "Result contains items");

        soulPatches.forEach(soulPatch -> {
            assertNotNull(soulPatch.getCreatedAt(), "created at is set");
            assertNotNull(soulPatch.getUpdatedAt(), "updated at is set");
            soulPatch.setName("new name");
        });

        soulPatchRepository.saveAll(soulPatches);
        soulPatchRepository.findAll().forEach(soulPatch ->
                assertTrue(soulPatch.getUpdatedAt().isAfter(soulPatch.getCreatedAt()),
                        "updated after created at date"));
    }
}