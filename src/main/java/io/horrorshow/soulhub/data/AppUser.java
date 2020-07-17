package io.horrorshow.soulhub.data;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "app_user_user_name_key",
                        columnNames = "user_name")
        })
public class AppUser implements Serializable {

    private static final long serialVersionUID = -4675920971250068707L;

    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "hibernate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 chars")
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid Email address")
    @Size(min = 5, max = 255, message = "Email address must be between 3 and 255 characters")
    private String email;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<AppRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SOULPatchRating> ratings = new HashSet<>();

    public enum UserStatus {
        ACTIVE, INACTIVE
    }
}
