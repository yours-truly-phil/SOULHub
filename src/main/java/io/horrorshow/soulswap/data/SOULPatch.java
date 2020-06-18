package io.horrorshow.soulswap.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Indexed
@Table(name = "soulpatches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SOULPatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String name;

    @Column(columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String description;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String soulFileName;

    @Column(columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String soulFileContent;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String soulpatchFileName;

    @Column(columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String soulpatchFileContent;

    private String author;

    private Long noServings;
}
