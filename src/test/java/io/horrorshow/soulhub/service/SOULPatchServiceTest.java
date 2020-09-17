package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import io.horrorshow.soulhub.data.repository.SPFileRepository;
import io.horrorshow.soulhub.xml.SOULPatchXMLType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManagerFactory;
import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static io.horrorshow.soulhub.data.SPFile.FileType.MANIFEST;
import static io.horrorshow.soulhub.data.SPFile.FileType.SOUL;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SOULPatchServiceTest {

    @Mock
    SOULPatchRepository soulPatchRepository;
    @Mock
    SPFileRepository spFileRepository;
    @Mock
    EntityManagerFactory entityManagerFactory;

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
        service = new SOULPatchService(soulPatchRepository, spFileRepository, entityManagerFactory);
    }

    @Test
    void findAll_returns_all() {
        var sp1 = createTestSoulPatch(1L);
        var sp2 = createTestSoulPatch(2L);

        when(soulPatchRepository.findAll()).thenReturn(List.of(sp1, sp2));

        var spList = service.findAll();

        assertThat(spList).containsAll(List.of(sp1, sp2));
    }

    @Test
    void createSOULPatch_adds_created_app_user() {
        var testUser = new AppUser();
        testUser.setId(4711L);

        when(soulPatchRepository.saveAndFlush(any(SOULPatch.class))).then(returnsFirstArg());

        SOULPatch soulPatch = service.createSOULPatch(testUser);

        assertThat(soulPatch.getAuthor()).isEqualTo(testUser);
    }

    @Test
    void incrementNoDownloadsAndSave() {
        var soulPatch = new SOULPatch();
        long counter = 1337L;
        soulPatch.setNoViews(counter);

        when(soulPatchRepository.saveAndFlush(any(SOULPatch.class))).then(returnsFirstArg());

        var res = service.incrementNoDownloadsAndSave(soulPatch);
        assertThat(res.getNoViews()).isEqualTo(counter + 1L);
    }

    @Test
    void isPossibleSOULPatchId() {
        assertThat(service.isPossibleSOULPatchId(null)).isFalse();
        assertThat(service.isPossibleSOULPatchId(null)).isFalse();
        assertThat(service.isPossibleSOULPatchId("")).isFalse();
        assertThat(service.isPossibleSOULPatchId("a")).isFalse();
        assertThat(service.isPossibleSOULPatchId("§&§$&%")).isFalse();

        when(soulPatchRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        assertThat(service.isPossibleSOULPatchId("1")).isTrue();
        assertThat(service.isPossibleSOULPatchId(String.valueOf(Long.MAX_VALUE))).isTrue();
        when(soulPatchRepository.existsById(-1L)).thenReturn(Boolean.FALSE);
        assertThat(service.isPossibleSOULPatchId("-1")).isFalse();
    }

    @Test
    void isPossibleSPFileId() {
        assertThat(service.isPossibleSPFileId(null)).isFalse();
        assertThat(service.isPossibleSPFileId("")).isFalse();
        assertThat(service.isPossibleSPFileId("a")).isFalse();
        assertThat(service.isPossibleSPFileId("§&§$&%")).isFalse();

        when(spFileRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        assertThat(service.isPossibleSPFileId("1")).isTrue();
        assertThat(service.isPossibleSPFileId(String.valueOf(Long.MAX_VALUE))).isTrue();
        when(spFileRepository.existsById(-1L)).thenReturn(Boolean.FALSE);
        assertThat(service.isPossibleSPFileId("-1")).isFalse();
    }

    @Test
    void downloading_soulpatches_increments_download_counter() {
        var soulPatch = new SOULPatch();
        soulPatch.setNoViews(0L);

        var captor = ArgumentCaptor.forClass(SOULPatch.class);
        service.soulPatchDownloaded(soulPatch);
        verify(soulPatchRepository).saveAndFlush(captor.capture());

        var savedResult = captor.getValue();

        assertThat(savedResult.getNoViews()).isEqualTo(1L);
    }

    @Test
    void downloading_spfiles_increments_download_counter() {
        var spFile = new SPFile();
        var soulPatch = new SOULPatch();
        soulPatch.getSpFiles().add(spFile);
        spFile.setSoulPatch(soulPatch);
        soulPatch.setNoViews(0L);

        var captor = ArgumentCaptor.forClass(SOULPatch.class);
        service.spFileDownloaded(spFile);
        verify(soulPatchRepository).saveAndFlush(captor.capture());

        var savedResultSOULPatch = captor.getValue();

        assertThat(savedResultSOULPatch.getNoViews()).isEqualTo(1L);
    }

    @Test
    void create_soulpatchxml_for_all_soulpatches() {

        List<SOULPatch> testSoulPatches = new ArrayList<>();
        for (long id = 0L; id < 20L; id++) {
            testSoulPatches.add(createTestSoulPatch(id));
            testSoulPatches.add(createTestSoulPatch(Long.MAX_VALUE - id));
        }
        doReturn(testSoulPatches).when(soulPatchRepository).findAll();

        List<SOULPatchXMLType> xmlSPs = service.findAllXML();
        verify(soulPatchRepository).findAll();

        Map<String, SOULPatchXMLType> xmlSPMap =
                xmlSPs.stream().collect(
                        Collectors.toMap(SOULPatchXMLType::getId, it -> it,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new)
                );
        testSoulPatches.forEach(
                soulPatch -> {
                    SOULPatchXMLType xmlPatch = xmlSPMap.get(soulPatch.getId().toString());
                    assertThat(service.isSPXmlMatchSPData(soulPatch, xmlPatch)).isTrue();
                });
    }

    @Test
    void find_by_pattern_match_in_name_desc_and_filepath() {
        List<SOULPatch> testSoulPatches = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            testSoulPatches.add(createTestSoulPatch((long) i));
            testSoulPatches.add(createTestSoulPatch((long) i));
        }
        doReturn(testSoulPatches).when(soulPatchRepository).findAll();

        assertThat(service.findAll("").size()).isEqualTo(24);
        assertThat(service.findAll("ption ").size()).isEqualTo(24);
        assertThat(service.findAll("PTION ").size()).isEqualTo(24);
        assertThat(service.findAll("1").size()).isEqualTo(18);
        assertThat(service.findAll("23").size()).isEqualTo(2);
        assertThat(service.findAll("name 1").size()).isEqualTo(6);
        assertThat(service.findAll("name \\d").size()).isEqualTo(24);
        assertThat(service.findAll("name 1$").size()).isEqualTo(2);
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

    @Test
    void append_number_to_filename_when_zipping_duplicate_filenames() {
        Map<String, Integer> filenames = new HashMap<>();
        assertThat(service.appendNoIfDuplicateFilename(filenames, "common")).isEqualTo("common");
        assertThat(service.appendNoIfDuplicateFilename(filenames, "common")).isEqualTo("common_(1)");
        assertThat(service.appendNoIfDuplicateFilename(filenames, "unique")).isEqualTo("unique");
        assertThat(service.appendNoIfDuplicateFilename(filenames, "common")).isEqualTo("common_(2)");
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

    @Test
    void createRating_puts_app_user_and_stars_as_rating_into_soulpatch() {
        var sp = new SOULPatch();
        int rating = 5;
        var appUser = new AppUser();
        appUser.setId(4711L);
        var captor = ArgumentCaptor.forClass(SOULPatch.class);

        service.createRating(sp, rating, appUser);
        verify(soulPatchRepository).saveAndFlush(captor.capture());

        var resultSoulPatch = captor.getValue();
        var ratingsWithCorrectUser = resultSoulPatch
                .getRatings().stream()
                .filter(r -> r.getAppUser().getId() == 4711L)
                .collect(Collectors.toList());
        assertThat(ratingsWithCorrectUser.size()).isEqualTo(1);
        assertThat(ratingsWithCorrectUser.get(0).getStars()).isEqualTo(5);
    }

    @Test
    void soulpatch_rating_throws_validation_exception_if_invalid_input() {
        var sp = new SOULPatch();
        var appUser = new AppUser();

        assertThrows(ValidationException.class,
                () -> service.soulPatchRating(sp, -1, appUser));
        assertThrows(ValidationException.class,
                () -> service.soulPatchRating(sp, 6, appUser));
        assertThrows(ValidationException.class,
                () -> service.soulPatchRating(sp, null, appUser));

        assertThrows(ValidationException.class,
                () -> service.soulPatchRating(null, 0, appUser));
        assertThrows(ValidationException.class,
                () -> service.soulPatchRating(sp, 0, null));
    }
}
