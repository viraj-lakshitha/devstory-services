package lk.devstory.devstory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lk.devstory.devstory.utils.Role;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "user_details")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullName;

    private String username;

    private String email;

    private String password;

    @Builder.Default
    private Role role = Role.ROLE_USER; // Set default role as User

    private boolean isActive;

    private boolean isLocked;

    private boolean isAccountExpired;

    private boolean isCredentialExpired;
}
