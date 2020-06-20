package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SPFile;
import io.horrorshow.soulswap.data.repository.SOULPatchRepository;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.horrorshow.soulswap.data.SPFile.FileType.SOUL;
import static io.horrorshow.soulswap.data.SPFile.FileType.SOULPATCH;
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
        p.setName(String.format("name %s", no));
        p.setDescription(String.format("description %s", no));
        p.setAuthor(String.format("author %s", no));
        p.setNoServings(no);

        p.getSpFiles().add(createTestSPFile(2 * no, SOUL, p));
        p.getSpFiles().add(createTestSPFile(2 * no + 1, SOULPATCH, p));
        return p;
    }

    static SPFile createTestSPFile(Long id, SPFile.FileType fileType, SOULPatch soulPatch) {
        SPFile spFile = new SPFile();
        spFile.setId(id);
        spFile.setSoulPatch(soulPatch);
        spFile.setFileType(fileType);
        spFile.setName(String.format("%s file %s", fileType.toString(), id));
        spFile.setFileContent(String.format("%s file content %s", fileType.toString(), id));
        return spFile;
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        service = new SOULPatchService(soulPatchRepository);
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
}
