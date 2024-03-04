package com.aspire.auth.service;

import com.aspire.auth.dto.UserRequestDTO;
import com.aspire.auth.dto.UserResponseDTO;
import com.aspire.common.exceptions.UserAlreadyExistsException;

public interface LoginService {
    UserResponseDTO registerUser(UserRequestDTO reqDTO) throws UserAlreadyExistsException;
}
