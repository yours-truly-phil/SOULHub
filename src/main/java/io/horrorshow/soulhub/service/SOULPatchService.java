package io.horrorshow.soulhub.service;

import com.helger.commons.annotation.VisibleForTesting;
import io.horrorshow.soulhub.data.*;
import io.horrorshow.soulhub.data.api.SOULPatchParser;
import io.horrorshow.soulhub.data.records.RecordsConverter;
import io.horrorshow.soulhub.data.records.SOULPatchRecord;
import io.horrorshow.soulhub.data.repository.SOULPatchRepository;
import io.horrorshow.soulhub.data.repository.SPFileRepository;
import io.horrorshow.soulhub.data.util.SOULPatchesFetchFilter;
import io.horrorshow.soulhub.exception.ResourceNotFound;
import io.horrorshow.soulhub.xml.SOULFileXMLType;
import io.horrorshow.soulhub.xml.SOULPatchFileXMLType;
import io.horrorshow.soulhub.xml.SOULPatchXMLType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
@Log4j2
public class SOULPatchService {

    private static final int MIN_RATING_STARS = 0;
    private static final int MAX_RATING_STARS = 5;

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

    private Page<SOULPatch> findAnyMatchingFullTextSearch(String s, Pageable pageable) {
        var em = entityManagerFactory.createEntityManager();
        var fullTextEM = Search.getFullTextEntityManager(em);

        em.getTransaction().begin();

        var qb = fullTextEM.getSearchFactory()
                .buildQueryBuilder().forEntity(SOULPatch.class).get();
        var analyzer = fullTextEM.getSearchFactory()
                .getAnalyzer(SOULPatch.class);

        var tokens = tokenizeString(analyzer, s);

        var query = qb
                .keyword()
                .onFields(SOULPatch_.NAME, SOULPatch_.DESCRIPTION)
                .andField(SOULPatch_.SP_FILES + "." + SPFile_.FILE_CONTENT)
                .andField(SOULPatch_.SP_FILES + "." + SPFile_.NAME)
                .matching(s).createQuery();

        var fullTextQuery = fullTextEM.createFullTextQuery(query, SOULPatch.class)
                .setFirstResult(Math.toIntExact(pageable.getPageNumber() * pageable.getPageSize()))
                .setMaxResults(Math.toIntExact(pageable.getPageSize()));

        List<?> resultList = fullTextQuery.getResultList();
        var queryResults = resultList.stream().filter(o -> o instanceof SOULPatch)
                .map(o -> (SOULPatch) o)
                .collect(Collectors.toList());

        Page<SOULPatch> pageResult = new PageImpl<>(queryResults, pageable, queryResults.size());

        em.getTransaction().commit();
        em.close();
        return pageResult;
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
            log.debug("Error tokenizing string {}", string, e);
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
        soulPatch.setDescription("describe your SOULPatch");
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
        log.debug("soulpatch save: {}", soulPatch);
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
        log.debug("SOULPatch download event, incremented counter: {}", soulPatch);
        return save(soulPatch);
    }

    public InputStream getZipSOULPatchStreamProvider(SOULPatch soulPatch) {
        return new ByteArrayInputStream(zipSOULPatchFiles(soulPatch));
    }

    public byte[] zipSOULPatchFiles(SOULPatch soulPatch) {
        try (final var baos = new ByteArrayOutputStream();
             final var zos = new ZipOutputStream(baos)) {

            Map<String, Integer> filenames = new HashMap<>();
            for (SPFile spFile : soulPatch.getSpFiles()) {

                String filename = appendNoIfDuplicateFilename(filenames, spFile.getName());

                ZipEntry entry = new ZipEntry(filename);
                entry.setSize(spFile.getFileContent().getBytes().length);
                zos.putNextEntry(entry);
                zos.write(spFile.getFileContent().getBytes());
                zos.closeEntry();
            }
            zos.finish();
            zos.flush();
            zos.close();
            log.debug("zipped soulpatch files {}", soulPatch);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("error zipping soulpatch {}", soulPatch, e);
            return "ErrorZippingFiles".getBytes();
        }
    }

    @VisibleForTesting
    String appendNoIfDuplicateFilename(Map<String, Integer> filenames, String filename) {
        if(filenames.containsKey(filename)) {
            filenames.put(filename, filenames.get(filename) + 1);
        } else {
            filenames.put(filename, 0);
        }

        if(filenames.get(filename) > 0) {
            filename += "_(" + filenames.get(filename) + ")";
        }
        return filename;
    }

    private Sort.Order getFirstSortOrder(Pageable pageable) {
        if (pageable.getSort().get().findFirst().isPresent()) {
            return pageable.getSort().get().findFirst().get();
        } else {
            return new Sort.Order(Sort.Direction.ASC, SOULPatch_.NAME);
        }
    }

    private Page<SOULPatch> findAnyMatchingFiltered(SOULPatchesFetchFilter filter, Pageable pageable) {
        var em = entityManagerFactory.createEntityManager();
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(SOULPatch.class);
        var root = cq.from(SOULPatch.class);

        Predicate predicate = getPredicate(filter, cb, root);
        cq.where(getPredicate(filter, cb, root));

        Order order = getOrderBy(pageable, cb, cq, root);
        cq.orderBy(order);

        var query = em.createQuery(cq);
        query.setFirstResult(Math.toIntExact(pageable.getOffset()));
        query.setMaxResults(pageable.getPageSize());

        long count = em.createQuery(countAnyMatching(cb, predicate)).getSingleResult();
        Page<SOULPatch> pageResult = new PageImpl<>(query.getResultList(), pageable, count);
        em.close();
        return pageResult;
    }

