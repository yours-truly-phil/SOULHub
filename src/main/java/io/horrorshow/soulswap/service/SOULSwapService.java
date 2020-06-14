package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.SOULSwapRepository;
import io.horrorshow.soulswap.soulswap.SOULPatch;
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

    public List<SOULPatch> findAll() {
        List<SOULPatch> soulPatches = new ArrayList<>();
        repository.findAll().forEach(soulPatchEntity -> {
            SOULPatch soulPatch = new SOULPatch();
            soulPatch.setId(soulPatchEntity.getId().toString());
        });
        return soulPatches;
    }
}
