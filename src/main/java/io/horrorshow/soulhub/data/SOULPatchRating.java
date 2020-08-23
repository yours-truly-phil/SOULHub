package io.horrorshow.soulhub.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static io.horrorshow.soulhub.data.SOULPatchRating.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DB_TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {DB_COL_SOULPATCH, DB_COL_APP_USER})
        },
        indexes = {
                @javax.persistence.Index(name = "soulpatch_ratings_pkey",
                        columnList = DB_COL_ID),
                @javax.persistence.Index(name = "soulpatch_ratings_soulpatch_index",
                        columnList = DB_COL_SOULPATCH)
        })
public class SOULPatchRating extends AuditModel {

    private static final long serialVersionUID = -4128532670280420016L;

    public static final String DB_TABLE_NAME = "soulpatch_ratings";

    public static final String DB_COL_ID = "id";
    public static final String DB_COL_SOULPATCH = "soulpatch_id";
    public static final String DB_COL_APP_USER = "app_user_id";
    public static final String DB_COL_STARS = "stars";

    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "hibernate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @Column(name = DB_COL_ID, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = DB_COL_SOULPATCH, nullable = false, updatable = false)
    private SOULPatch soulPatch;

    @ManyToOne
    @JoinColumn(name = DB_COL_APP_USER, nullable = false, updatable = false)
    private AppUser appUser;

    @Column(name = DB_COL_STARS, nullable = false)
    private Integer stars;
}
