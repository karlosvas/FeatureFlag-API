package com.equipo01.featureflag.featureflag.mapper;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper que convierte la entidad User a DTO y la DTO a entidad y viceversa. Utiliza MapStruct para
 * generar el código de mapeo automáticamente.
 *
 * <p>{@link Mapper} Anotación de MapStruct que indica que esta interfaz es un mapper. {@link
 * Mapping} Anotación de MapStruct que indica cómo se deben mapear
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
