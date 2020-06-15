package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.SOULSwapRepository;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SOULSwapService {
    private final SOULSwapRepository repository;

    @Autowired
    public SOULSwapService(SOULSwapRepository repository) {
        this.repository = repository;
    }

    public List<SOULPatchXMLType> findAll() {
        List<SOULPatchXMLType> soulPatches = new ArrayList<>();
        repository.findAll().forEach(soulPatchEntity -> {
            SOULPatchXMLType soulPatch = new SOULPatchXMLType();
            soulPatch.setId(soulPatchEntity.getId().toString());
        });
        return soulPatches;
    }
}
