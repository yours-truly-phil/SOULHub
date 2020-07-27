package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.data.records.RecordsConverter;
import io.horrorshow.soulhub.data.records.SOULPatchRecord;
import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import io.horrorshow.soulhub.data.repository.SPFileRepository;
import io.horrorshow.soulhub.exception.ResourceNotFound;
import io.horrorshow.soulhub.xml.SOULFileXMLType;
import io.horrorshow.soulhub.xml.SOULPatchFileXMLType;
import io.horrorshow.soulhub.xml.SOULPatchXMLType;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SOULPatchService {

    private final SOULPatchRepository soulPatchRepository;
    private final SPFileRepository spFileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SOULPatchService(SOULPatchRepository soulPatchRepository,
                            SPFileRepository spFileRepository) {
        this.soulPatchRepository = soulPatchRepository;
        this.spFileRepository = spFileRepository;
    }

    public List<SOULPatch> findAll() {
        return soulPatchRepository.findAll();
    }

    public List<SOULPatchRecord> getAllSOULPatchRecords() {
        return findAll().stream()
                .map(RecordsConverter::newSoulPatchRecord)
                .collect(Collectors.toList());
    }

    public SOULPatchRecord getSOULPatchRecord(Long soulPatchId) {
        return RecordsConverter.newSoulPatchRecord(findById(soulPatchId));
    }

    public List<SOULPatch> findAll(String searchTerm) {
        return soulPatchRepository.findAll().stream()
                .filter(e -> {
                    String regex = String.format("^(?i).*%s.*$", searchTerm);
                    return e.getName().matches(regex) ||
                            e.getDescription().matches(regex) ||
                            e.getSpFiles().stream().anyMatch(
                                    sf -> sf.getName().matches(regex));
                })
                .collect(Collectors.toList());
    }

    public List<SOULPatchXMLType> findAllXML() {
        List<SOULPatchXMLType> xmlPatches = new ArrayList<>();
        List<SOULPatch> soulPatches = soulPatchRepository.findAll();
        soulPatches.forEach(
                patch -> {
                    SOULPatchXMLType soulPatchXML = new SOULPatchXMLType();
                    soulPatchXML.setId(patch.getId().toString());

                    patch.getSpFiles(SPFile.FileType.SOUL).forEach(e -> {
                        SOULFileXMLType xml = new SOULFileXMLType();
                        xml.setId(String.valueOf(e.getId()));
                        xml.setFilename(e.getName());
                        xml.setFilecontent(e.getFileContent());
                        soulPatchXML.getSoulfile().add(xml);
                    });

                    patch.getSpFiles(SPFile.FileType.SOULPATCH).forEach(e -> {
                        SOULPatchFileXMLType xml = new SOULPatchFileXMLType();
                        xml.setId(String.valueOf(e.getId()));
                        xml.setFilename(e.getName());
                        xml.setFilecontent(e.getFileContent());
                        soulPatchXML.getSoulpatchfile().add(xml);
                    });

                    xmlPatches.add(soulPatchXML);
                });
        return xmlPatches;
    }

    public SOULPatch createSOULPatch(AppUser creator) {
        SOULPatch soulPatch = new SOULPatch();
        soulPatch.setAuthor(creator);
        return soulPatch;
    }

    public SOULPatch findById(Long id) {
        return soulPatchRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(String.format("SOULPatch Id: %d", id)));
    }

    public long countSOULPatches() {
        return soulPatchRepository.count();
    }

    public SOULPatch update(Long id, SOULPatch soulPatch) {
        return soulPatchRepository.findById(id).map(sp -> {
            sp.setName(soulPatch.getName());
            sp.setDescription(soulPatch.getDescription());
            sp.setNoViews(soulPatch.getNoViews());
            sp.setUpdatedAt(LocalDateTime.now());
            return soulPatchRepository.saveAndFlush(soulPatch);
        }).orElseThrow(() ->
                new ResourceNotFound(String.format("%s id: %d", SOULPatch.class.getName(), id)));
    }

    public SOULPatch save(SOULPatch soulPatch) {
        return soulPatchRepository.saveAndFlush(soulPatch);
    }

    public SPFile saveSpFile(SPFile spFile) {
        return spFileRepository.saveAndFlush(spFile);
    }

    public SPFile createSPFile(SOULPatch soulPatch) {
        SPFile spFile = new SPFile();
        spFile.setSoulPatch(soulPatch);
        soulPatch.getSpFiles().add(spFile);
        return spFile;
    }

    public Optional<SPFile> findSpFile(Long id) {
        return spFileRepository.findById(id);
    }

    public void delete(SOULPatch soulPatch) {
        soulPatchRepository.delete(soulPatch);
    }

    public void deleteById(Long id) {
        SOULPatch p = soulPatchRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(String.format("SOULPatch Id: %d", id)));
        soulPatchRepository.delete(p);
    }

    public void deleteSpFile(SPFile spFile) {
        spFileRepository.delete(spFile);
    }

    public boolean isSPXmlMatchSPData(SOULPatch patch, SOULPatchXMLType xmlType) {
        try {
            return xmlType.getId().equals(String.valueOf(patch.getId()));
        } catch (Exception e) {
            return false;
        }
    }


    @Transactional
    public void createDatabaseIndex() {
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long countSPFiles() {
        return spFileRepository.count();
    }

    public boolean existsById(Long id) {
        return soulPatchRepository.existsById(id);
    }

    public boolean spFileExistsById(Long id) {
        return spFileRepository.existsById(id);
    }

    public boolean isPossibleSOULPatchId(String parameter) {
        return NumberUtils.isCreatable(parameter)
                && existsById(Long.valueOf(parameter));
    }

    public boolean isPossibleSPFileId(String parameter) {
        return NumberUtils.isCreatable(parameter)
                && spFileExistsById(Long.valueOf(parameter));
    }
}
