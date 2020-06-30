package io.horrorshow.soulswap.data;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_role",
        uniqueConstraints = {
                @UniqueConstraint(name = "app_role_role_name_key", columnNames = "role_name")
        })
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AppUser> usersWithRole;
}
