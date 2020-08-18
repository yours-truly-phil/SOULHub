package io.horrorshow.soulhub.service;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.data.api.SOULPatchParser;
import io.horrorshow.soulhub.data.records.RecordsConverter;
import io.horrorshow.soulhub.data.records.SOULPatchRecord;
import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import io.horrorshow.soulhub.data.repository.SPFileRepository;
import io.horrorshow.soulhub.exception.ResourceNotFound;
import io.horrorshow.soulhub.xml.SOULFileXMLType;
import io.horrorshow.soulhub.xml.SOULPatchFileXMLType;
import io.horrorshow.soulhub.xml.SOULPatchXMLType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jsoup.helper.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SOULPatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOULPatchService.class);

    private final SOULPatchRepository soulPatchRepository;
    private final SPFileRepository spFileRepository;

    @PersistenceUnit
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public SOULPatchService(SOULPatchRepository soulPatchRepository,
                            SPFileRepository spFileRepository,
                            EntityManagerFactory entityManagerFactory) {
        this.soulPatchRepository = soulPatchRepository;
        this.spFileRepository = spFileRepository;
        this.entityManagerFactory = entityManagerFactory;
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

    @Transactional
    public List<SPFile> fullTextSearchSPFiles(String text) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Validate.notNull(em, "Entity manager must not be null");
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(em);

        em.getTransaction().begin();

        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(SPFile.class).get();

        Query query = qb.keyword().onFields(SPFile.COL_NAME, SPFile.COL_CONTENT)
                .ignoreAnalyzer()
                .ignoreFieldBridge()
                .matching(text).createQuery();

        List<?> queryResultList = fullTextEntityManager
                .createFullTextQuery(query, SPFile.class)
                .getResultList();

        em.getTransaction().commit();
        em.close();

        return queryResultList.stream()
                .filter(SPFile.class::isInstance)
                .map(SPFile.class::cast)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SOULPatch> fullTextSearchSOULPatches(String text) {
        List<SOULPatch> result = new ArrayList<>();
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Validate.notNull(entityManager, "Entity manager can't be null");
            FullTextEntityManager fullTextEntityManager =
                    Search.getFullTextEntityManager(entityManager);
            entityManager.getTransaction().begin();

            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                    .forEntity(SOULPatch.class).get();

            Analyzer customAnalyzer = fullTextEntityManager.getSearchFactory()
                    .getAnalyzer(SOULPatch.class);

            List<String> keywordList = tokenizeString(customAnalyzer, text);

            Query query = qb.keyword().onFields(SOULPatch.FIELD_NAME, SOULPatch.FIELD_DESCRIPTION)
                    .ignoreAnalyzer()
                    .ignoreFieldBridge()
                    .matching(text).createQuery();

            List<?> queryResultList = fullTextEntityManager
                    .createFullTextQuery(query, SOULPatch.class)
                    .getResultList();

            entityManager.getTransaction().commit();
            entityManager.close();

            result.addAll(queryResultList.stream()
                    .filter(SOULPatch.class::isInstance)
                    .map(SOULPatch.class::cast)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.debug("Error fulltext searching for soulpatch", e);
        }
        return result;
    }

    private List<String> tokenizeString(Analyzer analyzer, String string) {
        List<String> result = new ArrayList<>();
        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(string));
            stream.reset();
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
            stream.close();
        } catch (IOException e) {
            LOGGER.debug("Error tokenizing string {}", string, e);
        }
        return result;
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

                    patch.getSpFiles(SPFile.FileType.MANIFEST).forEach(e -> {
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
        soulPatch.setName("my new SOULPatch");
        soulPatch.setAuthor(creator);
        return save(soulPatch);
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
        LOGGER.debug("soulpatch save: {}", soulPatch);
        return soulPatchRepository.saveAndFlush(soulPatch);
    }

    public SPFile saveSPFile(SPFile spFile) {
        spFile.setFileType(SOULPatchParser.guessFileType(spFile));
        return spFileRepository.saveAndFlush(spFile);
    }

    public SPFile createSPFile(SOULPatch soulPatch) {
        SPFile spFile = new SPFile();
        return saveSPFileToSOULPatch(soulPatch, spFile);
    }

    public SPFile saveSPFileToSOULPatch(SOULPatch soulPatch, SPFile spFile) {
        spFile.setSoulPatch(soulPatch);
        soulPatch.getSpFiles().add(spFile);
        return saveSPFile(spFile);
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
        SOULPatch soulPatch = spFile.getSoulPatch();
        soulPatch.getSpFiles().remove(spFile);
        soulPatchRepository.save(soulPatch);
    }

    public boolean isSPXmlMatchSPData(SOULPatch patch, SOULPatchXMLType xmlType) {
        try {
            return xmlType.getId().equals(String.valueOf(patch.getId()));
        } catch (Exception e) {
            return false;
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

    public SOULPatch incrementNoDownloadsAndSave(SOULPatch soulPatch) {
        soulPatch.setNoViews(soulPatch.getNoViews() + 1);
        LOGGER.debug("SOULPatch download event, incremented counter: {}", soulPatch);
        return save(soulPatch);
    }

    public byte[] zipSOULPatchFiles(SOULPatch soulPatch) {
        try (final var baos = new ByteArrayOutputStream();
             final var zos = new ZipOutputStream(baos)) {

            for (SPFile spFile : soulPatch.getSpFiles()) {
                ZipEntry entry = new ZipEntry(spFile.getName());
                entry.setSize(spFile.getFileContent().getBytes().length);
                zos.putNextEntry(entry);
                zos.write(spFile.getFileContent().getBytes());
                zos.closeEntry();
            }
            zos.finish();
            zos.flush();
            zos.close();
            LOGGER.debug("zipped soulpatch files {}", soulPatch);
            return baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("error zipping soulpatch {}", soulPatch, e);
            return null;
        }
    }

    public Page<SOULPatch> findAnyMatching(
            SOULPatchesFetchFilter filter, Pageable pageable) {
        if (filter.getNamesFilter().isPresent() && !filter.getUsersFilter().isEmpty()) {
            return soulPatchRepository.findSOULPatchesByAuthorIdInAndNameContainingIgnoreCase(
                    filter.getUsersFilter().stream().map(AppUser::getId).collect(Collectors.toSet()),
                    filter.getNamesFilter().get(),
                    pageable);
        } else if (filter.getNamesFilter().isEmpty() && !filter.getUsersFilter().isEmpty()) {
            return soulPatchRepository.findSOULPatchesByAuthorIdIn(
                    filter.getUsersFilter().stream().map(AppUser::getId).collect(Collectors.toSet()),
                    pageable);
        } else if (filter.getNamesFilter().isPresent() && filter.getUsersFilter().isEmpty()) {
            return soulPatchRepository.findSOULPatchesByNameContainingIgnoreCase(
                    filter.getNamesFilter().get(),
                    pageable);
        } else {
            return soulPatchRepository.findAll(pageable);
        }
    }

    public int countAnyMatching(SOULPatchesFetchFilter filter) {

        if (filter.getNamesFilter().isPresent() && !filter.getUsersFilter().isEmpty()) {
            return (int) soulPatchRepository.countSOULPatchesByAuthorIdInAndNameContainingIgnoreCase(
                    filter.getUsersFilter().stream().map(AppUser::getId).collect(Collectors.toSet()),
                    filter.getNamesFilter().get());
        } else if (filter.getNamesFilter().isEmpty() && !filter.getUsersFilter().isEmpty()) {
            return (int) soulPatchRepository.countSOULPatchesByAuthorIdIn(
                    filter.getUsersFilter().stream().map(AppUser::getId).collect(Collectors.toSet()));
        } else if (filter.getNamesFilter().isPresent() && filter.getUsersFilter().isEmpty()) {
            return (int) soulPatchRepository.countSOULPatchesByNameContainingIgnoreCase(
                    filter.getNamesFilter().get());
        } else {
            return (int) soulPatchRepository.count();
        }
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class SOULPatchesFetchFilter implements Serializable {

        private static final long serialVersionUID = -1706756888052669980L;

        private final Set<AppUser> usersFilter = new HashSet<>();

        private String namesFilter = null;

        public static SOULPatchesFetchFilter getEmptyFilter() {
            return new SOULPatchesFetchFilter();
        }

        public Optional<String> getNamesFilter() {
            if (namesFilter != null && !namesFilter.isBlank()) {
                return Optional.of(namesFilter);
            } else {
                return Optional.empty();
            }
        }

        public void setNamesFilter(String namesFilter) {
            this.namesFilter = namesFilter;
        }

        public Set<AppUser> getUsersFilter() {
            return usersFilter;
        }
    }
}
