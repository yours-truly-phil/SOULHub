package io.horrorshow.soulswap.service;

import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SOULSwapRepository;
import io.horrorshow.soulswap.exception.ResourceNotFound;
import io.horrorshow.soulswap.xml.SOULFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SOULPatchService {

    private final SOULSwapRepository repository;

    @Autowired
    public SOULPatchService(SOULSwapRepository repository) {
        this.repository = repository;
    }

    public synchronized List<SOULPatch> findAll() {
        return repository.findAll();
    }

    public List<SOULPatch> findAll(String searchTerm) {
        return repository.findAll().stream()
                .filter(e -> {
                    String regex = String.format("^(?i).*%s.*$", searchTerm);
                    return e.getName().matches(regex) ||
                            e.getDescription().matches(regex) ||
                            e.getSoulFileName().matches(regex) ||
                            e.getSoulpatchFileName().matches(regex);
                })
                .collect(Collectors.toList());
    }

    public synchronized List<SOULPatchXMLType> findAllXML() {
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

    public SOULPatch update(Long id, SOULPatch soulPatch) {
        SOULPatch p = findById(id);
        p.setName(soulPatch.getName());
        p.setDescription(soulPatch.getDescription());
        p.setSoulFileName(soulPatch.getSoulFileName());
        p.setSoulFileContent(soulPatch.getSoulFileContent());
        p.setSoulpatchFileName(soulPatch.getSoulpatchFileName());
        p.setSoulpatchFileContent(soulPatch.getSoulpatchFileContent());
        p.setAuthor(soulPatch.getAuthor());
        p.setNoServings(soulPatch.getNoServings());
        return save(p);
    }

    public SOULPatch save(SOULPatch soulPatch) {
        return repository.save(soulPatch);
    }

    public void delete(SOULPatch soulPatch) {
        repository.delete(soulPatch);
    }

    public SOULPatch findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(String.format("SOULPatch Id: %d", id)));
    }

    public void deleteById(Long id) {
        SOULPatch p = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(String.format("SOULPatch Id: %d", id)));
        repository.delete(p);
    }

    public boolean isMatch(SOULPatch patch, SOULPatchXMLType xmlType) {
        try {
            boolean isMatch = xmlType.getId().equals(String.valueOf(patch.getId()));
            isMatch &= xmlType.getSoulfile().get(0).getFilename().equals(patch.getSoulFileName());
            isMatch &= xmlType.getSoulfile().get(0).getFilecontent().equals(patch.getSoulFileContent());
            isMatch &= xmlType.getSoulpatchfile().get(0).getFilename().equals(patch.getSoulpatchFileName());
            isMatch &= xmlType.getSoulpatchfile().get(0).getFilecontent().equals(patch.getSoulpatchFileContent());
            return isMatch;
        } catch (Exception e) {
            return false;
        }
    }
}
