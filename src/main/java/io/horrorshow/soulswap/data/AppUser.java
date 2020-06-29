package io.horrorshow.soulswap.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "app_user_user_name_key", columnNames = "user_name")
        })
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column(name = "status", nullable = false)
    private UserStatus status;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles;

    public enum UserStatus {
        ACTIVE, INACTIVE
    }
}
