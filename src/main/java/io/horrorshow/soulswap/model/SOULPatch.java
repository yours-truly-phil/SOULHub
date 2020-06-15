package io.horrorshow.soulswap.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "soulpatches")
@Data
public class SOULPatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String soulFileName;
    @Column(columnDefinition = "TEXT")
    private String soulFileContent;
    private String soulpatchFileName;
    @Column(columnDefinition = "TEXT")
    private String soulpatchFileContent;
    @NotBlank
    private String author;
    private Long noServings;
}
