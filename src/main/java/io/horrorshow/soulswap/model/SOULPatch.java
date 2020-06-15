package io.horrorshow.soulswap.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "soulpatches")
@Data
public class SOULPatch extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotBlank
    private String soulFileName;
    @Column(columnDefinition = "TEXT")
    private String soulFileContent;
    private String soulpatchFileName;
    @Column(columnDefinition = "TEXT")
    private String soulpatchFileContent;
    private OffsetDateTime offsetDateTime; // mapped to timestamp
    @NotBlank
    private String author;
    private Long noServings;
}
