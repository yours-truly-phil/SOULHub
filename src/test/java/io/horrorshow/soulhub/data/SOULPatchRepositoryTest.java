package io.horrorshow.soulhub.data;

import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static io.horrorshow.soulhub.data.SPFile.FileType.SOUL;
import static io.horrorshow.soulhub.data.SPFile.FileType.SOULPATCH;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("SOULHubRepository Tests")
class SOULPatchRepositoryTest {

    private static final HashMap<String, SOULPatch> soulPatches = new HashMap<>();
    @Autowired
    private SOULPatchRepository repository;

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
        file2.setFileType(SOULPATCH);
        file2.setSoulPatch(soulPatch1);

        soulPatch1.getSpFiles().add(file1);
        soulPatch1.getSpFiles().add(file2);

        SOULPatch soulPatch2 = new SOULPatch();
        soulPatch1.setName("name1");
        soulPatch2.setName("name2");
        soulPatch1.setAuthor("author1");
        soulPatch2.setAuthor("author2");

        soulPatches.put("Valid1", soulPatch1);
        soulPatches.put("Valid2", soulPatch2);
    }

    @Test
    void save_and_retrieve_some_soulpatches() {
        repository.saveAll(soulPatches.values());
        List<SOULPatch> patches = repository.findAll();
        for (SOULPatch patch : patches) {
            assertNotNull(patch.getId());
        }
    }
}