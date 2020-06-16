package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SOULSwapRepository;
import io.horrorshow.soulswap.xml.SOULFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SOULPatchService {

    private final SOULSwapRepository repository;

    @Autowired
    public SOULPatchService(SOULSwapRepository repository) {
        this.repository = repository;
    }

    public List<SOULPatchXMLType> findAllXML() {
        List<SOULPatchXMLType> xmlPatches = new ArrayList<>();
        List<SOULPatch> soulPatches = repository.findAll();
        soulPatches.forEach(
                patch -> {
                    SOULPatchXMLType soulPatchXML = new SOULPatchXMLType();
                    soulPatchXML.setId(patch.getId().toString());

                    SOULFileXMLType sXml = new SOULFileXMLType();
                    sXml.setFilename(patch.getSoulFileName());
                    sXml.setFilecontent(patch.getSoulFileContent());
                    soulPatchXML.getSoulfile().add(sXml);

                    SOULPatchFileXMLType spXml = new SOULPatchFileXMLType();
                    spXml.setFilename(patch.getSoulpatchFileName());
                    spXml.setFilecontent(patch.getSoulpatchFileContent());
                    soulPatchXML.getSoulpatchfile().add(spXml);

                    xmlPatches.add(soulPatchXML);
                });
        return xmlPatches;
    }
}
