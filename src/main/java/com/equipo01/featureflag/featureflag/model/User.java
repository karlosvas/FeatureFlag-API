package com.equipo01.featureflag.featureflag.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Set;
import com.equipo01.featureflag.featureflag.model.enums.Role;

/**
 * Entidad que representa a un usuario en el sistema.
 * Incluye detalles como nombre de usuario, contraseña (hashed), roles y estado activo.
 * Utilizado para autenticación y autorización.
 * 
 * id: Identificador único del usuario.
 * username: Nombre de usuario para login.
 * password: Contraseña (hashed).
 * roles: Roles asignados (USER, ADMIN, GUEST).
 * active: Usuario activo/inactivo.
 * 
 * {@link Getter} Genera los métodos get automáticamente.
 * {@link Setter} Genera los métodos set automáticamente.
 * {@link Builder} Genera el patrón builder para la clase.
 * {@link Entity} Anotación de JPA que indica que esta clase es una entidad persistente.
 * {@link Table} Anotación de JPA que especifica el nombre de la tabla
 */
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;   

    @Column(nullable = false, unique = true)
    private String username;  

    @Column(nullable = false)
    private String email;  

    @Column(nullable = false)
    private String password; 

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles; 
}

