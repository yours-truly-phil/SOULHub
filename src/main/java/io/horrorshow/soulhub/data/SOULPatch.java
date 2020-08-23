package io.horrorshow.soulhub.data;

import lombok.*;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Indexed
@Table(name = "soulpatches", indexes = {
        @javax.persistence.Index(
                name = "soulpatches_pkey",
                columnList = "id", unique = true),

        @javax.persistence.Index(
                name = "soulpatches_no_views_index",
                columnList = "no_views"),

        @javax.persistence.Index(
                name = "soulpatches_name_index",
                columnList = "name"),

        @javax.persistence.Index(
                name = "soulpatches_author_index",
                columnList = "author")
})
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@AnalyzerDef(name = "soulpatch_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                })
        })
public class SOULPatch extends AuditModel {

    private static final long serialVersionUID = -6746949290547828924L;

    public static final String DB_COL_ID = "id";
    public static final String DB_COL_NAME = "name";
    public static final String DB_COL_DESCRIPTION = "description";
    public static final String DB_COL_AUTHOR = "author";
    public static final String DB_COL_DOWNLOADS = "no_views";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = DB_COL_ID, updatable = false, nullable = false)
    @DocumentId
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = DB_COL_NAME)
    @NotBlank
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, name = SOULPatch_.NAME)
    @Analyzer(definition = "soulpatch_analyzer")
    private String name;

    @Column(columnDefinition = "TEXT", name = DB_COL_DESCRIPTION)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, name = SOULPatch_.DESCRIPTION)
    @Analyzer(definition = "soulpatch_analyzer")
    private String description;

    @OneToMany(mappedBy = SPFile_.SOUL_PATCH, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    @IndexedEmbedded
    private Set<SPFile> spFiles = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = DB_COL_AUTHOR, nullable = false)
    private AppUser author;

    @Column(name = DB_COL_DOWNLOADS)
    private Long noViews = 0L;

    @OneToMany(mappedBy = SOULPatchRating_.SOUL_PATCH, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    private Set<SOULPatchRating> ratings = new HashSet<>();

    public List<SPFile> getSpFiles(SPFile.FileType fileType) {
        return spFiles.stream().filter(
                spFile -> spFile.getFileType()
                        .equals(fileType)).collect(Collectors.toList());
    }

    public double getAverageRating() {
        return getRatings().stream().mapToDouble(SOULPatchRating::getStars)
                .average().orElse(0.0d);
    }
}
