package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper interface for converting between User entities and DTOs.
 * 
 * This interface provides bi-directional mapping functionality between User domain
 * entities and their corresponding Data Transfer Objects (DTOs). It handles the
 * conversion logic needed to maintain separation between the data layer and the
 * presentation/API layer for user-related operations.
 * 
 * The mapper automatically generates implementation code at compile time using
 * MapStruct annotations, ensuring type-safe and performant conversions. It uses
 * the shared MapperConfiguration for consistent behavior across all mappers.
 * 
 * Key mapping capabilities:
 * - Bi-directional single entity conversions (User ↔ UserDTO)
 * - Collection mappings for bulk operations
 * - Request DTO to entity conversion with default values
 * - Automatic field mapping based on name matching
 * 
 * Default value assignments:
 * - New users are assigned "USER" role by default
 * - New users are set as active (true) by default
 * 
 */
@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

  /**
   * Converts a User entity to a UserDTO for API responses.
   * 
   * @param user The user entity from the persistence layer
   * @return UserDTO containing all user data for API response
   * @throws IllegalArgumentException if the user parameter is null
   */
  UserDTO userToUserDTO(User user);

  /**
   * Converts a UserDTO to a User entity for persistence operations.
   * 
   * @param userDTO The user DTO from the API layer
   * @return User entity ready for persistence operations
   * @throws IllegalArgumentException if the userDTO parameter is null
   */
  User userDTOToUser(UserDTO userDTO);

  /**
   * Converts a list of User entities to a list of UserDTOs.
   * 
   * @param listUsers List of user entities from the persistence layer
   * @return List of UserDTO objects for API response
   * @throws IllegalArgumentException if the listUsers parameter is null
   */
  List<UserDTO> userListToUserDTOList(List<User> listUsers);

  /**
   * Converts a list of UserDTOs to a list of User entities.
   * 
   * @param listUsersDTOs List of user DTOs from the API layer
   * @return List of User entities ready for persistence operations
   * @throws IllegalArgumentException if the listUsersDTOs parameter is null
   */
  List<User> userDTOListToUsersList(List<UserDTO> listUsersDTOs);

  /**
   * Converts a UserRequestDTO to a UserDTO with default system values.
   * 
   * @param userRequestDTO The user registration request containing basic user information
   * @return UserDTO with request data plus system-assigned default values
   * @throws IllegalArgumentException if the userRequestDTO parameter is null
   */
  @Mapping(target = "role", constant = "USER")
  @Mapping(target = "active", constant = "true")
  UserDTO defaultUserDto(UserRequestDTO userRequestDTO);
}
