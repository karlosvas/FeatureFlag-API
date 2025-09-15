package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper that converts the User entity to DTO and the DTO to entity
 * and vice versa.
 * Uses MapStruct to automatically generate the mapping code.
 * 
 * {@link Mapper} MapStruct annotation indicating that this interface is a mapper.
 * {@link Mapping} MapStruct annotation indicating how to map
 */
@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

  // User -> UserDTO
  UserDTO userToUserDTO(User user);

  // UserDTO -> User
  User userDTOToUser(UserDTO userDTO);

  // List<User> -> List<UserDTO>
  List<UserDTO> userListToUserDTOList(List<User> listUsers);

  // List<UserDTO> -> List<User>
  List<User> userDTOListToUsersList(List<UserDTO> listUsersDTOs);

  // UserRequestDTO -> User
  @Mapping(target = "role", constant = "USER")
  @Mapping(target = "active", constant = "true")
  UserDTO defaultUserDto(UserRequestDTO userRequestDTO);
}
