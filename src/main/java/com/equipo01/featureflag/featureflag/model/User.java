package com.equipo01.featureflag.featureflag.model;

import com.equipo01.featureflag.featureflag.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity representing a user in the system. Includes details such as username, password (hashed),
 * roles, and active status. Used for authentication and authorization.
 *
 * <p>Annotations used - {@link Entity} JPA annotation indicating that this class is a persistent
 * entity. - {@link Table} JPA annotation that specifies the table name. - {@link NoArgsConstructor}
 * Generates a no-arguments constructor. - {@link AllArgsConstructor} Generates a constructor with
 * all arguments. - {@link Getter} Automatically generates get methods. - {@link Setter}
 * Automatically generates set methods. - {@link Builder} Generates the builder pattern for the
 * class. - {@link Columns} Used to specify details about the database columns. - {@link
 * UserDetails} Interface that provides information about the user for authentication and
 * authorization.
 *
 * <p>Attributes id: Unique user identifier. username: Username for login. password: Password
 * (hashed). roles: Assigned roles (USER, ADMIN, GUEST). active: Active/inactive user.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
  /**
   * {@link Id} Indicates the unique identifier of the entity. {@link GeneratedValue} Specifies the
   * value generation strategy for the identifier.
   */
  @Id @GeneratedValue private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = false)
  private Boolean active;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return active;
  }

  @Override
  public boolean isAccountNonLocked() {
    return active;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return active;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }
}
