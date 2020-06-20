package io.horrorshow.soulswap.data;

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

import static io.horrorshow.soulswap.data.SPFile.FileType.SOUL;
import static io.horrorshow.soulswap.data.SPFile.FileType.SOULPATCH;

@Entity
@Indexed
@Table(name = "soulpatches")
@Data
@EqualsAndHashCode(callSuper = true)
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @DocumentId
    private Long id;

    @NotBlank
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "soulpatch_analyzer")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Analyzer(definition = "soulpatch_analyzer")
    private String description;

    @OneToMany(mappedBy = "soulPatch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<SPFile> spFiles = new HashSet<>();

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String author;

    private Long noServings;

    public List<SPFile> getSoulFiles() {
        return spFiles.stream().filter(
                spFile -> spFile.getFileType()
                        .equals(SOUL)).collect(Collectors.toList());
    }

    public List<SPFile> getSoulpatchFiles() {
        return spFiles.stream().filter(
                spFile -> spFile.getFileType()
                        .equals(SOULPATCH)).collect(Collectors.toList());
    }
}
