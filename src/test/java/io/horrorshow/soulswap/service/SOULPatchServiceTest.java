package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SOULPatchRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SOULPatchServiceTest {

    @Mock
    SOULPatchRepository mockedRepository;

    SOULPatchService service;

    @BeforeAll
    static void initAll() {
    }

    static SOULPatch createTestSoulPatch(Long no) {
        SOULPatch p = new SOULPatch();
        p.setId(no);
        p.setName(String.format("name %s", no));
        p.setDescription(String.format("description %s", no));

        fail();// TODO: soulfiles schema change
//        p.setSoulFileName(String.format("soulfile name %s", no));
//        p.setSoulFileContent(String.format("soulfile content %s", no));
//        p.setSoulpatchFileName(String.format("soulpatchfile name %s", no));
//        p.setSoulpatchFileContent(String.format("soulpatchfile content %s", no));
        p.setAuthor(String.format("author %s", no));
        p.setNoServings(no);
        return p;
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        service = new SOULPatchService(mockedRepository);
    }

    @Test
    void create_soulpatchxml_for_all_soulpatches() {

        List<SOULPatch> testSoulPatches = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            testSoulPatches.add(createTestSoulPatch((long) i));
            testSoulPatches.add(createTestSoulPatch(Long.MAX_VALUE - (long) i));
        }
        Mockito.doReturn(testSoulPatches).when(mockedRepository).findAll();

        List<SOULPatchXMLType> xmlSPs = service.findAllXML();
        Mockito.verify(mockedRepository).findAll();

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
        Mockito.doReturn(testSoulPatches).when(mockedRepository).findAll();

        assertEquals(24, service.findAll("").size());
        assertEquals(24, service.findAll("ption ").size());
        assertEquals(24, service.findAll("PTION ").size());
        assertEquals(6, service.findAll("1").size());
        assertEquals(6, service.findAll("name 1").size());
        assertEquals(24, service.findAll("name \\d").size());
        assertEquals(2, service.findAll("name 1$").size());
    }
}
