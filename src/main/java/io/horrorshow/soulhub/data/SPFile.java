package io.horrorshow.soulhub.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "spfiles", indexes = {
        @javax.persistence.Index(name = "spfiles_pkey",
                columnList = "id"),
        @javax.persistence.Index(name = "spfiles_soulpatch_id_index",
                columnList = "soulpatch_id")
})
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@AnalyzerDef(name = "soulfile_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                })
        })
public class SPFile extends AuditModel {

    public static final String COL_NAME = "name";
    public static final String COL_CONTENT = "content";
    public static final String COL_ID = "id";
    public static final String COL_TYPE = "filetype";
    public static final String COL_SOULPATCH = "soulpatch_id";
    private static final long serialVersionUID = -4509227070431094816L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = COL_ID, updatable = false, nullable = false)
    @DocumentId
    @EqualsAndHashCode.Include
    private Long id = -1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COL_SOULPATCH, nullable = false)
    private SOULPatch soulPatch;

    @Enumerated(EnumType.STRING)
    @Column(name = COL_TYPE)
    private FileType fileType;

    @Column(name = COL_NAME)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, name = COL_NAME)
    @Analyzer(definition = "soulfile_analyzer")
    private String name;

    @Column(columnDefinition = "TEXT", name = COL_CONTENT)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO, name = COL_CONTENT)
    @Analyzer(definition = "soulfile_analyzer")
    private String fileContent;

    public enum FileType {
        SOUL, MANIFEST, OTHER, UNKNOWN
    }
}
