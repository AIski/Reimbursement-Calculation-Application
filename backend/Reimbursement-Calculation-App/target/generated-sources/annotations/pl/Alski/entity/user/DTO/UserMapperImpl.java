package pl.Alski.entity.user.DTO;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.Alski.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T20:28:48+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO UserToDto(User claim) {
        if ( claim == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( claim.getId() );
        userDTO.setFirstName( claim.getFirstName() );
        userDTO.setLastName( claim.getLastName() );
        userDTO.setCompanyName( claim.getCompanyName() );

        return userDTO;
    }

    @Override
    public User dtoToUser(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setCompanyName( dto.getCompanyName() );

        return user;
    }

    @Override
    public List<UserDTO> usersToDTOs(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( UserToDto( user ) );
        }

        return list;
    }

    @Override
    public List<User> DTOsToUsers(List<UserDTO> userDTOS) {
        if ( userDTOS == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( userDTOS.size() );
        for ( UserDTO userDTO : userDTOS ) {
            list.add( dtoToUser( userDTO ) );
        }

        return list;
    }
}
