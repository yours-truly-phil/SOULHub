package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import io.horrorshow.soulhub.xml.SOULPatchXMLType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static io.horrorshow.soulhub.data.SPFile.FileType.MANIFEST;
import static io.horrorshow.soulhub.data.SPFile.FileType.SOUL;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SOULPatchServiceTest {

    @Mock
    SOULPatchRepository soulPatchRepository;

    SOULPatchService service;

    @BeforeAll
    static void initAll() {
    }

    static SOULPatch createTestSoulPatch(Long no) {
        SOULPatch p = new SOULPatch();
        p.setId(no);
        p.setName(format("name %s", no));
        p.setDescription(format("description %s", no));
        p.setAuthor(createAppUser());

        p.setNoViews(no);

        p.getSpFiles().add(createTestSPFile(2 * no, SOUL, p));
        p.getSpFiles().add(createTestSPFile(2 * no + 1, MANIFEST, p));
        return p;
    }

    public static AppUser createAppUser() {
        AppUser user = new AppUser();
        user.setId(0L);
        user.setUserName("user1");
        user.setEmail("user1@mail.com");
        user.setEncryptedPassword("$pw");
        user.setStatus(AppUser.UserStatus.ACTIVE);
        return user;
    }

    static SPFile createTestSPFile(Long id, SPFile.FileType fileType, SOULPatch soulPatch) {
        SPFile spFile = new SPFile();
        spFile.setId(id);
        spFile.setSoulPatch(soulPatch);
        spFile.setFileType(fileType);
        spFile.setName(format("%s file %s", fileType.toString(), id));
        spFile.setFileContent(format("%s file content %s", fileType.toString(), id));
        return spFile;
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        service = new SOULPatchService(soulPatchRepository, null);
    }

    @Test
    void create_soulpatchxml_for_all_soulpatches() {

        List<SOULPatch> testSoulPatches = new ArrayList<>();
        for (long id = 0L; id < 20L; id++) {
            testSoulPatches.add(createTestSoulPatch(id));
            testSoulPatches.add(createTestSoulPatch(Long.MAX_VALUE - id));
        }
        Mockito.doReturn(testSoulPatches).when(soulPatchRepository).findAll();

        List<SOULPatchXMLType> xmlSPs = service.findAllXML();
        Mockito.verify(soulPatchRepository).findAll();

        Map<String, SOULPatchXMLType> xmlSPMap =
                xmlSPs.stream().collect(
                        Collectors.toMap(SOULPatchXMLType::getId, it -> it,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new)
                );

        testSoulPatches.forEach(
                soulPatch -> {
                    SOULPatchXMLType xmlPatch = xmlSPMap.get(soulPatch.getId().toString());
                    assertTrue(service.isSPXmlMatchSPData(soulPatch, xmlPatch));
                });
    }

    @Test
    void find_by_pattern_match_in_name_desc_and_filepath() {
        List<SOULPatch> testSoulPatches = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            testSoulPatches.add(createTestSoulPatch((long) i));
            testSoulPatches.add(createTestSoulPatch((long) i));
        }
        Mockito.doReturn(testSoulPatches).when(soulPatchRepository).findAll();

        assertEquals(24, service.findAll("").size());
        assertEquals(24, service.findAll("ption ").size());
        assertEquals(24, service.findAll("PTION ").size());
        assertEquals(18, service.findAll("1").size());
        assertEquals(2, service.findAll("23").size());
        assertEquals(6, service.findAll("name 1").size());
        assertEquals(24, service.findAll("name \\d").size());
        assertEquals(2, service.findAll("name 1$").size());
    }

    @Test
    void soulpatch_to_zip_file() throws IOException {
        SOULPatch soulPatch = createTestSoulPatch(4711L);
        byte[] zipped = service.zipSOULPatchFiles(soulPatch);
        List<String[]> unzipped = unzipSOULPatchZip(zipped);
        evaluateUnzipResult(unzipped, soulPatch);

        Set<SPFile> emptySet = new HashSet<>();
        soulPatch.setSpFiles(emptySet);
        zipped = service.zipSOULPatchFiles(soulPatch);
        unzipped = unzipSOULPatchZip(zipped);
        evaluateUnzipResult(unzipped, soulPatch);
    }

    private void evaluateUnzipResult(List<String[]> unzipped, SOULPatch soulPatch) {
        unzipped.forEach(strings -> {
            assertTrue(soulPatch.getSpFiles().stream()
                    .map(SPFile::getFileContent)
                    .anyMatch(s -> s.equals(strings[1])));
            assertTrue(soulPatch.getSpFiles().stream()
                    .map(SPFile::getName)
                    .anyMatch(s -> s.equals(strings[0])));
        });
    }

    private List<String[]> unzipSOULPatchZip(byte[] bytes) throws IOException {
        List<String[]> result = new ArrayList<>();
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
        byte[] buffer = new byte[1024];
        int len;
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String[] spFileStrings = new String[2];
            spFileStrings[0] = zipEntry.getName();
            StringBuilder sb = new StringBuilder();
            while ((len = zis.read(buffer, 0, buffer.length)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            spFileStrings[1] = sb.toString();
            result.add(spFileStrings);
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return result;
    }
}
