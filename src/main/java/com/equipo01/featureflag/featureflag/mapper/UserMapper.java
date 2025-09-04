package com.equipo01.featureflag.featureflag.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
 
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
    /**
     * Logger para el mapeo de usuarios.
     */
    public final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    // User -> UserDTO
    UserDTO userToUserDTO(User user);
    
    // UserDTO -> User
    @Mapping(target = "password", ignore = true)
    User userDTOToUser(UserDTO userDTO);

    // List<User> -> List<UserDTO>
    List<UserDTO> userListToUserDTOList(List<User> listUsers);

    // List<UserDTO> -> List<User>
     @Mapping(target = "password", ignore = true)
    List<User> userDTOListToUsersList(List<UserDTO> listUsersDTOs);
}