package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SOULSwapRepository;
import io.horrorshow.soulswap.xml.SOULFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SOULPatchServiceTest {

    @Mock
    SOULSwapRepository mockedRepository;

//    @InjectMocks
    SOULPatchService soulPatchService;

    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        soulPatchService = new SOULPatchService(mockedRepository);
    }

    @Test
    void create_soulpatchxml_for_all_soulpatches() {

        List<SOULPatch> testSoulPatches = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            testSoulPatches.add(createTestSoulPatch((long) i));
            testSoulPatches.add(createTestSoulPatch(Long.MAX_VALUE - (long) i));
        }
        Mockito.doReturn(testSoulPatches).when(mockedRepository).findAll();
//        Mockito.when(mockedRepository.findAll()).thenReturn(testSoulPatches);

        List<SOULPatchXMLType> xmlSPs = soulPatchService.findAllXML();
        Mockito.verify(mockedRepository).findAll();

        Map<String, SOULPatchXMLType> xmlSPMap =
                xmlSPs.stream().collect(
                        Collectors.toMap(SOULPatchXMLType::getId, it -> it,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new)
                );

        testSoulPatches.forEach(it -> {
            // TODO: create equals method to compare xml type with soulpatch
            SOULPatchXMLType xmlSP = xmlSPMap.get(it.getId().toString());
            assertEquals(it.getId().toString(), xmlSP.getId());

            SOULFileXMLType xmlsoulfile = xmlSP.getSoulfile().get(0);

            assertEquals(it.getSoulFileName(), xmlsoulfile.getFilename());
            assertEquals(it.getSoulFileContent(), xmlsoulfile.getFilecontent());

            SOULPatchFileXMLType xmlsoulpatchfile = xmlSP.getSoulpatchfile().get(0);

            assertEquals(it.getSoulpatchFileName(), xmlsoulpatchfile.getFilename());
            assertEquals(it.getSoulpatchFileContent(), xmlsoulpatchfile.getFilecontent());
        });
    }

    static SOULPatch createTestSoulPatch(Long no) {
        SOULPatch p = new SOULPatch();
        p.setId(no);
        p.setName(String.format("name %s", no));
        p.setDescription(String.format("description %s", no));
        p.setSoulFileName(String.format("soulfile name %s", no));
        p.setSoulFileContent(String.format("soulfile content %s", no));
        p.setSoulpatchFileName(String.format("soulpatchfile name %s", no));
        p.setSoulpatchFileContent(String.format("soulpatchfile content %s", no));
        ;
        p.setAuthor(String.format("author %s", no));
        p.setNoServings(no);
        return p;
    }
}