    public Page<SOULPatch> findAnyMatching(SOULPatchesFetchFilter filter, Pageable pageable) {
        if (filter.getFullTextSearch().isPresent()) {
            return findAnyMatchingFullTextSearch(filter.getFullTextSearch().get(), pageable);
        } else {
            return findAnyMatchingFiltered(filter, pageable);
        }
    }

    private Predicate getPredicate(SOULPatchesFetchFilter filter, CriteriaBuilder cb, Root<SOULPatch> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getNamesFilter().isPresent()) {
            var namesFilter = cb.like(cb.lower(root.get(SOULPatch_.NAME)),
                    "%" + filter.getNamesFilter().get().toLowerCase(Locale.US) + "%");
            predicates.add(namesFilter);
        }

        List<Predicate> userPredicates = new ArrayList<>();
        for (AppUser user : filter.getUsersFilter()) {
            userPredicates.add(cb.equal(root.get(SOULPatch_.AUTHOR).get(SOULPatchRating_.ID), user.getId()));
        }
        if (userPredicates.size() > 0) {
            var userOrPredicate = cb.or(userPredicates.toArray(new Predicate[0]));
            predicates.add(userOrPredicate);
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private CriteriaQuery<Long> countAnyMatching(CriteriaBuilder cb, Predicate predicate) {
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<SOULPatch> root = cq.from(SOULPatch.class);
        cq.select(cb.count(root));
        cq.where(predicate);
        return cq;
    }

    private Order getOrderBy(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<SOULPatch> cq, Root<SOULPatch> root) {
        Sort.Order sortOrder = getFirstSortOrder(pageable);
        Expression<?> orderBy;
        if (sortOrder.getProperty().equals(SOULPatch_.RATINGS)) {
            Join<SOULPatch, SOULPatchRating> join = root.join(SOULPatch_.RATINGS, JoinType.LEFT);
            var avg = cb.avg(join.get(SOULPatchRating_.STARS));
            cq.select(root).groupBy(root.get(SOULPatch_.ID));
            orderBy = cb.coalesce(avg, 0);
        } else if (sortOrder.getProperty().equals(SOULPatch_.NAME) ||
                sortOrder.getProperty().equals(SOULPatch_.DESCRIPTION)) {
            orderBy = root.get(sortOrder.getProperty());
        } else if (sortOrder.getProperty().equals(SOULPatch_.NO_VIEWS)) {
            orderBy = cb.toLong(root.get(SOULPatch_.NO_VIEWS));
        } else {
            orderBy = root.get(SOULPatch_.NAME);
        }
        return (sortOrder.isAscending()) ? cb.asc(orderBy) : cb.desc(orderBy);
    }

    public int countAnyMatching(SOULPatchesFetchFilter filter) {

        if (filter.getNamesFilter().isPresent() && !filter.getUsersFilter().isEmpty()) {
            return Math.toIntExact(soulPatchRepository.countSOULPatchesByAuthorIdInAndNameContainingIgnoreCase(
                    filter.getUsersFilter().stream().map(AppUser::getId).collect(Collectors.toSet()),
                    filter.getNamesFilter().get()));
        } else if (filter.getNamesFilter().isEmpty() && !filter.getUsersFilter().isEmpty()) {
            return Math.toIntExact(soulPatchRepository.countSOULPatchesByAuthorIdIn(
                    filter.getUsersFilter().stream().map(AppUser::getId).collect(Collectors.toSet())));
        } else if (filter.getNamesFilter().isPresent() && filter.getUsersFilter().isEmpty()) {
            return Math.toIntExact(soulPatchRepository.countSOULPatchesByNameContainingIgnoreCase(
                    filter.getNamesFilter().get()));
        } else {
            return Math.toIntExact(soulPatchRepository.count());
        }
    }

    public void soulPatchDownloaded(@NotNull SOULPatch soulPatch) {
        incrementNoDownloadsAndSave(soulPatch);
    }

    public void spFileDownloaded(@NotNull SPFile spFile) {
        if (spFile.getSoulPatch() != null) {
            incrementNoDownloadsAndSave(spFile.getSoulPatch());
        }
    }

    public void soulPatchRating(SOULPatch sp, Integer v, AppUser user) {
        if (v == null || v < MIN_RATING_STARS || v > MAX_RATING_STARS)
            throw new ValidationException(String.format(
                    "invalid value %d, must be between %d and %d",
                    v, MIN_RATING_STARS, MAX_RATING_STARS));
        if (sp == null) throw new ValidationException("sp must not be null");
        if (user == null) throw new ValidationException("user must not be null");

        sp.getRatings().stream()
                .filter(rating -> rating.getAppUser().equals(user)).distinct()
                .findAny()
                .ifPresentOrElse(
                        rating -> updateRating(rating, v)
                        , () -> createRating(sp, v, user));
    }

    @VisibleForTesting
    void createRating(SOULPatch sp, Integer v, AppUser user) {
        SOULPatchRating rating = new SOULPatchRating();
        rating.setAppUser(user);
        rating.setSoulPatch(sp);
        rating.setStars(v);
        sp.getRatings().add(rating);
        save(sp);
    }

    private void updateRating(SOULPatchRating rating, Integer v) {
        rating.setStars(v);
        save(rating.getSoulPatch());
    }

    public long countTotalDownloads() {
        return soulPatchRepository.totalNoSOULPatchDownloads();
    }
}
