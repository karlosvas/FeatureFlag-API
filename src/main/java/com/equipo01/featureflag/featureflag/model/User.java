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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entidad que representa a un usuario en el sistema. Incluye detalles como nombre de usuario,
 * contraseña (hashed), roles y estado activo. Utilizado para autenticación y autorización.
 *
 * <p>Anotaciones utilizadas - {@link Entity} Anotación de JPA que indica que esta clase es una
 * entidad persistente. - {@link Table} Anotación de JPA que especifica el nombre de la tabla -
 * {@link NoArgsConstructor} Genera un constructor sin argumentos. - {@link AllArgsConstructor}
 * Genera un constructor con todos los argumentos. - {@link Getter} Genera los métodos get
 * automáticamente. - {@link Setter} Genera los métodos set automáticamente. - {@link Builder}
 * Genera el patrón builder para la clase. - {@link Columns} Se utiliza para especificar detalles
 * sobre las columnas de la base de datos. - {@link UserDetails} Interfaz que proporciona
 * información sobre el usuario para la autenticación y autorización.
 *
 * <p>Atributos id: Identificador único del usuario. username: Nombre de usuario para login.
 * password: Contraseña (hashed). roles: Roles asignados (USER, ADMIN, GUEST). active: Usuario
 * activo/inactivo.
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
   * {@link Id} Indica el identificador único de la entidad. {@link GeneratedValue} Especifica la
   * estrategia de generación de valores para el identificador.
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
