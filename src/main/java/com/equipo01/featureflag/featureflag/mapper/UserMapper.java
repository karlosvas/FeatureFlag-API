package com.equipo01.featureflag.featureflag.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.model.User;
 
/**
 * Mapper que convierte la entidad User a DTO y la DTO a entidad
 * y viceversa.
 * Utiliza MapStruct para generar el código de mapeo automáticamente.
 * 
 * {@link Mapper} Anotación de MapStruct que indica que esta interfaz es un mapper.
 * {@link Mapping} Anotación de MapStruct que indica cómo se deben mapear
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    // User -> UserDTO
    UserDTO userToUserDTO(User user);

    

    // List<User> -> List<UserDTO>
    List<UserDTO> userListToUserDTOList(List<User> listUsers);

    // List<UserDTO> -> List<User>
    List<User> userDTOListToUsersList(List<UserDTO> listUsersDTOs);
}
