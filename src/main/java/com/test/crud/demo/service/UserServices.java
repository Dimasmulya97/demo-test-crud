package com.test.crud.demo.service;

import com.test.crud.demo.constant.Constants;
import com.test.crud.demo.constant.Validator;
import com.test.crud.demo.dto.LoginRequest;
import com.test.crud.demo.dto.LoginResponse;
import com.test.crud.demo.dto.Response;
import com.test.crud.demo.dto.UserRequest;
import com.test.crud.demo.exception.*;
import com.test.crud.demo.model.User;
import com.test.crud.demo.repo.UserRepo;
import com.test.crud.demo.security.JwtUtils;
import com.test.crud.demo.security.service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServices {
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @Transactional
    @SneakyThrows
    public Response<Object> register(UserRequest userRequest) {
        try {
            String message = "";
            User user = new User();
            BCrypt encrypt = new BCrypt();
            String password = userRequest.getPassword() != null ? encrypt.hashpw(userRequest.getPassword(),BCrypt.gensalt()) : "";
            if (userRequest.getUsername() == null) {
                message = Validator.Message.INVALID_NULL.replace("{value}", "Nama");
                throw validatorException(message, userRequest.getName());
            }
            if (userRequest.getRole() == null) {
                message = Validator.Message.INVALID_NULL.replace("{value}", "Role");
                throw validatorException(message, userRequest.getRole());
            }
            if (userRequest.getPassword() == null) {
                message = Validator.Message.INVALID_NULL.replace("{value}", "Password");
                throw validatorException(message, userRequest.getPassword());
            }
            if (!Pattern.matches(Validator.Regex.ALPHANUMERIC_PATTERN, userRequest.getName())) {
                message = Validator.Message.INVALID_FAILED.replace("{value}", "Nama anda");
                throw validatorException(message, userRequest.getName());
            }
            if (!Pattern.matches(Validator.Regex.ALPHANUMERIC_PATTERN, userRequest.getRole())) {
                message = Validator.Message.INVALID_FAILED.replace("{value}", "Role");
                throw validatorException(message, userRequest.getName());
            }
            user.setName(userRequest.getName());
            user.setUsername(userRequest.getUsername());
            user.setPassword(password);
            user.setRole(userRequest.getRole());
            userRepo.save(user);

            log.info("User created: {}", user);
            return Response.builder()
                    .responseCode(Constants.Response.SUCCESS_CODE)
                    .responseMessage(Constants.Response.SUCCESS_MESSAGE)
                    .data(user)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
    public Response<Object> login(LoginRequest loginRequest) {
        try {
            BCrypt encrypt = new BCrypt();
            User user = userRepo.findByUsername(loginRequest.getUsername());
            if (Objects.isNull(user)) {
                throw new UnauthorizedException(Constants.Message.INVALID_USERNAME_MESSAGE);
            }
            if (!encrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                throw new UnauthorizedException(Constants.Message.INVALID_PASSWORD_MESSAGE);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Mempersiapkan respons login
            LoginResponse response = mappingLoginResponse(user, jwt);
            return Response.builder()
                    .responseCode(Constants.Response.SUCCESS_CODE)
                    .responseMessage(Constants.Response.SUCCESS_MESSAGE)
                    .data(response)
                    .build();
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(Constants.Message.INVALID_PASSWORD_MESSAGE);
        } catch (Exception e) {
            throw new AuthenticationException(Constants.Message.GENERAL_AUTHENTICATION_ERROR);
        }
    }

    public Response<Object> validateAccessToken(String accessToken) {
        boolean isValid = jwtUtils.validateJwtToken(accessToken);
        if (!isValid) {
            throw badRequestCustomException(Constants.Message.INVALID_TOKEN_MESSAGE);
        }
        return Response.builder()
                .responseCode(Constants.Response.SUCCESS_CODE)
                .responseMessage(Constants.Response.SUCCESS_VALID_TOKEN_MESSAGE)
                .build();
    }

    private LoginResponse mappingLoginResponse(User user, String jwt) {

        return LoginResponse.builder()
                .username(user.getUsername())
                .nama(user.getName())
                .accessToken(jwt)
                .tokenType("Bearer")
                .expiresIn(jwtExpirationMs)
                .role(user.getRole())
                .build();
    }

    private boolean isValid(String value, String request) {
        String[] valueList = value.split("\\|");
        List<String> reqList = new ArrayList<>();
        for (int i=0 ; i < valueList.length; i++) {
            if (valueList[i].equals(request)) {
                reqList.add(request);
                break;
            }
        }
        if (reqList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public ValidatorException validatorException(String message, String value) {
        return new ValidatorException(message, value);
    }

    private BadRequestCustomException badRequestCustomException(String message) {
        return new BadRequestCustomException(message);
    }

    private NotFoundException notFoundException(String message) {
        return new NotFoundException(message);
    }

    private RuntimeException runtimeException(String message) {
        return new RuntimeException(message);
    }
}
