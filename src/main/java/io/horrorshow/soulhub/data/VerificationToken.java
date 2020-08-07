package io.horrorshow.soulhub.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "verification_tokens")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class VerificationToken implements Serializable {

    private static final long serialVersionUID = -5811217464211940833L;

    public static final int EXPIRATION_IN_MINS = 60 * 24;

    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "hibernate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private Long id = -1L;

    @Column(name = "token", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String token;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate = calculateExpiryDate(EXPIRATION_IN_MINS);

    public LocalDateTime calculateExpiryDate(int expiration_time_mins) {
        return LocalDateTime.now().plus(expiration_time_mins, ChronoUnit.MINUTES);
    }
}
