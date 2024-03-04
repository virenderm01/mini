package com.aspire.auth.service;

import com.aspire.auth.constants.Role;
import com.aspire.auth.dto.UserRequestDTO;
import com.aspire.auth.dto.UserResponseDTO;
import com.aspire.auth.model.UserEntity;
import com.aspire.auth.repository.UserRepository;
import com.aspire.common.exceptions.UserAlreadyExistsException;
import com.aspire.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO reqDTO) throws UserAlreadyExistsException{
        Optional<UserEntity> optionalUser = userRepository.findByUsername(reqDTO.getUsername());

        if(!optionalUser.isEmpty()){

            throw new UserAlreadyExistsException("Username already exists");
        }
        UserEntity user = UserEntity.builder()
                .email(reqDTO.getEmail())
                .role(Role.USER)
                .enabled(true)
                .password(passwordEncoder.encode(reqDTO.getPassword()))
                .username(reqDTO.getUsername())
                .build();
        user = userRepository.save(user);

        return convertUserModelToDTO(user);
    }

    private UserResponseDTO convertUserModelToDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .userId(user.getId())
                .role(user.getRole().name())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .token(jwtUtil.generateToken(user.getUsername()))
                .build();
    }

}
