package com.isaias.finance.user_service.data.mapper;

import com.isaias.finance.user_service.data.dto.UserBasicDTO;
import com.isaias.finance.user_service.data.dto.UserPublicDTO;
import com.isaias.finance.user_service.data.dto.UserRegistrationRequestDTO;
import com.isaias.finance.user_service.data.entity.User;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface UserMapper {
    User userRegistrationRequestDTOToUser (UserRegistrationRequestDTO userRegistrationRequestDTO);

    UserBasicDTO userToUserBasicDTO (User user);

    UserPublicDTO userToUserPublicDTO (User user);
}