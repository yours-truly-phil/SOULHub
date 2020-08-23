package io.horrorshow.soulhub.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "soulpatch_ratings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"soulpatch_id", "app_user_id"})
        },
        indexes = {
                @javax.persistence.Index(name = "soulppatch_ratings_pkey",
                        columnList = "id"),
                @javax.persistence.Index(name = "soulpatch_ratings_soulpatch_index",
                        columnList = "soulpatch_id")
        })
public class SOULPatchRating extends AuditModel {

    private static final long serialVersionUID = -4128532670280420016L;

    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "hibernate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "soulpatch_id", nullable = false, updatable = false)
    private SOULPatch soulPatch;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false, updatable = false)
    private AppUser appUser;

    @Column(name = "stars", nullable = false)
    private Integer stars;
}
