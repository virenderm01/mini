package com.aspire.auth.controller;

import com.aspire.auth.dto.UserRequestDTO;
import com.aspire.auth.dto.UserResponseDTO;
import com.aspire.auth.service.LoginService;
import com.aspire.common.exceptions.AspireApiException;
import com.aspire.common.exceptions.UserAlreadyExistsException;
import com.aspire.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mini-aspire/api/v1/auth")
public class LoginController {
    @Autowired
    private LoginService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerCustomer(@RequestBody UserRequestDTO reqDTO) throws UserAlreadyExistsException {
        UserResponseDTO user = authService.registerUser(reqDTO);
        return new ResponseEntity<>(
                user,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> generateToken(@RequestBody UserRequestDTO authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new AspireApiException( "Invalid username/password", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                UserResponseDTO.builder()
                        .username(authRequest.getUsername())
                        .token(jwtUtil.generateToken(authRequest.getUsername()))
                        .build(),
                HttpStatus.OK
        );

    }
}
