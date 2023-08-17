package pl.Alski.entity.user.DTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.Alski.entity.user.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO UserToDto(User claim);
    User dtoToUser(UserDTO dto);
    List<UserDTO> usersToDTOs(List<User> users);
    List<User> DTOsToUsers(List<UserDTO> userDTOS);
}
