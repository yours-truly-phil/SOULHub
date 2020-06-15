package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.dao.SOULSwapRepository;
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
        List<SOULPatchXMLType> soulPatches = new ArrayList<>();
        repository.findAll().forEach(soulPatch -> {
            SOULPatchXMLType soulPatchXML = new SOULPatchXMLType();
            soulPatchXML.setId(soulPatch.getId().toString());
        });
        return soulPatches;
    }
}
